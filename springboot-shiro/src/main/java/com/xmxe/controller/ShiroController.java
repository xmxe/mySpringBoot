package com.xmxe.controller;

import com.xmxe.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class ShiroController {

	private ShiroService shiroService;
	@Autowired
	public void setShiroService(ShiroService shiroService) {
		this.shiroService = shiroService;
	}

	@GetMapping("login")
	public String login(){
		return "login";
	}

	@GetMapping("index")
	public String index(){
		return "index";
	}

	@RequiresRoles("user1")//指定角色才可以执行的权限
	@GetMapping("/miss")
	public void miss(HttpServletRequest request, HttpServletResponse response) {
		try{
			System.out.println(111);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequiresPermissions("user:del")//指定拥有此权限的才可以执行
	@RequestMapping(value="/mizz",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> mizz(HttpServletRequest request) {
		Map<String, Object> json = null;
		try{
			System.out.println(222);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 验证码
	 */
	@RequestMapping(value="/code")
	public void generate(HttpServletRequest request,HttpServletResponse response){
		try{
			shiroService.generate(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@RequestMapping(value="/loginCheck")
	@ResponseBody
	public Map<String, Object> loginCheck(HttpServletRequest request) {
		Map<String, Object> json = null;
		try{
			json = shiroService.loginCheck(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 用户注销
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request, ModelMap mm) throws Exception{
		//shiro销毁登录
		String msg = request.getParameter("msg");
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		mm.addAttribute("msg", msg);
		return "login";
	}
}
