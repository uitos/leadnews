package com.heima.rabbitmq.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 15:19:49
 */
@Component
@Slf4j
public class TestConsumerListener {

    @RabbitListener(queues = "test.queue")
    public void handleMessage(Message message){
        byte[] body = message.getBody();
        String msg = new String(body);
        System.out.println(msg);
    }

    /**
     * 监听死信队列的消息
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.ttl.queue"),
            exchange = @Exchange(name = "dl.ttl.direct", type = ExchangeTypes.DIRECT),
            key = "dl"
    ))
    public void handleDeadMessage(String message){
        log.warn("收到消息:" + message);
    }

}