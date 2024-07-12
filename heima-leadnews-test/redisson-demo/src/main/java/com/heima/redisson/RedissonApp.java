package com.heima.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedissonApp {
    public static void main(String[] args) {
        SpringApplication.run(RedissonApp.class, args);
    }
}
