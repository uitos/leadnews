package com.heima.wemedia.interceptor;

import com.heima.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String wmUserIdStr = request.getHeader("wmUserId");
        log.warn("拦截器获取的id{}" , wmUserIdStr);
        if (StringUtils.isNotBlank(wmUserIdStr)){
            UserContext.setId(Long.valueOf(wmUserIdStr));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeId();
    }
}
