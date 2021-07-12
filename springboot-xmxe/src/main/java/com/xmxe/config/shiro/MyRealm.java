package com.xmxe.config.shiro;

import com.xmxe.entity.User;
import com.xmxe.mapper.master.MasterMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

//自定义Realm
public class MyRealm extends AuthorizingRealm{

    @Resource
    MasterMapper masterMapper;

    /**
     * 验证用户
     */
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的token （用户名密码等信息）
	    UsernamePasswordToken utoken=(UsernamePasswordToken) token;
	    String inPassword = new String(utoken.getPassword());
        String inUsername = utoken.getUsername();
//        Object username2 = token.getPrincipal();

        User user = masterMapper.getUserById("test".equals(inUsername) ? 3 : 4);
        if(user == null)
            throw new UnknownAccountException("账号不存在");
       /* if ("1".equals(username)){
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
        }*/
        //放入shiro.调用CredentialsMatcher检验密码
        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword() , ByteSource.Util.bytes("qwert"),this.getClass().getName());
    }
    /**
     *授权 验证权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {    
    	//principal.getPrimaryPrincipal();得到的为new SimpleAuthenticationInfo(username, utoken.getPassword(),this.getClass().getName())第一个参数
    	Map<String,Collection<String>> map = new HashMap<>();
        List<String> roleidsList = new ArrayList<>();
        roleidsList.add("user");
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
