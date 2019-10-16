package com.leyou.order.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 16:16 2019/5/15
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String pubKeyPath;

    private PublicKey publicKey;    //公钥

    //  对象一旦实例化后，就应该读取公钥和私钥
    //  构造函数执行之后执行
    @PostConstruct
    public void init() throws Exception {
        //  读取公钥私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

    }
}
