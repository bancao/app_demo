package com.cprt.demo.http.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;

import com.google.common.collect.Maps;

public class SessionCache implements Cache<Serializable, Session> {
	
	private static final Map<Serializable, Session> map = Maps.newHashMap();
	
	@Override
	public void clear() throws CacheException {
		map.clear();
	}

	@Override
	public Session get(Serializable key) throws CacheException {
		return map.get(key);
	}

	@Override
	public Set<Serializable> keys() {
		return map.keySet();
	}

	@Override
	public Session put(Serializable key, Session value) throws CacheException {
		map.put(key, value);
		return value;
	}

	@Override
	public Session remove(Serializable key) throws CacheException {
		Session session = map.remove(key);
		if(session != null) {
			session.setAttribute(key, null);
		}
		return session;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<Session> values() {
		return map.values();
	}

}
