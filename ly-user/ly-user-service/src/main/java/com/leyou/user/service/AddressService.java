package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.mapper.AddressMapper;
import com.leyou.user.pojo.Address;
import com.thoughtworks.xstream.io.AbstractDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 11:48 2019/5/21
 */
@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public void insertAddress(Address address,Long userId) {
        address.setUserId(userId);
        addressMapper.insert(address);
    }


    public List<Address> queryAddressByUserId(Long userId) {
        List<Address> addressList = addressMapper.queryAddressByUserId(userId);
        if(CollectionUtils.isEmpty(addressList)){
            throw new LyException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
        return addressList;
    }

    public void updateAddress(Address address) {
        addressMapper.deleteByPrimaryKey(address.getId());
        addressMapper.insert(address);
    }

    public void modifyAddress(Long userId, Long addressId) {
        addressMapper.updateAddress(userId);
        addressMapper.updateBaseAddress(userId,addressId);
    }

    public void delAddress(Long addressId) {
        addressMapper.deleteByPrimaryKey(addressId);
    }

    public Address findById(Long addressId) {
        return addressMapper.selectByPrimaryKey(addressId);
    }
}
