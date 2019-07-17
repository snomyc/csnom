package com.snomyc.service.mq.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;


/**
 * 消息发送到交换机确认机制
 */
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
	private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");
	
    /*关于 msgSendConfirmCallBack：
      1.如果消息没有到exchange,则confirm回调,ack=false
      2.如果消息到达exchange,则confirm回调,ack=true
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
        	logger.info("发送成功: 消息队列消息id=" + correlationData+"");
        } else {
        	logger.info("发送失败: 消息队列消息id=" + correlationData+"\n异常:" + cause);
        }
    }
}
