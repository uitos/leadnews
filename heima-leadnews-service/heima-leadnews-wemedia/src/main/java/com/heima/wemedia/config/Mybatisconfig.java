package com.heima.wemedia.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Mybatis配置类
 * 用于配置Mybatis相关的拦截器
 */
@Configuration
public class Mybatisconfig {

    /**
     * 创建并配置MybatisPlus拦截器
     * 该方法配置了分页拦截器，用于支持分页查询
     *
     * @return 配置好的MybatisPlus拦截器
     */
    @Bean
    public MybatisPlusInterceptor interceptor(){
        // 创建MybatisPlus拦截器实例
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 创建分页拦截器实例
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置分页拦截器的数据库类型为MySQL
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 设置最大单页限制数量为1000条，防止查询过多数据导致内存溢出
        paginationInnerInterceptor.setMaxLimit(1000L);

        // 将分页拦截器添加到MybatisPlus拦截器中
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 返回配置好的MybatisPlus拦截器实例
        return interceptor;
    }
}

