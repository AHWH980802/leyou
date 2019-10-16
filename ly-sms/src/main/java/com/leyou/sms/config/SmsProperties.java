package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 17:34 2019/5/14
 */
@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {

    String ACCOUNT_SID;

    String AUTH_TOKEN;
}
