package com.common.redis;

import com.common.base.exception.BusinessException;
import com.common.redis.serialize.ListTranscoder;
import com.common.redis.serialize.MapTranscoder;
import com.common.redis.serialize.ObjectsTranscoder;
import com.common.spring.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

@Component("RedisClient")
public class RedisClient {

	private static RedisProperties redisProperties;

	@Autowired
	public void setRedisProperties(RedisProperties redisProperties) {
		RedisClient.redisProperties = redisProperties;
	}

	//是否设置有效期标识
	private int noSetExpFlag = 1;

	public static String type_list = RedisManager.type_list;
	public static String type_map = RedisManager.type_map;


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
		Jedis jedis = getJedis();
		jedis.set(key, value);
		closeJedis(jedis);
	}
	public void set(String key, String value, int seconds){
		Jedis jedis = getJedis();
		jedis.setex(key, seconds, value);
		closeJedis(jedis);
	}


	/**
	 * 存入 object
	 * @param key
	 */
	public <T> void set(String key, T t){
		Jedis jedis = getJedis();
		ObjectsTranscoder<Object> objectsTranscoder=new ObjectsTranscoder<>();
		byte[] bytes=objectsTranscoder.serialize(t);
		jedis.set(key.getBytes(), bytes);
		closeJedis(jedis);
	}
	public <T> void set(String key, T t, int seconds){
		Jedis jedis = getJedis();
		ObjectsTranscoder<T> objectsTranscoder=new ObjectsTranscoder<>();
		byte[] bytes=objectsTranscoder.serialize(t);
		jedis.setex(key.getBytes(),seconds,bytes);
		closeJedis(jedis);
	}
	
	/**
	 * 存入 list.Object
	 * @param key
	 * @param list
	 */
	public <T> void set(String key, List<T> list){
		Jedis jedis = getJedis();
		ListTranscoder<Object> listTranscoder=new ListTranscoder<>();
		byte[] bytes=listTranscoder.serialize(list);
		jedis.set(key.getBytes(),bytes);
		closeJedis(jedis);
	}
	public <T> void set(String key, List<T> list, int seconds){
		Jedis jedis = getJedis();
		ListTranscoder<Object> listTranscoder = new ListTranscoder<>();
		byte[] bytes=listTranscoder.serialize(list);
		jedis.setex(key.getBytes(),seconds,bytes);
		closeJedis(jedis);
	}

	/**
	 * 存入 map (string , object)
	 * @param key
	 * @param map
	 */
	public <T> void set(String key, Map<String, T> map){
		Jedis jedis = getJedis();
		MapTranscoder<T> mapTranscoder=new MapTranscoder<>();
		byte[] bytes=mapTranscoder.serialize(map);
		jedis.set(key.getBytes(),bytes);
		closeJedis(jedis);
	}
	public <T> void set(String key, Map<String, T> map, int seconds){
		Jedis jedis = getJedis();
		MapTranscoder<T> mapTranscoder = new MapTranscoder<>();
		byte[] bytes = mapTranscoder.serialize(map);
		jedis.setex(key.getBytes(),seconds,bytes);
		closeJedis(jedis);
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
	 * 读取 Object
	 * @param key
	 * @return object
	 */
	public <T> T getObject(String key){
		Jedis jedis = getJedis();
		ObjectsTranscoder<T>  objectsTranscoder=new ObjectsTranscoder<>();
		T value=objectsTranscoder.deserialize(jedis.get(key.getBytes()));
		closeJedis(jedis);
		return value;
	}
	
	/**
	 * 读取 List
	 * @param key
	 * @return list
	 */
	public <T> List<T> getList(String key){
		Jedis jedis = getJedis();
		ListTranscoder<T> listTranscoder = new ListTranscoder<T>();
		List<T> value=listTranscoder.deserialize(jedis.get(key.getBytes()));
		closeJedis(jedis);
		return value;
	}


	
	/**
	 * 读取 Map
	 * @param key
	 * @return map
	 */
	public <T> Map<String, T> getMap(String key){
		Jedis jedis = getJedis();
		MapTranscoder<T> mapTranscoder=new MapTranscoder<>();
		Map<String, T> value=mapTranscoder.deserialize(jedis.get(key.getBytes()));
		closeJedis(jedis);
		return value;
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
	 * @Title: list 添加一条数据
	 * @author jianghaoming
	 * @date 2017/1/13  14:19
	 * @param
	 * @return
	 */
	public <T> void insertList(String key, T t) throws BusinessException {
		this.insertList(key,t,noSetExpFlag);
	}
	public <T> void insertList(String key, T t, int seconeds) throws BusinessException {
		checkNull(key); //检查是否存在
		//获取redis中list，并添加数据
		List<T> list = this.getList(key);
		list.add(t);
		//设置有效期
		this.setTtl(key,list,seconeds,type_list);
	}

	/**
	 * @Title: map 添加一条数据
	 * @author jianghaoming
	 * @date 2017/1/13  14:19
	 * @param
	 * @return
	 */
	public <T> void insertMap(String key, String mapkey, T t) throws BusinessException {
		this.insertMap(key,mapkey,t,noSetExpFlag);
	}
	public <T> void insertMap(String key, String mapkey, T t,int seconeds) throws BusinessException {
		checkNull(key); //检查是否存在
		//获取redis中map，并添加数据
		Map<String,T> map = this.getMap(key);
		map.put(mapkey,t);
		//设置有效期
		this.setTtl(key,map,noSetExpFlag,type_map);
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
	

	/**
	 * @Title: 判断有效期，并重新赋值
	 * @author jianghaoming
	 * @date 2017/1/13  15:06
	 * @param secd 有效期， 重置有效期时间，为1不设置有效期时间
	 */
	 private <T> void setTtl(final String key, T t, int secd, final String type) throws BusinessException {
		 Map<String, T> map = null;
		 List<T> list = null;
		 if(type.equals(type_map)) {
			 map = (Map<String, T>) t;
		 }else if(type.equals(type_list)){
			 list = (List<T>) t;
		 }

		 if(secd != noSetExpFlag){
			 if(type.equals(type_map)) {
				 this.set(key, map, secd);
			 }else if(type.equals(type_list)){
				 this.set(key, list, secd);
			 }
		 } else {
			 secd = getTtl(key);
			 if (secd > 0) {
				 if(type.equals(type_map)) {
					 this.set(key, map, secd);
				 } else if(type.equals(type_list)){
					 this.set(key, list, secd);
				 }
			 } else {
				 if(type.equals(type_map)) {
					 this.set(key, map);
				 } else if(type.equals(type_list)){
					 this.set(key, list);
				 }

			 }
		 }
	 }


	
}