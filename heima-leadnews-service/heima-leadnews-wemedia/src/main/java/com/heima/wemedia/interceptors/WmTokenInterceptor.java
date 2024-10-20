package com.heima.wemedia.interceptors;


import com.heima.common.constants.WmConstants;
import com.heima.wemedia.utils.WmUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义Wm拦截器
 */
@Component
@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取通过网关更新后请求头中的“userId”
        String userId = request.getHeader(WmConstants.WM_USER_HEARD);
        //先判断获取的userId是否为空
        if (StringUtils.isNotBlank(userId)) {
            //将userId设置到ThreadLocal中 方便后续使用
            log.info("Wm自定义拦截器获取到的userId为:{}", userId);
            WmUserContext.setUser(Long.valueOf(userId));
        } else {
            log.info("可能为登录,Wm自定义拦截器获取到的userId为空");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户
        WmUserContext.removeUser();
    }
}
