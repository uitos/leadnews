package com.heima.wemedia.config;

import com.heima.wemedia.Interceptor.UserConterndIntercrptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserConterndIntercrptor())
                .addPathPatterns("/**");
    }
}
