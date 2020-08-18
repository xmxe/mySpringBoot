package com.xmxe.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.core.annotation.Order;


@Order(1)//定义优先级
@WebFilter(filterName="startFilter",urlPatterns="/*")
public class StartFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.startWebsocketOnline();
		System.out.println("startFilter开始启动并完成初始化，开启8889端口");
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
		
		OnlineChatServer s;
		try {
				s = new OnlineChatServer(8889);
				s.start();							
			//System.out.println( "websocket服务器启动,端口" + s.getPort() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
