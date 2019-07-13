package com.snomyc.service.sys.bean;

import com.snomyc.common.base.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author snomyc
 * 类描述:
 * 创建时间:2018年6月3日 下午3:53:18

 */
@Entity
public class SysConfig extends BaseEntity {
    
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, unique = true,length=36)
    private String paramCode; //参数编号
	
    @Column(nullable = false)
    private String paramVal;//参数值
    
    @Column(nullable = false)
    private String paramDesc;//参数说明

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public String getParamVal() {
		return paramVal;
	}

	public void setParamVal(String paramVal) {
		this.paramVal = paramVal;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

}
