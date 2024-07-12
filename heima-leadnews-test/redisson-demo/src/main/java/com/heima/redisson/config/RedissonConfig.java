package com.heima.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        // 创建配置对象
        Config config = new Config();
        // 配置单节点Redis服务器
        config.useSingleServer()
                .setAddress("redis://192.168.200.130:6379")
                .setPassword("leadnews");

        // 创建Redisson客户端实例
        return Redisson.create(config);
    }

}
