package com.snomyc.service.sys.dao.impl;

import com.snomyc.common.base.core.persistence.PagingHibernateJdbcDao;
import com.snomyc.service.sys.dao.UserQueryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserQueryDaoImpl implements UserQueryDao {

    @Autowired
    private PagingHibernateJdbcDao dao;

    @Override
    public List<Map<String, Object>> findByUserName(String userName) {
        StringBuilder selSQL = new StringBuilder();
        List<Object> queryParams = new ArrayList<Object>();
        selSQL.append("select * from user where user_name = ? ");
        queryParams.add(userName);
        return dao.findMap(selSQL.toString(), queryParams.toArray());
    }
}