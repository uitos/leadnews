package com.heima.wemedia.interceptor;

import com.heima.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author mianbao
 * 1
 */
@Slf4j
public class WmInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("wmUserId");
        if (Objects.nonNull(userId)) {
            UserContext.setId(Long.valueOf(userId));
        }
        log.info("userID={}", userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        UserContext.remove();

    }
}
