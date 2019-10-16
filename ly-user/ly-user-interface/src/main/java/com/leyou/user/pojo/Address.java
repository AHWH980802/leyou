package com.leyou.user.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:42 2019/5/21
 */
@Data
@Table(name = "tb_address")
public class Address {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long userId;

    private String name;
    private String state;
    private String city;
    private String district;
    private String address;
    private String phone;
    private String email;
    private String addressAlias;
    @Column(name = "default_address")
    private int defaultAddress;
}
