package com.heima.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   //开启定时任务
public class TaskApp {
    public static void main(String[] args) {
        SpringApplication.run(TaskApp.class, args);
    }
}
