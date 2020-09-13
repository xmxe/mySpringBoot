package com.xmxe.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;


@Configuration
public class ShiroConfiguration {
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginCheck", "anon"); //表示可以匿名访问
        filterChainDefinitionMap.put("/code", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        chainDefinition.addPathDefinitions(filterChainDefinitionMap);

        // all other paths require a logged in user
        //chainDefinition.addPathDefinition("/loginCheck","anon");
        //chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;

        /*ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/");
        bean.setSuccessUrl("/index");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/loginCheck", "anon"); //表示可以匿名访问
        filterChainDefinitionMap.put("/code", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;*/
    }
    //配置自定义的权限登录器
    @Bean
    public MyRealm authRealm() {
        MyRealm authRealm=new MyRealm();
        authRealm.setCachingEnabled(true);
        authRealm.setAuthenticationCachingEnabled(false);
        authRealm.setCredentialsMatcher(credentialsMatcher());
        return authRealm;
    }
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManage = new DefaultWebSessionManager();
        sessionManage.setGlobalSessionTimeout(1000 * 60 * 30);
        sessionManage.setSessionDAO(sessionDAO());
        sessionManage.setSessionValidationSchedulerEnabled(true);
        sessionManage.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
        sessionManage.setSessionIdCookieEnabled(true);
        sessionManage.setSessionIdCookie(getSessionIdCookie());
        //解决Shiro第一次重定向url携带jsessionid问题 https://blog.csdn.net/Ruanes/article/details/108417460
        sessionManage.setSessionIdUrlRewritingEnabled(false);
        return sessionManage;
    }
    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SessionsSecurityManager securityManager() {
        System.out.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(authRealm());
        manager.setSessionManager(sessionManager());
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

    //会话验证调度器
    @Bean(name = "sessionValidationScheduler")
	public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
		scheduler.setInterval(15 * 60 * 1000);
		return scheduler;
	}


    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }
   /*
    * 管理shiro bean生命周期
    */
    @Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        /*
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。 加入这项配置能解决这个bug
         */
        creator.setUsePrefix(true);
        //以cglib动态代理方式生成代理类
        creator.setProxyTargetClass(true);
        return creator;
    }

    /*
     *springboot shiro开启注释
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    /*
     *html引用shiro标签所需要注册的bean
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}
