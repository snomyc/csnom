package com.snomyc.common.base.security.manager;

import java.util.Map;

/**
 * 
 * 类名称：TokenManager<br>
 * 类描述：令牌管理器<br>
 * 创建时间：2015年10月27日 上午11:05:22<br>
 * @version v1.0
 *
 */
public interface TokenManager {
	/**
	 * 获取实体
	* @param key
	* @param clazz
	* 2017年12月1日 上午10:26:29 
	* @T
	 */
	 public  <T> T get(String key, Class<T> clazz);
	 /**
	  * 存放实体
	 * @param key
	 * @param value
	 * @param expireSeconds
	 * 2017年12月1日 上午10:26:36 
	 * @String
	  */
	 public String set(String key, Object value, int expireSeconds);
	 public String set(String key, Object value);
	   public void hset(String key, Map<String, String> value);
	   public void hset(String key, Map<String, String> value, int expireSeconds) ;
	   public Map<String, String> hget(String key) ;
	   /**
	    * 存放String类型
	    */
	   public String setString(String key, String value, int expireSeconds);
	   public String setString(String key, String value);
	   public void delString(String key);
	   public String getString(String key);

    public String createToken(String token, String userId);
    public String createToken(String token, String userId, String openId, int source, String sessionKey);
    public String createToken(String token, String userId, String openId, int source, String sessionKey, int seconds);

    /**
     * 移除令牌
     * @param token
     */
    public void removeToken(String token);
    /**
     * 移除令牌
     * @param token
     */
    public void removeToken(String token, String replaceUserId, String replaceOpenId);

    /**
     * 检查令牌
     * @param token
     * @return
     */
    public boolean checkToken(String token);

    /**
     * 检查令牌
     * @param token
     * @param userId
     * @return
     */
    public boolean checkToken(String token, String userId);

    /**
     * 通过token获取用户id
     * @param token
     * @return
     */
    public String getUserId(String token);
    /**
     * 通过用户id，获取token
     * @param token
     * @return
     */
    public String getToken(String userId);
    
    /**
     * 通过应用id、用户id获取对应的token
     * @param openId 应用id
     * @param userId 用户id
     * @return
     */
    public String getToken(String openId, String userId);
    /**
     * 通过token获取用户openid
     * @param token
     * @return
     */
    public String getOpenId(String token);
    
    
    /**
     * 通过token获取用户sessionKey
     * @param token
     * @return
     */
    public String getSessionKey(String token);

    
    public void delToken(String userId);
    
    
    /**
     *  redis保存极光ID，token失效就删除别名。
    * @param alias
    * @return
    * 创建人：ZhangCaibao 
    * 2018年4月16日 下午2:26:52 
    * @String
     */
    public String setJpushAlias(String userId, String alias);
    /**
     * reids存放信鸽
    * @param derver
    * @param xinge
    * @return
    * 创建人：ZhangCaibao 
    * 2018年4月25日 上午10:27:48 
    * @String
     */
    public String setXinge(String derver, String xinge);
    
    public String getJpushAlias(String userId);
    
    public String getXinge(String derver);
    
}
