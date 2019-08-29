package com.snomyc.service.sys.dao.impl;

import com.mongodb.WriteResult;
import com.snomyc.bean.mongodb.RequestLog;
import com.snomyc.service.sys.dao.RequestLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RequestLogDaoImpl implements RequestLogDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveRequestLog(RequestLog log) {
        mongoTemplate.save(log);
    }

    @Override
    public RequestLog findByUserId(String userId) {
        Query query=new Query(Criteria.where("userId").is(userId));
        RequestLog log =  mongoTemplate.findOne(query , RequestLog.class);
        return log;
    }

    @Override
    public long updateRequestLog(RequestLog log) {
        Query query = new Query(Criteria.where("id").is(log.getId()));
        Update update = new Update().set("requestDate", new Date()).set("postBody", log.getPostBody());
        //更新查询返回结果集的第一条
        WriteResult result = mongoTemplate.updateFirst(query,update,RequestLog.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,RequestLog.class);
        if(result!=null)
            return result.getN();
        else
            return 0;
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,RequestLog.class);
    }

    @Override
    public List<RequestLog> findAll() {
        Query query = new Query();
        //mongoDB分页查询下标为1开始总共1000行数据
        query.skip(100).limit(1000);
        List<RequestLog> logList =  mongoTemplate.find(query,RequestLog.class);
        return logList;
    }

    @Override
    public void deleteAll() {
        //eq相等   ne、neq不相等，   gt大于， lt小于 gte、ge大于等于   lte、le 小于等于   not非   mod求模
//        Query query = new Query(Criteria.where("requestDate").lt(new Date()));
//        mongoTemplate.remove(query,RequestLog.class);
        Query query = new Query();
        //mongoDB分页查询下标为1开始总共1000行数据
        query.skip(100).limit(1000);
        mongoTemplate.remove(query,RequestLog.class);
    }
}