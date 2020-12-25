package com.xmxe.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialsMatcher extends SimpleCredentialsMatcher{
    // log4j2实现
    // Logger logger = LogManager.getLogger(CredentialsMatcher.class);
    // slf4j实现
    Logger logger = LoggerFactory.getLogger(CredentialsMatcher.class);

    /**
     * @param token 页面输入输入的数据
     * @param info 从MyRealm doGetAuthenticationInfo()得到的数据 看返回类型就知道
     */
	@Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取页面输入的数据
	    UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        String inPassword = new String(utoken.getPassword());
        String inUsername = utoken.getUsername();

        logger.info("开始密码匹配，输入的密码为--->{},用户名为--->{},token.getCredentials--->{}",inPassword,inUsername,super.getCredentials(token));

        //获得MyRealm送过来的数据:(可以采用加盐(salt)的方式去检验)
        String dbPassword= String.valueOf(super.getCredentials(info));
//        PrincipalCollection pc = info.getPrincipals();
//        Object primary = pc.getPrimaryPrincipal();
//
//        SimpleAuthenticationInfo simpleAuthenticationInfo = (SimpleAuthenticationInfo)info;
//        ByteSource byteSource = simpleAuthenticationInfo.getCredentialsSalt();
//        logger.info("获取new SimpleAuthenticationInfo中用户数据--->{}",simpleAuthenticationInfo);
        SimpleHash simpleHash = new SimpleHash("SHA-1", inPassword, "qwert");
        //进行密码的比对
        return this.equals(simpleHash.toHex(), dbPassword);
    }

//    shiro 密码加密(加盐)
  /* public static void main(String[] args) {
	    String salt = "qwert";//盐值
        SimpleHash simpleHash = new SimpleHash("SHA-1", "test1", salt);
//        new SimpleHash(Sha256Hash.ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), hashIterations).toBase64();
        System.out.println(simpleHash.toHex());

    }*/
}
