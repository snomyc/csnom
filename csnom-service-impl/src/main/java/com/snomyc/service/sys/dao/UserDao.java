package com.snomyc.service.sys.dao;

import com.snomyc.service.sys.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    User findById(String id);

    @Query(value="select * from user where id = ?1", nativeQuery = true)
    List<User> findByIds(String id);

    @Query(value="update user set age = ?1 where user_name in(?2) ", nativeQuery = true)
    @Modifying
    public void updateAge(int age, List<String> userNames);
}