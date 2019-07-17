package com.snomyc.service.mq.receiver;

import com.alibaba.fastjson.JSONObject;
import com.snomyc.bean.User;
import com.snomyc.service.mq.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicRabbitAmazonConsumer2 {
	
	  private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");
	  
	  private static int num=0;
	  
	  @RabbitListener(queues = {RabbitConfig.topic_amazon})
	  @RabbitHandler
	  public void handleMessage(Message message){
		  try {
			  num=num+1;
			  logger.info("消息队列2开始消费 ....");
			  User user = JSONObject.parseObject(message.getBody(),User.class);
			  logger.error("接收2:{}",user.getUserName());
			  logger.error("开始消费推送信息队列2数据："+JSONObject.toJSONString(user));
			  logger.info("消息队列2完成消费..."+num);
		  }catch (Exception e) {
			  throw new RuntimeException("处理消息队列2报错");
		  }
	  }
}


