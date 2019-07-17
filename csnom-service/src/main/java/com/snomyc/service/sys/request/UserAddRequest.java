package com.snomyc.service.sys.request;


import com.snomyc.bean.base.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

public class UserAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "userName", value = "用户名", required = true)
	private String userName;
	
	@ApiModelProperty(name = "password", value = "密码", required = true)
	private String password;
	
	@ApiModelProperty(name = "age", value = "年龄", required = true)
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
