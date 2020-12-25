package com.xmxe.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@Order(1)//定义优先级
@WebFilter(filterName="startFilter",urlPatterns="/*")
public class StartFilter implements Filter{
	// log4j2实现
	// Logger logger = LogManager.getLogger(StartFilter.class);
	// slf4j实现
	Logger logger = LoggerFactory.getLogger(StartFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.startWebsocketOnline();
		logger.info("startFilter开始启动并完成初始化，开启8889端口");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//调用该方法后，表示过滤器经过原来的url请求处理方法
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	
	/**
	 * 启动在线管理服务
	 */
	public void startWebsocketOnline(){
		try {
			OnlineChatServer s = new OnlineChatServer(8889);
			s.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
