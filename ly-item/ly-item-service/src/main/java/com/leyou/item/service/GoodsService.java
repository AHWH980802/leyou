package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.dto.CartDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.awt.print.Pageable;
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 18:26 2019/4/22
 */
@Slf4j
@Service
public class GoodsService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpuByPage(String key, Integer page, Boolean saleable, Integer rows) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //默认排序
        example.setOrderByClause("last_update_time  DESC");

        List<Spu> spus = spuMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //解析分类和品牌的名称
        loadCategoryAndBrandName(spus);

        //解析分页结果
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        return new PageResult<>(spuPageInfo.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));

            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

        }
    }

    /**
     * 新增商品
     * @param spu
     */
    @Transient
    public void saveGoods(Spu spu) {
        //新增spu
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);

        int insert = spuMapper.insert(spu);
        if (insert != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //新增spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuDetail);
        if (insert != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        //新增sku和stock
        saveSkuAndStock(spu);

        //发送mq消息
        amqpTemplate.convertAndSend("item.insert",spu.getId());


    }

    public SpuDetail selectSpuDetailForUpdate(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null) {
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }


    @Transactional
    public List<Sku> selectSkuForUpdate(Long id) {
        //查询商品
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> list = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }

        //查询库存
        /*for (Sku s : list) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock == null){
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }*/

        List<Object> ids = list.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);

        if (CollectionUtils.isEmpty(stocks)) {
            throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        Map<Long, Long> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));

        list.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return list;
    }

    /**
     * 修改商品
     * @param spu
     */
    @Transactional
    public void updateGoods(Spu spu) {
        if(spu.getId() == null){
            throw new LyException(ExceptionEnum.GOODS_ID_CANNOT_BE_NULL);
        }

        //删除sku和stock表的数据
        deleteSkuAndStock(spu.getId());

        //修改sku
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }

        //修改detail
        int i = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(i !=1){
            throw new LyException(ExceptionEnum.SPU_DETAIL_UPDATE_ERROR);
        }

        //新增sku和stock
        saveSkuAndStock(spu);

        //发送mq消息
        amqpTemplate.convertAndSend("item.update",spu.getId());
    }


    /**
     * 添加Sku和Stock的代码模板  方便重用
     * @param spu
     */
    public void saveSkuAndStock(Spu spu){
        //新增sku
        List<Stock> stocks = new ArrayList<>();

        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            int insert = skuMapper.insert(sku);
            if (insert != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }

            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stocks.add(stock);

        }

        //批量新增库存
        int insert = stockMapper.insertList(stocks);
        if (insert != stocks.size()){
            throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
    }

    /**
     * 根据ID修改上架状态
     * @param saleable
     * @param id
     */
    public void saleableGoods(Boolean saleable, Long id) {
        spuMapper.saleableGoods(!saleable,id);
    }

    /**
     * 删除商品
     * @param id
     */
    @Transactional
    public void deleteGoods(Long id) {


        //删除spuDetail表的数据
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        spuDetailMapper.delete(spuDetail);

        //删除sku

        deleteSkuAndStock(id);

        //删除spu表的数据
        spuMapper.deleteByPrimaryKey(id);

        //发送mq消息
        amqpTemplate.convertAndSend("item.delete",id);
    }


    public void deleteSkuAndStock(Long id){
        Sku sku = new Sku();
        sku.setSpuId(id);
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)) {
            //删除sku
            skuMapper.delete(sku);

            //删除stock
            List<Object> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }
    }

    public Spu querySpuById(Long id) {
        // 查询Spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        // 查询sku
        spu.setSkus(selectSkuForUpdate(spu.getId()));

        // 查询detail
        spu.setSpuDetail(selectSpuDetailForUpdate(spu.getId()));
        return spu;
    }

    public List<Sku> selectSkuByIds(List<Object> ids) {
        List<Sku> skuList = skuMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        return loadStockInSku(ids,skuList);
    }

    private List<Sku> loadStockInSku(List<Object> ids,List<Sku> skus) {
        List<Stock> stocks = stockMapper.selectByIdList(ids);

        if (CollectionUtils.isEmpty(stocks)) {
            throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        Map<Long, Long> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));

        skus.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return skus;
    }

    public void decreaseStock(List<CartDto> carts) {
        for (CartDto cart : carts) {
            //  减库存
            int count = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if(count !=1){
                log.error("[库存不足] 购买失败");
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOYGH);
            }
        }
    }
}
