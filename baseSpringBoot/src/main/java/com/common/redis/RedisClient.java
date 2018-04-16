package com.common.redis;

import com.common.base.exception.BusinessException;
import com.common.spring.utils.CommonUtils;
import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("RedisClient")
public class RedisClient {

	private static RedisProperties redisProperties;

	@Autowired
	public void setRedisProperties(RedisProperties redisProperties) {
		RedisClient.redisProperties = redisProperties;
	}

    //连接池
	private static JedisPool jedisPool = null;

	private synchronized static void initRedis(){

		String passwd = redisProperties.getPassword();
		String host = redisProperties.getHost();
		String port = redisProperties.getPort();

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		config.setMaxIdle(10);
		config.setMinIdle(5);//设置最小空闲数
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		//Idle时进行连接扫描
		config.setTestWhileIdle(true);
		//表示idle object evitor两次扫描之间要sleep的毫秒数
		config.setTimeBetweenEvictionRunsMillis(30000);
		//表示idle object evitor每次扫描的最多的对象数
		config.setNumTestsPerEvictionRun(10);
		//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		config.setMinEvictableIdleTimeMillis(60000);
		if(CommonUtils.isBlank(passwd)) {
			jedisPool = new JedisPool(config, host, Integer.parseInt(port), 10000);
		}else{
			jedisPool = new JedisPool(config, host, Integer.parseInt(port), 10000, passwd);
		}
	}

	protected static Jedis getJedis(){
		if(null == jedisPool) {
			initRedis();
		}
		return jedisPool.getResource();
	}

	private static void closeJedis(Jedis jedis){
		if(null != jedis){
			jedis.close();
		}
	}



	/**
	 * 存入 String
	 */
	public void set(String key, String value){
		this.set(key,value,-1);
	}

	public void set(String key, String value, int seconds){
		Jedis jedis = getJedis();
        if(seconds > 0) {
            jedis.setex(key, seconds, value);
        }else{
            jedis.set(key,value);
        }
		closeJedis(jedis);
	}

	/**
	 * 存储对象
	 * @param key
	 */
	public <T> void set(String key, T t){
	    this.set(key,t,-1);
	}
	public <T> void set(String key, T t, int seconds){
        this.set(key,GsonUtils.toJson(t),seconds);
    }


	/**
	 * 读取 String
	 * @param key
	 * @return String
	 */
	public String getString(String key){
		Jedis jedis = getJedis();
		String value = jedis.get(key);
		closeJedis(jedis);
		return value;
	}
	
	/**
	 * 读取 对象
	 * @param key
	 * @return object
	 */
	public <T> T get(String key, TypeToken<T> typeToken){
		Jedis jedis = getJedis();
		T result = GsonUtils.conver(getString(key), typeToken);
		closeJedis(jedis);
		return result;
	}

	/**
	 * @Title: 判断key是否存在
	 * @author jianghaoming
	 * @date 2017/1/13  14:28
	 */
	public Boolean exists(String key){
		Jedis jedis = getJedis();
		boolean flag = jedis.exists(key);
		closeJedis(jedis);
		return flag;
	}

	/**
	 * @Title: 检查是否为空
	 * @author jianghaoming
	 * @date 2017/1/13  14:33
	 */
	public void checkNull(String key) throws BusinessException
	{
		if(!exists(key)){
			throw new BusinessException("未找到相关redis数据，key="+key);
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
		Jedis jedis = getJedis();
		int result = Math.toIntExact(jedis.ttl(key));
		closeJedis(jedis);
		return result;
	}

    /**
     * 获取key值列表
     * @param pattern
     * @return
     */
	public Set<String> getKeys(String pattern){
        Jedis jedis = getJedis();
        jedis.keys(pattern);
        return jedis.keys(pattern);
    }

	/**
	 * 删除
	 */
	public void delKey(String key){
		Jedis jedis = getJedis();
		jedis.del(key);
		closeJedis(jedis);
	}
	
	
	/**
	 * 清空DB
	 */
	 public void flushDB(){
		 Jedis jedis = getJedis();
		 jedis.flushDB();
		 closeJedis(jedis);
	 }
	 
	/**
	 * 清空所有
	 */
	 public void flushAll(){
		 Jedis jedis = getJedis();
		 jedis.flushAll();
		 closeJedis(jedis);
	 }


	
}