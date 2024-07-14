package com.heima.kafka.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-14 14:37:01
 */
@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "itheima-topic")
    public void handleMessage(String message){
        if(!StringUtils.isEmpty(message)) {
            System.out.println(message);
        }
    }
}
