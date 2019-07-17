package com.snomyc.service.sys.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.snomyc.bean.User;
import com.snomyc.common.base.service.BaseServiceImpl;
import com.snomyc.service.sys.UserService;
import com.snomyc.service.sys.dao.UserDao;
import com.snomyc.service.sys.dao.UserQueryDao;
import com.snomyc.service.sys.request.UserEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(version="1.0")
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserQueryDao userQueryDao;

    @Override
    public User findUserById(String id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findByIds(String id) {
        return userDao.findByIds(id);
    }

    @Override
    @Transactional
    public void testSave(UserEditRequest request) {
        //在事物下查询出来的实体是持久化状态，更改属性会自动保存到数据库
        User user = this.getById(request.getId());
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setAge(request.getAge());
//        List<User> userList = this.findByIds(request.getId());
//        for (User user:userList) {
//            user.setUserName(request.getUserName());
//            user.setPassword(request.getPassword());
//            user.setAge(request.getAge());
//        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testTranSactional(UserEditRequest request) {
        //Transactional注解规则，如果在主方法头上设置规则则主方法里的所有子方法设置的Transactional规则失效
        //在方法上加Transactional规则，在类头上也加Transactional规则，则以方法上的为准
        this.testSave(request);
    }

    @Override
    @Transactional
    public void updateAge(int age, List<String> userNames) {
        userDao.updateAge(age,userNames);
    }

    @Override
    public List<Map<String, Object>> findByUserName(String userName) {
        return userQueryDao.findByUserName(userName);
    }

    @Override
	public PagingAndSortingRepository<User, String> getDao() {
		return userDao;
	}

}


