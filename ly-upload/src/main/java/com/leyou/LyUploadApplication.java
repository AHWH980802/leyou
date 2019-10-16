package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 21:16 2019/4/16
 */
@SpringBootApplication
@EnableEurekaClient
public class LyUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyUploadApplication.class,args);
    }
}
