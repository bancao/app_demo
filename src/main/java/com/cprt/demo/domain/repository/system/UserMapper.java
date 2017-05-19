package com.cprt.demo.domain.repository.system;

import java.util.List;

import com.cprt.demo.domain.model.system.User;

public interface UserMapper {
	int insert(User user);

	int update(User user);

	User selectByPrimaryKey(Long id);

	int deleteByPrimaryKey(Long id);

	List<User> selectPageList(User user);

	int selectCount(User user);

	User selectByAccountAndPassword(User user);

	List<String> findAuthorities(String userName);

	int updateLock(int userId);

	List<User> selectUserByRoleId(int roleId);
}
