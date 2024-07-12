package com.heima.rabbitmq.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 15:21:26
 */
@SpringBootTest
@Slf4j
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMsg(){
        //rabbitTemplate.convertAndSend("test.direct.exchange", "test","Haha!!");
        //rabbitTemplate.convertAndSend("simple.direct.exchange", "dl","Haha22!!");
        Message message = MessageBuilder
                .withBody("Haha33!!".getBytes(StandardCharsets.UTF_8))
                .setExpiration("3000")
                .build();
        rabbitTemplate.convertAndSend("simple.direct.exchange", "dl",message);
        log.warn("消息发送成功");
    }


}
