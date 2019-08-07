package com.snomyc.service.mybatis.sys.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.snomyc.bean.User;
import com.snomyc.service.mybatis.sys.SysMapperService;
import com.snomyc.service.mybatis.sys.mapper.SysMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service(version="1.0")
public class SysMapperServiceImpl implements SysMapperService {

    @Autowired
    private SysMapper sysMapper;

    @Override
    public List<User> findAllUsers() {
        return sysMapper.findAllUsers();
    }

    @Override
    public Map<String, Object> findByUserName(String userName) {
        return sysMapper.findByUserName(userName);
    }

    @Override
    public List<Map<String, Object>> findUsersBySelective(Map<String, Object> paramsMap) {
        return sysMapper.findUsersBySelective(paramsMap);
    }

    @Override
    public List<User> findAllByTempUserId(String userId) {
        return sysMapper.findAllByTempUserId(userId);
    }
}
