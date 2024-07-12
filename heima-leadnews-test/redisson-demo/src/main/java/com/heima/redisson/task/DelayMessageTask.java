package com.heima.redisson.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 09:20:32
 */
@Component
@Slf4j
public class DelayMessageTask {

    @Autowired
    private RedissonClient redissonClient;
    @Async
    public void handleMessage(){
        log.warn("准备开始从test-deque延迟队列中获取任务");
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque("test-deque");
        while (true) {
            try {
                //从队列中获取任务，如果拿到，直接返回。如果没有，则等10秒，如果还没有，本次拿任务中断
                String message = blockingDeque.poll(10, TimeUnit.SECONDS);
                if(message != null){
                    log.warn("拿到test-deque中的任务:{}",message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("获取任务失败:{}",e.getMessage());
            }
        }
    }

}
