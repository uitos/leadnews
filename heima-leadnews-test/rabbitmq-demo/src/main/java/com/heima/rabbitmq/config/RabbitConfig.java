package com.heima.rabbitmq.config;

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
    public Queue testQueue(){
        return new Queue("test.queue");
    }

    @Bean
    public DirectExchange testDirectExchange(){
        return new DirectExchange("test.direct.exchange");
    }

    @Bean
    public Binding testQueueToTestDirectExchange(Queue testQueue, DirectExchange testDirectExchange){
        return BindingBuilder.bind(testQueue)
                .to(testDirectExchange)
                .with("test");
    }

    @Bean
    public Queue simpleQueue(){
        return QueueBuilder
                .durable("simple.queue")
                .deadLetterExchange("dl.ttl.direct")
                .deadLetterRoutingKey("dl")
                .ttl(10000)
                .build();
    }

    @Bean
    public DirectExchange simpleDirectExchange(){
        return new DirectExchange("simple.direct.exchange");
    }

    @Bean
    public Binding simpleQueueToSimpleDirectExchange(Queue simpleQueue, DirectExchange simpleDirectExchange){
        return BindingBuilder.bind(simpleQueue)
                .to(simpleDirectExchange)
                .with("dl");
    }

}
