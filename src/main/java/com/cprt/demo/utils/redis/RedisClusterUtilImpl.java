package com.cprt.demo.utils.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;


public class RedisClusterUtilImpl implements IRedisClient{
	


	@Autowired
    private JedisCluster jedisCluster;  
      
    public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override  
    public String get(String key) {  
        return jedisCluster.get(key);  
    }  
	
	@SuppressWarnings("deprecation")
	private void closeJedisCluster(JedisCluster edisCluster){
		if(jedisCluster==null){
			return;
		}
		
		try {
			jedisCluster.quit();
			//jedisCluster.quit();
		} catch (Exception e) {
		}
		
	}
  
    @Override  
    public String set(String key, String value) {  
    	String result = "";
    	try {
    		result = jedisCluster.set(key, value); 
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result; 
    }  
  
    @Override  
    public String hget(String hkey, String key) {  
    	String result = "";
    	try {
    		result = jedisCluster.hget(hkey, key); 
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;
    }  
  
    @Override  
    public long hset(String hkey, String key, String value) {  
    	long result = 0L;
    	try {
    		result = jedisCluster.hset(hkey, key, value);
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }  
  
    @Override  
    public long incr(String key) {
    	long result = jedisCluster.incr(key);
    	try {
    		result = jedisCluster.incr(key); 
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }  
  
    @Override  
    public long expire(String key, int second) {
    	long result = 0L;
    	try {
    		result = jedisCluster.expire(key, second);  
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		} finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }  
  
    @Override  
    public long ttl(String key) {
    	long result = 0L;
    	try {
			result = jedisCluster.ttl(key);
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }  
  
    @Override  
    public long del(String key) {
    	long result = 0L;
    	try {
    		result = jedisCluster.del(key); 
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }  
  
    @Override  
    public long hdel(String hkey, String key) {
    	long result = 0L;
    	try {
    		result = jedisCluster.hdel(hkey, key);
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
        return result;  
    }

    
    
	@Override
	public Object getObject(String key) {
		try {
			byte[] bs = jedisCluster.get(key.getBytes());
			Object obj = byteToObject(bs);
			Object localObject2 = obj;
			return localObject2;
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
		return null;
	}

	@Override
	public String setObject(String key, Object obj) {
		String result = "";
		try {
			result = setObjectImpl(key, obj);
		} catch (Exception e) {
			closeJedisCluster(jedisCluster);
		}finally {
			closeJedisCluster(jedisCluster);
		}
		return result;
	}

	@Override
	public String setObject(String key, Serializable value) {
		return setObjectImpl(key, value);
	}

	@Override
	public String setObject(String key, List<? extends Serializable> value) {
		return setObjectImpl(key, value);
	}

	@Override
	public String setObject(String key, Map<?, ? extends Serializable> value) {
		return setObjectImpl(key, value);
	}

	@Override
	public String setObject(String key, Set<? extends Serializable> value) {
		return setObjectImpl(key, value);
	}

	@Override
	public <T> T getObject(String key, Class<T> clazz) {
		T result = null;
		byte[] bs = jedisCluster.get(key.getBytes());
		Object obj = byteToObject(bs);
		Object localObject2 = obj;
		result = clazz.cast(localObject2);	
		closeJedisCluster(jedisCluster);
		return result;
	}
	
	
	
	

	@Override
	public boolean tryLock(String key) {
		key = key + "_lock";
		RedisBillLockHandlerCluster lockHandler = new RedisBillLockHandlerCluster(this.jedisCluster);
		closeJedisCluster(jedisCluster);
		return lockHandler.tryLock(key);
	}

	@Override
	public boolean tryLock(String key, int ex) {
		key = key + "_lock";
		RedisBillLockHandlerCluster lockHandler = new RedisBillLockHandlerCluster(this.jedisCluster);
		closeJedisCluster(jedisCluster);
		return lockHandler.tryLock(key, ex);
	}

	@Override
	public void unLock(String key) {
		key = key + "_lock";
		RedisBillLockHandlerCluster lockHandler = new RedisBillLockHandlerCluster(this.jedisCluster);
		closeJedisCluster(jedisCluster);
		lockHandler.unLock(key);
	}

	
	
	
	
	
	@Override
	public String setex(String key, int seconds, String value) {
		String result = jedisCluster.setex(key, seconds, value);
		String str1 = result;
		closeJedisCluster(jedisCluster);
		return str1;
	}

	@Override
	public boolean exists(String key) {
		boolean result = jedisCluster.exists(key.getBytes()).booleanValue();
		boolean bool1 = result;
		closeJedisCluster(jedisCluster);
		return bool1;
	}

	@Override
	public Map<String, ? extends Serializable> hgetAll(String key) {
		Map map = new HashMap();
		Map<byte[], byte[]> bbs = jedisCluster.hgetAll(key.getBytes());
		Object obj = null;
		for (Map.Entry<byte[], byte[]> e : bbs.entrySet()) {
			byte[] bkey = (byte[]) e.getKey();
			byte[] bs = (byte[]) e.getValue();
			obj = byteToObject(bs);
			map.put(new String(bkey), obj);
		}
		Map localMap1 = map;
		closeJedisCluster(jedisCluster);
		return localMap1;
	}

	@Override
	public Long rpush(String key, Serializable value) {
		byte[] byteArray = objectToByte(value);
		long size = jedisCluster.rpush(key.getBytes(), new byte[][] { byteArray }).longValue();
		Long localLong = Long.valueOf(size);
		closeJedisCluster(jedisCluster);
		return localLong;
	}

	@Override
	public Long lrem(String key, int count, Serializable value) {
		byte[] byteArray = objectToByte(value);
		long result = jedisCluster.lrem(key.getBytes(), count, byteArray).longValue();
		Long localLong = Long.valueOf(result);
		closeJedisCluster(jedisCluster);
		return localLong;
	}

	@Override
	public Long llen(String key) {
		Long size = jedisCluster.llen(key.getBytes()).longValue();
		Long localLong = Long.valueOf(size);
		closeJedisCluster(jedisCluster);
		return localLong;
	}

	@Override
	public List lrange(String key, int start, int end) {
		List list = new ArrayList<>();
		List bs = jedisCluster.lrange(key.getBytes(), start, end);
		for (int i = 0; i < bs.size(); i++) {
			list.add(byteToObject((byte[]) bs.get(i)));
		}
		List localList1 = list;
		closeJedisCluster(jedisCluster);
		return localList1;
	}

	@Override
	public String hmset(String key, Map<String, ? extends Serializable> values) {
		Map hash = new HashMap();
		for (Map.Entry e : values.entrySet()) {
			String skey = (String) e.getKey();
			Serializable value = (Serializable) e.getValue();
			byte[] byteArray = objectToByte(value);
			hash.put(skey.getBytes(), byteArray);
		}
		String result = jedisCluster.hmset(key.getBytes(), hash);
		String str1 = result;
		closeJedisCluster(jedisCluster);
		return str1;
	}  

	@Override
	public Long setnx(String key, String value) {
		long result = jedisCluster.setnx(key, value).longValue();
		Long localLong = Long.valueOf(result);
		closeJedisCluster(jedisCluster);
		return localLong;
	}

	@Override
	public String getSet(String key, String value) {
		String result = jedisCluster.getSet(key, value);
		String str1 = result;
		closeJedisCluster(jedisCluster);
		return str1;
	}
    
	private Object byteToObject(byte[] bs) {
		Object obj = null;
		try {
			if (bs != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
				ObjectInputStream inputStream = new ObjectInputStream(bis);
				obj = inputStream.readObject();
			}

			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		closeJedisCluster(jedisCluster);
		return obj;
	}
    
	private String setObjectImpl(String key, Object value) {
		byte[] byteArray = objectToByte(value);
		String setObjectRet = jedisCluster.set(key.getBytes(), byteArray);
		String str1 = setObjectRet;
		closeJedisCluster(jedisCluster);
		return str1;
	}
	
	private byte[] objectToByte(Object obj) {
		byte[] bs = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bs = bos.toByteArray();
			oos.close();
			bos.close();
			return bs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bs;
	}
}
