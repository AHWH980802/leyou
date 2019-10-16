package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 8:39 2019/5/18
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {

    private long workerId;

    private long dataCenterId;
}
