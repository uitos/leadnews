package com.heima.wemedia.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ghy
 * @version 1.0.1
 * @date 2024-07-12 15:14:06
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue articlePublishQueue(){
        return QueueBuilder
                .durable("article.publish.queue")
                .deadLetterExchange("article.delay.direct")
                .deadLetterRoutingKey("article.delay")
                .build();
    }

    @Bean
    public DirectExchange articlePublishDirectExchange(){
        return new DirectExchange("article.publish.direct.exchange");
    }

    @Bean
    public Binding simpleQueueToSimpleDirectExchange(Queue articlePublishQueue, DirectExchange articlePublishDirectExchange){
        return BindingBuilder.bind(articlePublishQueue)
                .to(articlePublishDirectExchange)
                .with("article.delay");
    }

}
