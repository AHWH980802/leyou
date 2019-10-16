package com.leyou.user.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.user.pojo.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:46 2019/5/21
 */
public interface AddressMapper extends BaseMapper<Address> {

    @Select("SELECT id,user_id,name,state,city,district,address,phone,email,address_alias,default_address FROM tb_address WHERE user_id = #{userId}")
    List<Address> queryAddressByUserId(@Param("userId") Long userId);

    @Update("UPDATE tb_address SET default_address = 0 WHERE user_id = #{userId}")
    void updateAddress(@Param("userId") Long userId);

    @Update("UPDATE tb_address SET default_address = 1 WHERE user_id = #{userId} AND id = #{addressId}")
    void updateBaseAddress(@Param("userId") Long userId,@Param("addressId") Long addressId);
}
