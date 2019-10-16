package com.leyou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:23 2019/5/21
 */
@Table(name = "tb_brand")
public class UserDetail {

    @Id
    private Long id;
    private String nickName;
    private String year;
    private String month;
    private String day;
    private int gender;
    private String job;
    private String city;
}
