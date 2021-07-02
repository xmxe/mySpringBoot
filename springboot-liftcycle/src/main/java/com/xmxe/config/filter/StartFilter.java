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

	Logger logger = LoggerFactory.getLogger(StartFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("startFilter开始启动并完成初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 进入下一个过滤链
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

		
}
