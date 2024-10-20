package com.heima.wemedia.interceptor;

import com.heima.common.exception.CustomException;
import com.heima.common.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WmTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String wmUserIdStr = request.getHeader("wmUserId");
        if (StringUtils.isNotBlank(wmUserIdStr)){
            UserContext.setId(Long.valueOf(wmUserIdStr));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
