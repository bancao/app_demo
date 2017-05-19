/**
 * 
 */
package com.cprt.demo.security;

import java.util.List;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.cprt.demo.domain.model.system.User;
import com.cprt.demo.service.UserService;

public class AuthenticationRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	
	/**
	 * 获取认证信息
	 * 
	 * @param token
	 *            令牌
	 * @return 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken token) {
		AuthenticationToken authenticationToken = (AuthenticationToken) token;
		String username = authenticationToken.getUsername();
		String password = new String(authenticationToken.getPassword());
		String ip = authenticationToken.getHost();

		if (username != null && password != null) {
			User user = new User();
			user.setUserName(username);
			user.setPassword(password);
			
			try {
				User user2 = userService.login(user);
				if (user2 == null) {
					throw new UnknownAccountException();
				}
				int userRoldId = 0;
				return new SimpleAuthenticationInfo(new Principal(user2.getId().intValue(), username, userRoldId), password, getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new UnknownAccountException();	    
	}

	/**
	 * 获取授权信息
	 * 
	 * @param principals
	 *            principals
	 * @return 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) principals.fromRealm(getName()).iterator().next();
		String username = principal.getLoginAccount(); 
//		String username = (String) principals.fromRealm(getName()).iterator().next();
		if (username != null) {
			List<String> authorities = userService.findAuthorities(username);
			if (authorities != null) {
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
				authorizationInfo.addStringPermissions(authorities);
				return authorizationInfo;
			}
		}
		return null;
	}
	
	public void removeUserCache(String userId){
		  SimplePrincipalCollection pc = new SimplePrincipalCollection();
		  pc.add(userId, super.getName()); 
		  clearCachedAuthorizationInfo(pc);
		}
	
	
	 /**
   * 更新用户授权信息缓存.
   */
  public void clearCachedAuthorizationInfo(String principal) {
      SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
      clearCachedAuthorizationInfo(principals);
  }

  /**
   * 清除所有用户授权信息缓存.
   */
  public void clearAllCachedAuthorizationInfo() {
      Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
      System.out.println("进入方法");
      if (cache != null) {
          for (Object key : cache.keys()) {
              cache.remove(key);
          	System.out.println(key+"------");
          }
      }
  }
  
  public void delCache(String loginUserName){
		CacheManager cacheManager = getCacheManager();
		Cache<Object, Object> cache = cacheManager.getCache("authorization");
		System.out.println(getAuthenticationCacheName());
		Cache<Object, AuthorizationInfo> cache1 = getAuthorizationCache();
		if(cache.equals(cache1)){
			System.out.println("两个缓存相等");
		}
	      if (cache != null) {
	          for (Object key : cache.keys()) {
	              cache.remove(key);
	          	System.out.println(key+"------");
	          }
	      }
	      System.out.println("清理缓存1");
	      if (cache1 != null) {
	          for (Object key : cache1.keys()) {
	              cache.remove(key);
	          	System.out.println(key+"------");
	          }
	      }
	      System.out.println("清理缓存2");
		
	      
	      
		cache.remove(loginUserName);
  }
  
  
}

