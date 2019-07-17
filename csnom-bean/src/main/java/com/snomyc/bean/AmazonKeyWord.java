package com.snomyc.bean;

import com.snomyc.bean.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author snomyc
 * 类描述:亚马逊关键词表
 * 创建时间:2018年6月3日 下午3:53:18

 */
@Entity
public class AmazonKeyWord extends BaseEntity {
    
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
    private String keyWordRoot; //关键词根
	
    @Column(unique = true,nullable = false)
    private String keyWordSecond;//关键词根下提示的关键词 以及关键词+空格+(a-z) 所有提示的关键词集合 去除
    
    @Column(nullable = false)
    private Date createTime; //创建时间

	public String getKeyWordRoot() {
		return keyWordRoot;
	}

	public void setKeyWordRoot(String keyWordRoot) {
		this.keyWordRoot = keyWordRoot;
	}

	public String getKeyWordSecond() {
		return keyWordSecond;
	}

	public void setKeyWordSecond(String keyWordSecond) {
		this.keyWordSecond = keyWordSecond;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
