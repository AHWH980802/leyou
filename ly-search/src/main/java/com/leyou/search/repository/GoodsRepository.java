package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 15:35 2019/4/30
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
