package com.heima.wemedia.interceptor;

import com.heima.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自媒体作证拦截器
 * @author ghy
 * @version 1.0.1
 * @date 2024-10-20 14:58:58
 */
@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String wmUserIdStr = request.getHeader("wmUserId");
        log.warn("自媒体拦截器获取到的用户ID:{}", wmUserIdStr);
        if(StringUtils.isNotBlank(wmUserIdStr)){
            UserContext.setId(Long.valueOf(wmUserIdStr));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeId();
    }
}
