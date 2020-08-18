package com.xmxe.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmxe.config.quartz.QuartzManager;

@Controller
//@RequestMapping("**.do")
public class IndexController {
    @Autowired
    QuartzManager quartManager;
    
    @RequestMapping("/")
	public String login() {
		Session session = SecurityUtils.getSubject().getSession();  
		Object objmap =  session.getAttribute("user"); 
		 //用户登陆的情况下访问首页直接跳转到index
		if(objmap != null) {
			return "index";
		}
		return "login";
	} 
    
    
	/**
	 * 用户注销
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request,ModelMap mm) throws Exception{
		//shiro销毁登录
		String msg = request.getParameter("msg");
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
		mm.addAttribute("msg", msg);
		return "login";
	}
	
	@RequestMapping("/index")
	public String index() {
		//quartManager.shutdownJobs();
		quartManager.removeJob("scheduler", "scheduler_group", "myTigger", "triggerGroup");
		return "index";
	} 
	
}
