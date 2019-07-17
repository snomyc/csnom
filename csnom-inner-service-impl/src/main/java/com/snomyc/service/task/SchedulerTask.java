package com.snomyc.service.task;

import com.alibaba.fastjson.JSONObject;
import com.snomyc.bean.User;
import com.snomyc.service.mq.RabbitConfig;
import com.snomyc.service.mq.producer.BaseRabbitSend;
import com.snomyc.service.mq.producer.BaseRabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class SchedulerTask {
	
	private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");
	
//	@Autowired
//    private AmqpTemplate rabbitTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private BaseRabbitTemplate rabbitMqConfig;
	
	private int count=0;

    @Scheduled(cron="0 */6 * * * ?")
    private void process(){
    	User user = new User();
    	user.setUserName("你是大傻逼! 加"+(count++));
    	logger.error(JSONObject.toJSONString(user));
        BaseRabbitSend.convertAndTopicSend(RabbitConfig.exchange_amazon, RabbitConfig.topic_amazon, user, rabbitMqConfig);
    }
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 
    @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
    @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
    @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次
     * 方法说明:
    
     * 创立日期:2018年6月1日 下午5:38:02
     * 创建人:yangcan
    
     */
    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        User user = new User();
    	user.setUserName("你是大傻逼! 加加"+(count++));
    	logger.error(JSONObject.toJSONString(user));
        this.rabbitTemplate.convertAndSend(RabbitConfig.exchange_amazon, RabbitConfig.topic_amazon, user);
    }
}
