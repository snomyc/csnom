package com.snomyc.bean.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class BaseRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        return JSON.toJSONString(this);
    }

}
