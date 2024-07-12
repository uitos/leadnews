package com.heima.redisson.listeners;

import com.heima.redisson.task.DelayMessageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 09:25:06
 */
@Component
public class DelayMessageListener {

    @Autowired
    private DelayMessageTask delayMessageTask;

    @PostConstruct  //Bean初始化后就立即执行这个方法

    public void run(){
        delayMessageTask.handleMessage();
    }

}
