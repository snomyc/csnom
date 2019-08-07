package com.snomyc.service.mybatis.sys;

import com.snomyc.bean.User;

import java.util.List;
import java.util.Map;

/**
 * @author snomyc
 * 类描述:mybatis查询服务接口
 * 创建时间:2018年6月3日 下午4:22:56

 */
public interface SysMapperService {

    public List<User> findAllUsers();

    public Map<String,Object> findByUserName(String userName);

    public List<Map<String,Object>> findUsersBySelective(Map<String, Object> paramsMap);

    public List<User> findAllByTempUserId(String userId);
}
