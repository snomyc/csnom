package com.ssj.es.entiy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class EsResModel {
	
	private Integer total=0;
	
	private JSONArray list=new JSONArray();
	
	private JSONObject aggregations;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public JSONArray getList() {
		return list;
	}

	public void setList(JSONArray list) {
		this.list = list;
	}

    public JSONObject getAggregations() {
        return aggregations;
    }

    public void setAggregations(JSONObject aggregations) {
        this.aggregations = aggregations;
    }
}
