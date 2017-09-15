package com.common.redis;

import com.common.spring.ToolSpring;
import redis.clients.jedis.Jedis;

/**
 * Created by jianghaoming on 2016/12/29.17:52
 */
public class RedisManager {

    public static String type_str = "string";
    public static String type_obj = "object";
    public static String type_list = "list";
    public static String type_map = "map";

    private RedisClient redisClient;

    private RedisClient getRedisClient() {
        if (null == this.redisClient) {
            this.redisClient = (RedisClient) ToolSpring.getBean("RedisClient");
        }
        return redisClient;
    }

    private static RedisManager instance = null;

    static {
        instance = new RedisManager();
    }

    private RedisManager() {}
    private static RedisManager getInstance() {
        return instance;
    }
    public static RedisClient getRedis(){
        return getInstance().getRedisClient();
    }
    public static Jedis getJedis(){
        return getRedis().getJedis();
    }






}
