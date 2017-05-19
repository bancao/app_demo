package com.cprt.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cprt.demo.domain.model.system.User;
import com.cprt.demo.domain.repository.system.UserMapper;
import com.cprt.demo.utils.redis.IRedisClient;

@Service("userService")
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    @Qualifier("redisClient")
    private IRedisClient redisClient;

	
	public User login(User user) throws Exception{
//		redisClient.setObject(user.getLoginAccount(), user);
//		User u = (User) redisClient.getObject(user.getLoginAccount());
//		log.info("==============="+u.getPassword());
		return userMapper.selectByAccountAndPassword(user);
	}
	
	/**
	 * 根据用户名查询权限
	 * @param userName
	 * @return
	 */
	public List<String> findAuthorities(String userName) {
		return userMapper.findAuthorities(userName);
	}
}
