package com.cprt.demo.utils.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRedisClient {
	String get(String key);  
    String set(String key, String value);  
    String hget(String hkey, String key);  
    long hset(String hkey, String key, String value);  
    long incr(String key);  
    long expire(String key, int second);  
    long ttl(String key);  
    long del(String key);  
    long hdel(String hkey, String key);
    
    
    public Object getObject(String key);
    public String setObject(String key, Object obj);
	public String setObject(String key, Serializable value);
	public String setObject(String key, List<? extends Serializable> value);
	public String setObject(String key,Map<?, ?extends Serializable> value);
	public String setObject(String key,Set<?extends Serializable> value);
    public <T> T getObject(String key, Class<T> clazz);
    public boolean tryLock(String key);
	public boolean tryLock(String key, int ex);
	public void unLock(String key) ;
	public String setex(String key,int seconds,String value);
	public boolean exists(String key);
	public Map<String,? extends Serializable> hgetAll(String key);
	public Long rpush(String key,Serializable value);
	public Long lrem(String key,int count,Serializable value);
	public Long llen(String key);
	public <E> List<E> lrange(String key,int start,int end);
	public String hmset(String key,Map<String, ? extends Serializable> values);
	public Long setnx(String key,String value);
	public String getSet(String key,String value);
}
