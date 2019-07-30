package com.snomyc.service.mybatis.sys.mapper;

import com.snomyc.bean.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/***
 * mybatis中#和$符号的区别
 * #可以防止sql注入 $传入什么就是什么，先拼接再执行sql
 */
@Mapper
public interface SysMapper {
    public List<User> findAllUsers();
    public Map<String,Object> findByUserName(String userName);
    public List<Map<String, Object>> findUsersBySelective(Map<String, Object> paramsMap);
}
