package com.snomyc.service.sys.dao;

import java.util.List;
import java.util.Map;

public interface UserQueryDao{
    public List<Map<String,Object>> findByUserName(String userName);
}