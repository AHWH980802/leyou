package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author: 98050
 * @Time: 2018-10-21 18:42
 * @Feature: 用户实体类
 */
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 4,max = 32,message = "用户名长度必须在4-32位")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4,max = 32,message = "密码长度必须在4-32位")
    private String password;

    /**
     * 电话
     */
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$",message = "手机号不正确")
    private String phone;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 密码盐
     */
    @JsonIgnore
    private String salt;
}
