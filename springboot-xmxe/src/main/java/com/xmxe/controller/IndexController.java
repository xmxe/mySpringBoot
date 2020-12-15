package com.xmxe.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RequestMapping("**.do")
public class IndexController {
    
    @RequestMapping("/")
	public String login() {
		Subject subject = SecurityUtils.getSubject();
		Object user = subject.getPrincipal();
		if(user != null){
			return "index";
		}
		return "login";
	} 

    
	/**
	 * 用户注销
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
	    return "index";
	} 
	
}
