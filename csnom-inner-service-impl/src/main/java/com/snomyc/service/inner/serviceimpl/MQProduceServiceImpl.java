package com.snomyc.service.inner.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.snomyc.bean.User;
import com.snomyc.service.inner.MQProduceService;
import com.snomyc.service.mq.RabbitConfig;
import com.snomyc.service.mq.producer.BaseRabbitSend;
import com.snomyc.service.mq.producer.BaseRabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yangcan
 * 类描述:消息服务实现类
 * 创建时间:2019/7/17 17:50
 */
@Service(version="1.0")
public class MQProduceServiceImpl implements MQProduceService {
    private Logger logger = LoggerFactory.getLogger("AmazonRabbitmq");

    @Autowired
    private BaseRabbitTemplate rabbitMqConfig;

    @Override
    public void sendMessage() {
        User user = new User();
        user.setUserName("测试消息推送");
        logger.error(JSONObject.toJSONString(user));
        BaseRabbitSend.convertAndTopicSend(RabbitConfig.exchange_amazon, RabbitConfig.topic_amazon, user, rabbitMqConfig);
    }
}
