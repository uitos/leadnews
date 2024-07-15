package com.heima.search.interceptors;

import com.heima.search.utils.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class AppTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求头
        String appUserIdStr = request.getHeader("appUserId");
        if(StringUtils.isNotBlank(appUserIdStr)) {
            //2.放到ThreadLocal中
            try {
                Integer appUserId = Integer.valueOf(appUserIdStr);
                AppThreadLocalUtil.setAppUserId(appUserId);
                log.info("appUserId:{}", appUserId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return true;
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
        AppThreadLocalUtil.removeAppUserId();
    }
}
