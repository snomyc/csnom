package com.snomyc.bean;

import com.snomyc.bean.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class User extends BaseEntity {
    
	private static final long serialVersionUID = 1L;
	
	@Column(unique = true,nullable = false,length=50)
    private String userName;
	
	@Column(nullable = false, unique = true,length=50)
    private String password;
	
    private int age;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
