package com.snomyc.service.sys;

import com.snomyc.common.base.service.BaseService;
import com.snomyc.service.sys.bean.User;
import com.snomyc.service.sys.request.UserEditRequest;

import java.util.List;
import java.util.Map;

public interface UserService extends BaseService<User, String> {

    public User findUserById(String id);

    List<User> findByIds(String id);

    public void testSave(UserEditRequest request);

    public void testTranSactional(UserEditRequest request);

    public void updateAge(int age, List<String> userNames);

    public List<Map<String,Object>> findByUserName(String userName);
}
