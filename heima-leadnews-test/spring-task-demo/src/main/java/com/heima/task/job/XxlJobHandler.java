package com.heima.task.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-17 09:30:06
 */
@Component
@Slf4j
public class XxlJobHandler {

    @Value("${server.port}")
    private String port;

    @XxlJob("testJobHandler")
    public void testJobHandler(){
        log.warn("任务执行了--->" + port);
    }


    @XxlJob("demoJobHandler")
    public void demoJobHandler(){
        log.warn("本地xxl-job----任务执行了--->" + port);
    }
}
