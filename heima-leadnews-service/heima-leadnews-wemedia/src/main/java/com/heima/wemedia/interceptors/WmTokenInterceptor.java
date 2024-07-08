package com.heima.wemedia.interceptors;

import com.heima.wemedia.utils.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-06 11:44:59
 */
@Component
@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求头
        String wmUserIdStr = request.getHeader("wmUserId");
        if(StringUtils.isNotBlank(wmUserIdStr)) {
            //2.放到ThreadLocal中
            try {
                Integer wmUserId = Integer.valueOf(wmUserIdStr);
                WmThreadLocalUtil.setWmUserId(wmUserId);
                log.info("wmUserId:{}", wmUserId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return true;
        }
        //没有用户ID，就直接拦截
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    /**
     * 释放资源
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WmThreadLocalUtil.removeWmUserId();
    }
}
