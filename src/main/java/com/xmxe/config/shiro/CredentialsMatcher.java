package com.xmxe.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;

public class CredentialsMatcher extends SimpleCredentialsMatcher{
	@Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
        String inPassword = new String(utoken.getPassword());
        PrincipalCollection pc = info.getPrincipals();       
        pc.getPrimaryPrincipal();//获取保存在subject中用户数据查询数据库密码作对比
        //获得数据库中的密码
        //String dbPassword=(String) info.getCredentials();
        //进行密码的比对
        return this.equals(inPassword, "1");
    }

}
