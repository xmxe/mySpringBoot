package com.mySpringBoot.config.interceptor;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器
		InterceptorRegistration ir = registry.addInterceptor(new MyInterceptor());
		// 配置拦截的路径
		ir.addPathPatterns("/**");
		// 配置不拦截的路径
		ir.excludePathPatterns("/");
		ir.excludePathPatterns("/code");
		ir.excludePathPatterns("/loginCheck");	
		ir.excludePathPatterns("/static/**");
		// 还可以在这里注册其它的拦截器
		// registry.addInterceptor(new OtherInterceptor()).addPathPatterns("/**");

	}
	//自定义静态资源路径
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")//指的是对外暴露的访问路径
				.addResourceLocations("classpath:/static/");//指的是内部文件放置的目录
	}
	
	//指定首页
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//super.addViewControllers(registry);
		//registry.addViewController("/").setViewName("login");
	    //registry.setOrder(Ordered.HIGHEST_PRECEDENCE);//springmvc的拦截器的优先级默认是高于shiro的，通过此方法可配置优先级的顺序
	    //实现一个请求到视图的映射，而无需书写controller
	    //registry.addViewController("/").setViewName("redirect:/index");  
	    
	}
	
	
	
	 /*Spring Boot中，SpringMVC相关的自动化配置是在 WebMvcAutoConfiguration配置类中实现的，它的生效条件有一条，就是当不存在 WebMvcConfigurationSupport 的实例时，这个自动化配置才会生生效
	 因此，如果我们在 Spring Boot 中自定义 SpringMVC 配置时选择了继承 WebMvcConfigurationSupport，就会导致 Spring Boot 中 SpringMVC 的自动化配置失效。
	 Spring Boot 给我们提供了很多自动化配置，很多时候当我们修改这些配置的时候，并不是要全盘否定 Spring Boot 提供的自动化配置，我们可能只是针对某一个配置做出修改，其他的配置还是按照 Spring Boot 默认的自动化配置来，
	  而继承 WebMvcConfigurationSupport 来实现对 SpringMVC 的配置会导致所有的 SpringMVC 自动化配置失效，因此，一般情况下我们不选择这种方案。
	  */
	
	
	//若直接继承WebMvcConfigurationSupport,会导致application.properties配置文件不生效，需要增加以下配置代替配置文件	  
	//试图渲染解析
	/*@Bean
	public InternalResourceViewResolver resourceViewResolver()
	{
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		//请求视图文件的前缀地址
		internalResourceViewResolver.setPrefix("/WEB-INF/views/");
		//请求视图文件的后缀
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		super.configureViewResolvers(registry);
		registry.viewResolver(resourceViewResolver());
		registry.jsp("/WEB-INF/views/",".jsp");
	}*/
	
	

}

/**
 * 在SpringBoot2.0及Spring 5.0 WebMvcConfigurerAdapter已被废弃,标记为过时。
 * 	使用方式： 1.直接实现WebMvcConfigurer （官方推荐）
 * 2.直接继承WebMvcConfigurationSupport(继承此方法会导致application.properties不生效)
 * WebMvcConfigurerAdapter常用的方法
 * 
// 解决跨域问题 
public void addCorsMappings(CorsRegistry registry) ;

// 添加拦截器
void addInterceptors(InterceptorRegistry registry);

// 这里配置视图解析器 
void configureViewResolvers(ViewResolverRegistry registry);

// 配置内容裁决的一些选项
void configureContentNegotiation(ContentNegotiationConfigurer configurer);

// 视图跳转控制器 
void addViewControllers(ViewControllerRegistry registry);

// 静态资源处理 
void addResourceHandlers(ResourceHandlerRegistry registry);

// 默认静态资源处理器
void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);
 * 
 * */


/**
 * 
 跟自定义 SpringMVC 相关的类和注解主要有如下四个：

WebMvcConfigurerAdapter
WebMvcConfigurer
WebMvcConfigurationSupport
@EnableWebMvc

Spring Boot 1.x 中，自定义 SpringMVC 配置可以通过继承 WebMvcConfigurerAdapter 来实现。
Spring Boot 2.x 中，自定义 SpringMVC 配置可以通过实现 WebMvcConfigurer 接口来完成。
如果在 Spring Boot 中使用继承 WebMvcConfigurationSupport 来实现自定义 SpringMVC 配置，或者在 Spring Boot 中使用了 @EnableWebMvc 注解，都会导致 Spring Boot 中默认的 SpringMVC 自动化配置失效。
在纯 Java 配置的 SSM 环境中，如果我们要自定义 SpringMVC 配置，有两种办法，
第一种就是直接继承自 WebMvcConfigurationSupport 来完成 SpringMVC 配置，
还有一种方案就是实现 WebMvcConfigurer 接口来完成自定义 SpringMVC 配置，
如果使用第二种方式，则需要给 SpringMVC 的配置类上额外添加 @EnableWebMvc 注解，
表示启用 WebMvcConfigurationSupport，这样配置才会生效。
换句话说，在纯 Java 配置的 SSM 中，如果你需要自定义 SpringMVC 配置，你离不开 WebMvcConfigurationSupport ，
所以在这种情况下建议通过继承 WebMvcConfigurationSupport 来实现自动化配置。
*/
