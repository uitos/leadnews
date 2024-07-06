package com.heima.app.gateway.filter;

import com.heima.app.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-03 16:02:38
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    public static final String LOGIN_PATH = "/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.warn("AppGateway AuthorizeFilter running -----");
        // 1.放行登录请求
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().toString();
        log.warn("请求路径:{}", path);
        if (path.contains(LOGIN_PATH)) {
            log.warn("是登录请求，放行");
            return chain.filter(exchange);
        }
        //释放跨域测试请求
        if (path.contains("/ajax")) {
            log.warn("是跨域测试请求，放行");
            return chain.filter(exchange);
        }

        // 2.判断token是否有效
        boolean flag = true;
        String token = request.getHeaders().getFirst("token");
        if(StringUtils.isBlank(token)) {
            flag = false;
        }
        Claims claims = null;
        try {
            claims = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claims);
            if(result == 1 || result == 2) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        if(flag) {
            if(claims != null) {
                // 3.有效：放行
                Object id = claims.get("id");
                log.warn("token正确，用户ID:{}", id);
                exchange.mutate()
                    .request(builder -> builder.header("appUserId", id.toString()))
                    .build();
            }
            return chain.filter(exchange);
        } else {
            // 4.无效：拦截
            response.setRawStatusCode(HttpStatus.UNAUTHORIZED.value());
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
