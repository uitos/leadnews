package com.heima.redisson.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 09:15:19
 */
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发布一个任务
     * @param time 延迟时间，单位为秒
     * @return
     */
    @GetMapping("/sendMsg/{time}")
    public String sendMsg(@PathVariable(name = "time") Integer time){
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque("test-deque");
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer("This is a Delay Message!!", time, TimeUnit.SECONDS);
        //返回一个发布任务成功的时间
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
