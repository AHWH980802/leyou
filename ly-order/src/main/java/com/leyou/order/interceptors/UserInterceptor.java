package com.leyou.order.interceptors;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 8:34 2019/5/17
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //  获取cookie
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        //  解析token
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //  传递user
            //request.setAttribute("user",user);
            tl.set(user);
            //  放行
            return true;
        } catch (Exception e) {
            log.error("[购物车服务]  解析用户身份失败，",e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //  用完数据一定要清空
        tl.remove();
    }

    public static UserInfo gerUser(){
        return tl.get();
    }

}
