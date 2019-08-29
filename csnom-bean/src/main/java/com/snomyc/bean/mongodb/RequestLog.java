package com.snomyc.bean.mongodb;

import java.io.Serializable;
import java.util.Date;

public class RequestLog implements Serializable {

	private String id;//id
	private String phoneimei;//手机串号
	private String phonesys;//手机系统
	private String phonetype;//机型
	private String version;//客户端线上版本号
	private String platform;//平台 1:IOS 2:Android 3:小程序 4 小塾
	private String build;//客户端开发版本号
	private String apiUrl;//接口URL
	private String methodName;//请求的方法
	private String ipAddress;//IP地址
	private String accessToken;//访问令牌
	private Date requestDate;//请求时间
	private String userId;
	private String requestMap;//所有的请求参数
	private String postBody;//post请求的body内容

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneimei() {
		return phoneimei;
	}

	public void setPhoneimei(String phoneimei) {
		this.phoneimei = phoneimei;
	}

	public String getPhonesys() {
		return phonesys;
	}

	public void setPhonesys(String phonesys) {
		this.phonesys = phonesys;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(String requestMap) {
		this.requestMap = requestMap;
	}

	public String getPostBody() {
		return postBody;
	}

	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}
}
