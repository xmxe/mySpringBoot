package com.xmxe.config.shiro;

import java.util.LinkedHashMap;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;


@Configuration
public class ShiroConfiguration {
    
    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("myRealm") MyRealm authRealm,@Qualifier("sessionManager")DefaultWebSessionManager sessionManager) {
        System.out.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        manager.setSessionManager(sessionManager);       
        manager.setCacheManager(getCacheManage());
        return manager;
    }
    @Bean(name = "sessionIdCookie")
	public SimpleCookie getSessionIdCookie() {
    	/*
    	 * 关于shiro报错 there is no session with id的相关问题 
    	 * 登陆页面不记住密码就不会报这个错 或者关闭浏览器（只要浏览器地址没有jsessionid就不会报错）
    	 * */
		SimpleCookie cookie = new SimpleCookie("shiro.session");
		//cookie.setHttpOnly(true);//表示js脚本无法读取cookie信息
		cookie.setMaxAge(-1);//-1表示关闭浏览器 cookie就会消失
		cookie.setPath("/");//正常的cookie只能在一个应用中共享，即：一个cookie只能由创建它的应用获得。可在同一应用服务器内共享cookie的方法：设置cookie.setPath("/");
		return cookie;
	}
    
    @Bean(name = "sessionDao")
    public MemorySessionDAO sessionDAO() {
    	return new MemorySessionDAO();
    }
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionDao") MemorySessionDAO sessionDAO) {
    	DefaultWebSessionManager sessionManage = new DefaultWebSessionManager();
    	sessionManage.setGlobalSessionTimeout(1800000);
    	sessionManage.setSessionDAO(sessionDAO);
    	sessionManage.setSessionValidationSchedulerEnabled(true);
    	sessionManage.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
    	sessionManage.setSessionIdCookieEnabled(true);
    	EnterpriseCacheSessionDAO cacheSessionDAO = new EnterpriseCacheSessionDAO();
		cacheSessionDAO.setCacheManager(getCacheManage());
    	sessionManage.setSessionIdCookie(getSessionIdCookie());
    	return sessionManage;
    }
    @Bean(name = "sessionValidationScheduler")
	public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
		scheduler.setInterval(900000);
		return scheduler;
	}
    //配置自定义的权限登录器
    @Bean(name="myRealm")
    public MyRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        MyRealm authRealm=new MyRealm();
        authRealm.setCachingEnabled(true);
        authRealm.setAuthenticationCachingEnabled(false);
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }
    @Bean(name = "cacheShiroManager")
	public CacheManager getCacheManage() {
		return new EhCacheManager();
	}
    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }
   
    @Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager manager) {
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/");
        bean.setSuccessUrl("/index");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginCheck", "anon"); //表示可以匿名访问
        filterChainDefinitionMap.put("/code", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/js/**", "anon");
//        filterChainDefinitionMap.put("/favicon.ico", "anon");
//        filterChainDefinitionMap.put("/camera/**", "anon");
//        filterChainDefinitionMap.put("/image/**", "anon");
//        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/*", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/*.*", "authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }
    //html引用shiro标签所需要注册的bean
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }    
}
