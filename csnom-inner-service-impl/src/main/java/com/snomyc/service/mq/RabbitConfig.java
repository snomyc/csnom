package com.snomyc.service.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public final static String exchange_amazon = "exchange_amazon";
    public  final static String topic_amazon = "topic.amazon";
    
    //注册交换机
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange_amazon);
    }
    
    @Bean
    public Queue queueAmazon() {
        return new Queue(topic_amazon);
    }
    
    //绑定到交换机
    @Bean
    Binding bindingExchangeAmazon(Queue queueAmazon, TopicExchange exchange) {
        return BindingBuilder.bind(queueAmazon).to(exchange).with(topic_amazon);
    }

}
