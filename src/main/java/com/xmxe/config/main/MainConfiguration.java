package com.xmxe.config.main;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration // 类似于xml中的<beans>
//@PropertySource("classpath:application.properties") // 这个注解导入刚才增加的jdbc配置文件
//@EnableAspectJAutoProxy//开启自动代理 测试AOP时打开
public class MainConfiguration{
	
	
//	@Value("${jdbc.driver}")//获取application.properties属性值
	private String driver;
//	@Value("${jdbc.url}")
	private String url;
//	@Value("${jdbc.username}")
	private String username;
//	@Value("${jdbc.password}")
	private String password;


// druid bean注入配置  如果使用druid-spring-boot-starter集成的话只需要在application.properties配置而无需使用此配置(注释掉@Bean即可)
//	@Bean 
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	
	/*
	 * 统一页码处理配置
	 * */ 
	@Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        //常规写法
		/* return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
		    @Override
		    public void customize(ConfigurableWebServerFactory factory) {
		        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
		        factory.addErrorPages(errorPage404);
		    }
		};*/
        //lambda写法
        return (factory -> {
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/static/404.html");
            ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/500.html");
            factory.addErrorPages(errorPage404,errorPage500);
        });
    }
	
	/*
	 *springboot 1.5.x写法
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				Springboot中默认的静态资源路径有4个，分别是：
				**classpath:/METAINF/resources/，classpath:/resources/，classpath:/static/，classpath:/public/
				**优先级顺序为：META-INF/resources>resources>static>  public
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
				container.addErrorPages(error404Page, error500Page);
			}
		};
	}*/

}