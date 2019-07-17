package com.snomyc.service.mq.producer;

import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.UUID;

public class BaseRabbitSend {
	

    /**
     * topic 是 RabbitMQ 中最灵活的一种方式，可以根据 routing_key 自由的绑定不同的队列
     * @param queueName
     * @param data
     */
    public static String convertAndTopicSend(String exchange, String routingKey, final Object object,BaseRabbitTemplate rabbitMqConfig){
    	 String uuid = UUID.randomUUID().toString();
         CorrelationData correlationId = new CorrelationData(uuid);
         rabbitMqConfig.rabbitTemplate().convertAndSend(exchange,routingKey, object, correlationId);
         return uuid;
    }

	
    /**
     * Fanout 就是我们熟悉的广播模式或者订阅模式
     * @param queueName
     * @param data
     */
//    public static String convertAndFanoutSend(final Object object,BaseRabbitTemplate rabbitMqConfig){
//    	  String uuid = UUID.randomUUID().toString();
//          CorrelationData correlationId = new CorrelationData(uuid);
//          rabbitMqConfig.rabbitTemplate().convertAndSend(FanoutRabbitConfig.exchange_ssj, object, correlationId);
//          return uuid;
//    	
//    }
    

    
    /**
     * 一个发送者，N 个接收者或者 N 个发送者和 N 个接收者
     * oneToMany
     * @param queueName
     * @param data
     */
//    public static String convertAndNeoSend(String fanoutName, Object object,BaseRabbitTemplate rabbitMqConfig){
//    	String uuid = UUID.randomUUID().toString();
//        CorrelationData correlationId = new CorrelationData(uuid);
//        rabbitMqConfig.rabbitTemplate().convertAndSend(fanoutName, object, correlationId);
//        return uuid;
//    }


}
