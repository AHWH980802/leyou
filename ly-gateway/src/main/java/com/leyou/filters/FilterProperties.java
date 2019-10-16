package com.leyou.filters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 20:07 2019/5/15
 */
@Data
@ConfigurationProperties(prefix = "ly.filter")
public class FilterProperties {
    private List<String> allowPaths;
}
