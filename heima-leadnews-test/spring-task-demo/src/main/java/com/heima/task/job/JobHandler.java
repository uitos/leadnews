package com.heima.task.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-17 09:00:53
 */
@Component
@Slf4j
public class JobHandler {

    /**
     * 每2秒执行一次
     */
    //@Scheduled(cron = "0/2 * * * * ?")
    public void task(){
        log.warn("任务开始执行了---->");
    }

}
