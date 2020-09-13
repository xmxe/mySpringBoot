package com.xmxe.config.shiro;

import com.xmxe.dao.db1.DB1Dao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

//自定义Realm
public class MyRealm extends AuthorizingRealm{
    @Autowired
    DB1Dao db1;
	//验证用户
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;//获取用户输入的token
        String username = utoken.getUsername();
        Object username2 = token.getPrincipal();
        System.err.println(username2);
        //此处应填写根据用户名查询用户的方法，由于本测试只用"1"作为用户名,故此方法暂时忽略不写
        //User user = service.getUser(username);
        if ("1".equals(username)){
	        //处理session 单用户登录
	        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
	        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
	        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
	        for(Session session:sessions){
	            //清除该用户以前登录时保存的session
	            if(username.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
	                sessionManager.getSessionDAO().delete(session);
	            }
	        }
        }
        return new SimpleAuthenticationInfo(username, utoken.getPassword(),this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
    }
    //授权 验证权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {    
    	//principal.getPrimaryPrincipal();得到的为new SimpleAuthenticationInfo(username, utoken.getPassword(),this.getClass().getName())第一个参数
    	Map<String,Collection<String>> map = new HashMap<>();
        List<String> roleidsList = new ArrayList<>();
        roleidsList.add("a");
        map.put("roleIds",roleidsList);
        List<String> powersList = new ArrayList<>();
        powersList.add("user:add");
        map.put("powers",powersList);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(map.get("roleIds"));
        info.addStringPermissions(map.get("powers"));
        return info;
    }

}
