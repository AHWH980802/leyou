package com.leyou.filters;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 19:40 2019/5/15
 */
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    private JwtProperties prop;

    @Override
    public String filterType() {
        //  过滤器类型 前置过滤
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 是否过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //  获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //  获取request
        HttpServletRequest request = ctx.getRequest();
        //  获取请求的url路径
        String path = request.getRequestURI();
        //  判断是否放行，放行返回false
        return !isAllowPath(path);
    }

    private boolean isAllowPath(String path) {
        //  遍历白名单
        for (String allowPath : filterProperties.getAllowPaths()) {
            //  判断是否是允许的
            if(path.startsWith(allowPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //  获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //  获取request
        HttpServletRequest request = ctx.getRequest();
        //  获取token
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");

        try {
            //  解析token
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //  TODO 校验权限

        } catch (Exception e) {
            //  解析token失败 未登录
            ctx.setSendZuulResponse(false);
            //  返回状态码
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}
