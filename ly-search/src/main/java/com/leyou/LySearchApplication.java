package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 20:01 2019/4/29
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class  LySearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class);
    }

}
