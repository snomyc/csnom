package com.ssj.es.entiy;


import java.util.Map;


/**
 * @ClassName ElasticEntity
 * @Description  数据存储对象
 * @Version 1.0.0
*/

@SuppressWarnings("rawtypes")
public class ElasticEntity {

    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
	private Map data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}
    
    
    
}
