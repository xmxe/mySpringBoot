package com.xmxe.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;

//自定义Realm
public class MyRealm extends AuthorizingRealm{

    /**
     * 身份认证/登录（账号密码认证） 将用户信息进行封装 在SimpleCredentialsMatcher进行验证
     */
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的token （用户名密码等信息）
	    UsernamePasswordToken utoken=(UsernamePasswordToken) token;
	    // 输入的密码
	    String inPassword = new String(utoken.getPassword());
	    // 输入的用户
        String inUsername = utoken.getUsername();

//        Object username2 = token.getPrincipal();
//        String password2 = new String((char[]) token.getCredentials());

//        User user = masterMapper.getUserById("test".equals(inUsername) ? 1 : 2);
//        if(user == null)
//            throw new UnknownAccountException("账号不存在");

//        if ("1".equals(username)){
//	        //处理session 单用户登录
//	        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
//	        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
//	        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
//	        for(Session session:sessions){
//	            //清除该用户以前登录时保存的session
//	            if(username.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
//	                sessionManager.getSessionDAO().delete(session);
//	            }
//	        }
//        }

        //放入shiro 调用CredentialsMatcher检验密码
//        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword() , ByteSource.Util.bytes("qwert"),this.getClass().getName());
        return new SimpleAuthenticationInfo("test","30be0a1931a958bfd53ec65fe3f1e497c7629a8c",ByteSource.Util.bytes("qwert"),this.getClass().getName());
    }
    /**
     *授权 验证权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取登录用户,得到的为new SimpleAuthenticationInfo(username, utoken.getPassword(),this.getClass().getName())第一个参数
        String name = (String) principal.getPrimaryPrincipal();
    	Map<String,Collection<String>> map = new HashMap<>();

        List<String> roleidsList = new ArrayList<>();
        roleidsList.add("user");
        map.put("roleIds",roleidsList);

        List<String> powersList = new ArrayList<>();
        powersList.add("user:add");
        map.put("powers",powersList);

        // 添加角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(map.get("roleIds"));
        info.addStringPermissions(map.get("powers"));
        return info;
    }

}
