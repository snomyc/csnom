package com.snomyc.service.sys.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.snomyc.bean.mongodb.RequestLog;
import com.snomyc.service.sys.RequestLogService;
import com.snomyc.service.sys.dao.RequestLogDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version="1.0")
public class RequestLogServiceImpl implements RequestLogService {

    @Autowired
    private RequestLogDao requestLogDao;

    @Override
    public void saveRequestLog(RequestLog log) {
        requestLogDao.saveRequestLog(log);
    }

    @Override
    public RequestLog findByUserId(String userId) {
        return requestLogDao.findByUserId(userId);
    }

    @Override
    public long updateRequestLog(RequestLog log) {
        return requestLogDao.updateRequestLog(log);
    }

    @Override
    public void deleteById(String id) {
        requestLogDao.deleteById(id);
    }

    @Override
    public List<RequestLog> findAll() {
        return requestLogDao.findAll();
    }

    @Override
    public void deleteAll() {
        requestLogDao.deleteAll();
    }
}


