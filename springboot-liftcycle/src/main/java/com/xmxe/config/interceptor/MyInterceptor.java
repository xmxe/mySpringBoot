package com.xmxe.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor{

	Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
	
	/* 该方法将在请求处理之前进行调用，只有该方法返回true，才会继续执行后续的Interceptor和Controller，
	 * 当返回值为true时就会继续调用
	 * 下一个Interceptor的preHandle 方法如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法
	 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.info("拦截器preHandle:{}","请求处理之前调用");
//      HttpSession session = request.getSession(true);//request.getSession(false)等同于 如果当前没有session返回null
        String user = "";
		if(user == null) {
			logger.info("用户未登录:{}","跳转到login页面！");
			//			Thymeleaf不能直接被访问，它严格遵守了MVC，只能被控制器访问
			request.getRequestDispatcher("/").forward(request, response);
			//		    response.sendRedirect("/");
			return false;
		}
		 return true;

    }
	
    /* 该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
     * 可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
//    	logger.info("postHandle:{}","视图渲染解析之前进行调用");
    }
	   
    /* 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，
     * 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。用于进行资源清理。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//    	logger.info("afterCompletion:{}","视图渲染解析之后进行调用");
    }
    
}
