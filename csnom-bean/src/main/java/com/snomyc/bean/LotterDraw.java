package com.snomyc.bean;

import com.snomyc.bean.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LotterDraw extends BaseEntity {
    
	private static final long serialVersionUID = 1L;
	
	@Column(unique = true,nullable = false,length=50)
    private String userName; //用户名
	
	@Column(nullable = false, unique = true,length=50)
    private String mobile; //手机号
	
    private int num;//序号

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
}
