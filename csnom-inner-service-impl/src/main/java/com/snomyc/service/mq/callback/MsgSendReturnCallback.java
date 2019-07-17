package com.snomyc.service.mq.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 */
public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback {
	
	private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");
	 /*  关于  msgSendReturnCallback 的回调说明：
        1.exchange到queue成功,则不回调return
        2.exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
        发送不成功会回调此方法，后期再对发送失败的消息保存再发送的处理
    */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    	logger.info("exchange到queue失败："+message);
    }
}
