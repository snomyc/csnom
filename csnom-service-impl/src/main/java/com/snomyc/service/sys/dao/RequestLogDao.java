package com.snomyc.service.sys.dao;

import com.snomyc.bean.mongodb.RequestLog;

import java.util.List;

public interface RequestLogDao {
    public void saveRequestLog(RequestLog log);

    public RequestLog findByUserId(String userId);

    public long updateRequestLog(RequestLog log);

    public void deleteById(String id);

    public List<RequestLog> findAll();

    public void deleteAll();
}