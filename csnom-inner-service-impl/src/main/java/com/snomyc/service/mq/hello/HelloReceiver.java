package com.snomyc.service.mq.hello;

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
public class HelloReceiver {
	private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");
	
	@RabbitListener(queues = { RabbitConfig.topic_amazon })
	@RabbitHandler
	public void handleMessage(Message message) {
		try {
			logger.info("消息队列3开始消费 ....");
			User user = JSONObject.parseObject(message.getBody(), User.class);
			logger.error("接收3:{}",user.getUserName());
			logger.error("开始消费推送信息队列3数据：" + JSONObject.toJSONString(user));
		} catch (Exception e) {
			throw new RuntimeException("处理消息队列3报错");
		}
	}

}
