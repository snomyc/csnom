package com.snomyc.common.base.core.persistence;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 类名称：SearchFilter<br>
 * 类描述：对查询参数的封装<br>
 * @version v1.0
 *
 */
public class SearchFilter {

    public enum Operator {
    EQ, LIKE, GT, LT, GTE, LTE, IN,NOT
    }

    public String fieldName; // 参数名称
    public Object value; // 参数值
    public Operator operator; // 操作符

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value ==null || value =="" || "".equals(value)) {
            	continue;
            }

            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length != 2) { throw new IllegalArgumentException(key + " is not a valid search filter name"); }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);

            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, operator, parseValue(filedName, value));
            filters.put(key, filter);
        }

        return filters;
    }

    /**
     * searchParams中过滤时间，时间是根据filedName的名字来判断的
     */
    public static Object parseValue(String filedName, Object value) {
        try {
            if (filedName.toLowerCase().endsWith("date")) {
                return DateUtils.parseDate((String) value, new String[] { "yyyy-MM-dd" });
            } else if (filedName.toLowerCase().endsWith("time")) { return DateUtils.parseDate((String) value, new String[] { "yyyy-MM-dd HH:mm:ss" }); }
        } catch (ParseException e) {
            throw new RuntimeException("日期格式不正确", e);
        }
        return value;
    }

}
