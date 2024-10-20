package com.heima.wemedia.config;

import com.heima.wemedia.interceptors.WmTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
//@ConditionalOnClass(DispatcherServlet.class)//提供spring mvc的配置环境
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private WmTokenInterceptor wmTokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有请求 , 通过自定义的拦截器获取请求头中的用户信息
        registry.addInterceptor(wmTokenInterceptor)
                .addPathPatterns("/**");
    }
}
