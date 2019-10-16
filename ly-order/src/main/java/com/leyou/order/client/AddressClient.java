package com.leyou.order.client;

import com.leyou.order.dto.AddressDTO;
import com.leyou.user.api.AddressApi;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.ArrayList;
import java.util.List;

@FeignClient("user-service")
public interface AddressClient extends AddressApi {

}

