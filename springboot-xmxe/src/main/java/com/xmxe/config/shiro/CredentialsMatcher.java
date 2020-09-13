package com.xmxe.config.shiro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;

public class CredentialsMatcher extends SimpleCredentialsMatcher{
    Logger logger = LogManager.getLogger(CredentialsMatcher.class);

	@Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
        String inPassword = new String(utoken.getPassword());
        PrincipalCollection pc = info.getPrincipals();       
        Object primary = pc.getPrimaryPrincipal();//获取保存在subject中用户数据查询数据库密码作对比
        logger.info("getPrimaryPrincipal--->{}",primary);
        //获得数据库中的密码
        String dbPassword= new String((char[]) info.getCredentials()) ;
        logger.info("dbPassword--->{}",dbPassword);
        //进行密码的比对
        return this.equals(inPassword, dbPassword);
    }

}
