package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Qin PengCheng
 * @date 2018/5/27
 */
@Table(name = "tb_category")
@Data
public class Category implements Serializable{

    private static final long serialVersionUID = -3137434892299881897L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "is_parent")
    private Boolean isParent;

    private Integer sort;


}