package com.heima.wemedia.interceptor;

import com.heima.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String wmUserId = request.getHeader("wmUserId");
        log.warn("自媒体用户拦截器获取到的用户id为：{}",wmUserId);
        if (StringUtils.isNotBlank(wmUserId)){
            UserContext.setId(Long.valueOf(wmUserId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeId();
    }
}
