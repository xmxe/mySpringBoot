package com.xmxe.config;

import com.xmxe.entity.CASClientProperties;
import com.xmxe.entity.CASServerProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
public class CasSecurityConfig {
    @Autowired
    CASClientProperties casClientProperties;
    @Autowired
    CASServerProperties casServerProperties;
    @Autowired
    @Qualifier("userDetail")
    UserDetailsService userDetailService;

    //ServiceProperties 中主要配置一下 Client 的登录地址即可，这个地址就是在 CAS Server 上登录成功后，重定向的地址。
    @Bean
    ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casClientProperties.getLogin());   //http://localhost:9103/login/cas
        return serviceProperties;
    }

    // CAS 验证的入口，这里首先设置 CAS Server 的登录地址，同时将前面的 ServiceProperties 设置进去，这样当它登录成功后，就知道往哪里跳转了。
    @Bean
    @Primary
    AuthenticationEntryPoint authenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casServerProperties.getLogin()); //http://192.168.236.131:9100/cas/login
        entryPoint.setServiceProperties(serviceProperties());
        return entryPoint;
    }

    //CAS Client 拿到 ticket 要去 CAS Server 上校验，默认校验地址是：http://192.168.236.131:9100/cas/proxyValidate?ticket=xxx
    @Bean
    TicketValidator ticketValidator() {
        return new Cas20ProxyTicketValidator(casServerProperties.getPrefix());
    }

    //处理 CAS 验证逻辑
    @Bean
    CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(ticketValidator());
        provider.setUserDetailsService(userDetailService);
        provider.setKey("admin");
        return provider;
    }

    //CAS 认证的过滤器，过滤器将请求拦截下来之后，交由 CasAuthenticationProvider 来做具体处理。
    @Bean
    CasAuthenticationFilter casAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(serviceProperties());
        filter.setAuthenticationManager(new ProviderManager(authenticationProvider));
        return filter;
    }

    //SingleSignOutFilter 表示接受 CAS Server 发出的注销请求，
    //所有的注销请求都将从 CAS Client 转发到 CAS Server，CAS Server 处理完后，会通知所有的 CAS Client 注销登录。
    @Bean
    SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter sign = new SingleSignOutFilter();
        sign.setIgnoreInitConfiguration(true);
        return sign;
    }

    //配置将注销请求转发到 CAS Server。
    @Bean
    LogoutFilter logoutFilter() {
        LogoutFilter filter = new LogoutFilter(casServerProperties.getLogout(), new SecurityContextLogoutHandler());//http://192.168.236.131:9100/cas/logout
        filter.setFilterProcessesUrl(casClientProperties.getLogoutRelative());//  /logout/cas
        return filter;
    }
}
