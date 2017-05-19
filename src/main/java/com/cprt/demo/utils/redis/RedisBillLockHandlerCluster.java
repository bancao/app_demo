package com.cprt.demo.utils.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisBillLockHandlerCluster  {

	private static final Logger LOGGER= LoggerFactory.getLogger(RedisBillLockHandlerCluster.class);

	//默认超时时间：120s
	private static final int DEFAULT_SINGLE_EXPIRE_TIME = 120;
	
	private final JedisCluster jedisCluster;
	
	/**
	 * 构造
	 * @author 
	 */
	public RedisBillLockHandlerCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	/**
	 * 获取锁  如果锁可用   立即返回true，  否则返回false
	 * @author 
	 * @param billIdentify
	 * @return
	 */
	public boolean tryLock(String key) {
		return tryLock(key, 0L, null,DEFAULT_SINGLE_EXPIRE_TIME);
	}

	public boolean tryLock(String key, int expireTime) {
		return tryLock(key, 0L, null, expireTime);
	}
	
	/**
	 * 获取锁，锁自带默认120s超时时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean  tryLockWithExprie(String key, long timeout, TimeUnit unit) {
		return tryLock(key, 0L, null,DEFAULT_SINGLE_EXPIRE_TIME);
	}
	
	
	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * @author 
	 * @param billIdentify
	 * @param timeout
	 * @param unit
	 * @param expireTime : 设置锁自动超时时间（单位：s）
	 * @return
	 */
	public synchronized boolean  tryLock(String key, long timeout, TimeUnit unit, int expireTime) {
		try {
			long nano = System.nanoTime();
			do {
				LOGGER.debug("try lock key: " + key);
				//如返回1，则该客户端获得锁,
				//如返回0，表明该锁已被其他客户端取得
				//SET if Not eXists
				Long i = jedisCluster.setnx(key, key);
				if (i == 1) { 
					if(expireTime > 0) {
						jedisCluster.expire(key, expireTime);
						LOGGER.debug("get lock, key: " + key + " , expire in " + expireTime + " seconds.");
					}
					return Boolean.TRUE;
				} else { // 存在锁
					if (LOGGER.isDebugEnabled()) {
						String desc = jedisCluster.get(key);
						LOGGER.debug("key: " + key + " locked by another business：" + desc);
					}
				}
				if (timeout == 0) {
					break;
				}
				Thread.sleep(300);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));
			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			LOGGER.error(je.getMessage(), je);
			returnResource();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			returnResource();
		} finally {
			returnResource();
		}
		return Boolean.FALSE;
	}

	/**
	 * 如果锁空闲立即返回   获取失败 一直等待
	 * @author 
	 * @param billIdentify
	 */
	public void lock(String key) {
		try {
			do {
				LOGGER.debug("lock key: " + key);
				Long i = jedisCluster.setnx(key, key);
				if (i == 1) { 
					jedisCluster.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					LOGGER.debug("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");
					return;
				} else {
					if (LOGGER.isDebugEnabled()) {
						String desc = jedisCluster.get(key);
						LOGGER.debug("key: " + key + " locked by another business：" + desc);
					}
				}
				Thread.sleep(300); 
			} while (true);
		} catch (JedisConnectionException je) {
			LOGGER.error(je.getMessage(), je);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
		}
	}

	/**
	 * 释放锁
	 * @author 
	 * @param billIdentify
	 */
	public void unLock(String key) {
		List<String> list = new ArrayList<String>();
		list.add(key);
		unLock(list);
	}

	/**
	 * 批量释放锁
	 * @author 
	 * @param billIdentifyList
	 */
	public synchronized void unLock(List<String> keyList) {
		List<String> keys = new CopyOnWriteArrayList<String>();
		for (String keyObj : keyList) {
			String key = keyObj;
			keys.add(key);
		}
		try {
			jedisCluster.del(keys.toArray(new String[0]));
			LOGGER.debug("release lock, keys :" + keys);
		} catch (JedisConnectionException je) {
			LOGGER.error(je.getMessage(), je);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			returnResource();
		} finally {
			returnResource();
		}
	}
	
	/**
	 * @author 
	 * @param jedis
	 */
	private void returnResource() {
		try {
//			jedisCluster.quit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
