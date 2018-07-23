package com.common.redis;

import com.common.base.exception.BusinessException;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 *
 * update jhm 20180721
 * jedis --> luttuce
 *
 * get --> sync
 * set & del --> async
 */
@Component("RedisClient")
public class RedisClient {

	private static RedisProperties redisProperties;

	@Autowired
	public void setRedisProperties(RedisProperties redisProperties) {
		RedisClient.redisProperties = redisProperties;
	}

    private static StatefulRedisConnection<String, String> connection = null;
    private static io.lettuce.core.RedisClient lettuceRedis = null;
	private static synchronized void initRedis(){
		String passwd = redisProperties.getPassword();
		String host = redisProperties.getHost();
		String port = redisProperties.getPort();
        if(ObjectUtils.isEmpty(lettuceRedis)){
            RedisURI uri = new RedisURI();
            uri.setHost(host);
            uri.setPort(Integer.parseInt(port));
            if(!ObjectUtils.isEmpty(passwd)) {
                uri.setPassword(passwd);
            }
            lettuceRedis = io.lettuce.core.RedisClient.create(uri);
            connection = lettuceRedis.connect();
        }
	}

    /**
     * 异步
     * @return
     */
	public RedisAsyncCommands<String, String> getAsyncCommand(){
	    if(ObjectUtils.isEmpty(lettuceRedis)){
	        initRedis();
        }
		if(ObjectUtils.isEmpty(connection)) {
            connection = lettuceRedis.connect();
		}
		return connection.async();
	}

    /**
     * 同步
     * @return
     */
    public RedisCommands<String, String> getCommand(){
        if(ObjectUtils.isEmpty(lettuceRedis)){
            initRedis();
        }
        if(ObjectUtils.isEmpty(connection)) {
            connection = lettuceRedis.connect();
        }
        return connection.sync();
    }

	public void closeConnection(){
		if(ObjectUtils.isEmpty(connection)){
            connection.close();
		}
	}

	/**
	 * 存入 String
	 */
	public void set(String key, String value){
		this.set(key,value,-1);
	}

	public void set(String key, String value, int seconds){
        if(seconds > 0) {
            getAsyncCommand().setex(key, seconds, value);
        }else{
            getAsyncCommand().set(key,value);
        }
	}

	/**
	 * 存储对象
	 * @param key
	 */
	public <T> void set(String key, T t){
	    this.set(key,t,-1);
	}
	public <T> void set(String key, T t, int seconds){
	    try {
            String json = GsonUtils.toJson(t);
            this.set(key,json,seconds);
        }catch (Exception e){
	        //不能json转换，直接强转为string
            this.set(key, t + "", seconds);
        }

    }

	/**
	 * 读取 String
	 * @param key
	 * @return String
	 */
	public String get(String key){
		return getCommand().get(key);
	}
	
	/**
	 * 读取 对象
	 * @param key
	 * @return object
	 */
	public <T> T get(String key, TypeToken<T> typeToken){
		String json = get(key);
		return  GsonUtils.conver(json, typeToken);
	}

	public Boolean exists(String key){
		return getCommand().exists(key) > 0;
	}

	/**
	 * @Title: 检查是否为空
	 * @author jianghaoming
	 * @date 2017/1/13  14:33
	 */
	public void checkNull(String key) throws BusinessException
	{
		if(!exists(key)){
			throw new BusinessException("未找到相关redis数据，key :" + key);
		}
	}

	/**
	 * @Title: 获取key的有效期
	 * @author jianghaoming
	 * @date 2017/1/13  14:29
	 * @return 单位为秒 , -1, 永不过期。
	 */
	public int getTtl(String key) throws BusinessException {
		checkNull(key);
		return Math.toIntExact(getCommand().ttl(key));
	}

    /**
     * 获取key值列表
     */
	public List<String> getKeys(String pattern){
        return getCommand().keys(pattern);
    }

	/**
	 * 删除
	 */
	public void delKey(String key){
		getAsyncCommand().del(key);
	}

	public void delKeys(String pattern){
        List<String> result = getKeys(pattern);
        for(String key : result){
            delKey(key);
        }
    }
	
	
	/**
	 * 清空DB
	 */
	 public void flushDB(){
		 getCommand().flushdb();
	 }
	 
	/**
	 * 清空所有
	 */
	 public void flushAll(){
		getCommand().flushall();
	 }


	
}