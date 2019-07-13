package com.snomyc.common.base.security.manager.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.alibaba.fastjson.JSON;
import com.snomyc.common.base.security.manager.TokenManager;
import com.snomyc.common.util.CodecUtil;

/**
 * 
 * 类名称：RedisTokenManager<br>
 * 类描述：基于 Redis 的令牌管理器<br>
 * @version v1.0
 *
 */
@Service
public class RedisTokenManager implements TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisTokenManager.class);

    /**
     * Redis中key是userId  value 是token
     */
    private static final String REDIS_TOKEN_TOKEN_PREFIX = "TOKEN_TOKEN_";

    /**
     * Redis中key是token  value 是      userId 
     */
    private static final String REDIS_TOKEN_USERID_PREFIX = "TOKEN_USERID_";
    
    /**
     * Redis中key是token value 是       openId
     */
    private static final String REDIS_TOKEN_OPENID_PREFIX = "TOKEN_OPENID_";
    /**
     * Redis中key是token value 是       sessionkey
     */
    private static final String REDIS_TOKEN_SESSION_KEY = "TOKEN_SESSION_Key_";
    
    /**
     * token失效时间，默认7天
     */
    private static final int DEFAULT_SECONDS = 7 * 24 * 3600;
    
    /**
     * 极光推送ID的前缀
     */
    private static final String REDIS_JPUSH = "JPUSH_";
    
    private static final String REDIS_XINGE = "XINGE_";
    /**
     * 授权公众号授权方令牌
     */
    private static final String AUTHORIZER_ACCESS_TOKEN = "authorizer_access_token";
    /**
     * 授权公众号授权方的刷新令牌
     */
    private static final String AUTHORIZER_REFRESH_TOKEN = "authorizer_refresh_token";
    /**
     * 授权公众号授权方的JS-TOKEN
     */
    private static final String AUTHORIZER_JS_TOKEN = "authorizer_js_token";
    
    /**
     * 私塾家token失效时间，默认2小时
     */
    private static final int SSJ_SECONDS = 7000;

    private JedisPool jedisPool;
    private int seconds = DEFAULT_SECONDS;
    private int ssjseconds = SSJ_SECONDS;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String createToken(String token,String userId) {
    	if(token==null )token=CodecUtil.createUUID();
    	String oldToken = get(formatTokenToken(userId));
    	if (oldToken != null) {
    		removeToken(oldToken);
    	}
    	set(formatTokenUserId(token), userId, seconds);
    	set(formatTokenToken(userId), token, seconds);
    	
    	return token;
    }
    
    @Override
    public String createToken(String token,String userId, String openId, int source,String sessionKey) {
    	if(token==null )token=CodecUtil.createUUID();
    	String oldToken = get(formatTokenToken(userId + "_" + openId));
    	if (oldToken != null) {
    		logger.info("oldToken不为空，删除oldToken");
    		removeToken(oldToken,userId,openId);
    	}
    	set(formatTokenUserId(token), userId, seconds);
    	set(formatTokenToken(userId + "_" + openId), token, seconds);
    	set(formatTokenOpenid(token),openId, seconds);
    	set(formatTokenSessionKey(token),sessionKey, seconds);
    	return token;
    }
    
    @Override
    public String createToken(String token,String userId, String openId, int source,String sessionKey,int seconds) {
    	if(token==null )token=CodecUtil.createUUID();
    	String oldToken = get(formatTokenToken(userId + "_" + openId));
    	if (oldToken != null) {
    		logger.info("oldToken不为空，删除oldToken");
    		removeToken(oldToken,userId,openId);
    	}
    	set(formatTokenUserId(token), userId, seconds);
    	set(formatTokenToken(userId + "_" + openId), token, seconds);
    	if(source==4){
    		set(formatTokenToken(userId + "_" + userId), token, seconds);//单独存放是为了后台了删除
    	}
    	set(formatTokenOpenid(token),openId, seconds);
    	set(formatTokenSessionKey(token),sessionKey, seconds);
    	return token;
    }

    @Override
    public void removeToken(String token,String userId,String openId){
        delete(formatTokenUserId(token));
        delete(formatTokenToken(userId + "_" + openId));
        delete(formatTokenOpenid(token));
        delete(formatTokenSessionKey(token));
    }
    @Override
    public void delToken(String userId){
    	  String token=get(formatTokenToken(userId + "_" + userId));
    	  if(token!=null){
    		  delete(formatTokenUserId(token));
    		  delete(formatTokenOpenid(token));
    	      delete(formatTokenSessionKey(token));
    	      delete(formatTokenToken(userId + "_" + userId));
    	  }
    	  
    }
    
    @Override
    public void removeToken(String token) {
    	String replaceUserId =get(formatTokenUserId(token));
    	delete(formatTokenUserId(token));
    	if(replaceUserId!=null){
    		delete(formatTokenToken(replaceUserId));
    	}
    }

    @Override
    public boolean checkToken(String token) {
    	logger.info("token))))"+token);
        boolean result = exists(formatTokenUserId(token));
        if (result) {
            expire(formatTokenUserId(token), seconds);
        }
        return result;
    }

    @Override
    public boolean checkToken(String token, String userId) {
        boolean result = exists(formatTokenUserId(token));

        if (!userId.equals(get(formatTokenUserId(token)))) {
            result = false;
        } else {
            expire(formatTokenUserId(token), seconds);
        }

        return result;
    }

    public String getUserId(String token) {
        return get(formatTokenUserId(token));
    }
    public String getOpenId(String token) {
    	return get(formatTokenOpenid(token));
    }
    public String getSessionKey(String token) {
    	return get(formatTokenSessionKey(token));
    }

    /*
     * (non-Javadoc)
     
    public AccessToken getTokenInfo(String token) {
        return accessTokenService.findByAccessToken(token);
    }
    */
    /*
     * (non-Javadoc)
     */
    public String getToken(String openId, String userId) {
        return get(formatTokenToken(userId + "_" + openId));
    }
    public String getToken(String userId) {
    	return get(formatTokenToken(userId));
    }

    private String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    private String set(String key, String value, int expireSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, expireSeconds, value);
        }
    }

    private boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    private void expire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, seconds);
        }
    }

    private void delete(String... keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(keys);
        }
    }

    public  <T> T get(String key,Class<T> clazz){  
    	try (Jedis jedis = jedisPool.getResource()) {
    		 String value = jedis.get(key); 
    		 if(value!=null){
    			 return JSON.parseObject(value, clazz); 
    		 }
    		 return null;
        }
    }
    
    public String set(String key, Object  value, int expireSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
        	 String objectJson = JSON.toJSONString(value); 
            return jedis.setex(key, expireSeconds, objectJson);
        }
    }
    public String set(String key, Object  value) {
    	try (Jedis jedis = jedisPool.getResource()) {
    		String objectJson = JSON.toJSONString(value); 
    		return jedis.setex(key, DEFAULT_SECONDS, objectJson);
    	}
    }
    @Override
	public void hset(String key, Map<String, String> value) {
		try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, value);
            jedis.expire(key, DEFAULT_SECONDS);
        }
	}

	@Override
	public void hset(String key, Map<String, String> value, int expireSeconds) {
		try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, value);
            jedis.expire(key, expireSeconds);
        }
	}

	@Override
	public Map<String, String> hget(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(key);
            return map;
        }
	}

	@Override
	public String setString(String key, String value, int expireSeconds) {
		return set(key, value, expireSeconds);
	}

	@Override
	public String setString(String key, String value) {
		return set(key, value,DEFAULT_SECONDS);
	}
	@Override
	public void delString(String key) {
		delete(key);
	}
	@Override
	public String getString(String key) {
		return get(key);
	}
    
    private String formatTokenToken(String key) {
        return REDIS_TOKEN_TOKEN_PREFIX.concat(key);
    }

    private String formatTokenUserId(String userId) {
        return REDIS_TOKEN_USERID_PREFIX.concat(userId);
    }
    
    private String formatTokenOpenid(String openid) {
        return REDIS_TOKEN_OPENID_PREFIX.concat(openid);
    }
    private String formatTokenSessionKey(String sessionKey) {
    	return REDIS_TOKEN_SESSION_KEY.concat(sessionKey);
    }
    private String formatJpushKey(String key) {
    	return REDIS_JPUSH.concat(key);
    }
    private String formatXingeKey(String key) {
    	return REDIS_XINGE.concat(key);
    }

	private String authorizerAccessFormatToken(String authorizerAppid) {
        return AUTHORIZER_ACCESS_TOKEN.concat(authorizerAppid);
    }
	
	private String authorizerRefreshFormatToken(String authorizerAppid) {
		return AUTHORIZER_REFRESH_TOKEN.concat(authorizerAppid);
	}

	private String authorizerJSFormatToken(String authorizerAppid) {
		return AUTHORIZER_JS_TOKEN.concat(authorizerAppid);
	}
	
	public String createWeather(String libValue,String libKey,int times){
		times = times >0 ? times:ssjseconds;
		set(libKey, libValue, times);
		return libValue;
	}

	public String createGrWord(String wordKey,String wordValue,int times){
		times = times >0 ? times:ssjseconds;
		set(wordKey, wordValue, times);
		return wordValue;
	}

	 @Override
	 public String setJpushAlias(String userId,String alias) {
		String oldAlias = get(formatJpushKey(userId));
    	if (oldAlias != null) {
    		 delete(formatJpushKey(userId));
    	}
    	set(formatJpushKey(userId), alias, 365 * 24 * 3600);
		return alias;
	}

	@Override
	public String getJpushAlias(String userId) {
		return get(formatJpushKey(userId));
	}

	@Override
	public String setXinge(String derver, String xinge) {
		String oldAlias = get(formatXingeKey(derver));
    	if (oldAlias != null) {
    		 delete(formatXingeKey(derver));
    	}
    	set(formatXingeKey(derver), xinge, 365 * 24 * 3600);
		return xinge;
	}

	@Override
	public String getXinge(String derver) {
		return get(formatXingeKey(derver));
	}

}
