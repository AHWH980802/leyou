package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 18:15 2019/4/16
 */
@Data
public class PageResult<T> {

    /**
     * 总条数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 当前页数据
     */
    private List<T> items;

    public PageResult(){}

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
