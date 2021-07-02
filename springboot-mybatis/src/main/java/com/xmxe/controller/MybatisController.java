package com.xmxe.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmxe.entity.User;
import com.xmxe.service.MybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MybatisController {
	@Autowired
	MybatisService mybatisService;

	@RequestMapping("/getUserById")
	@ResponseBody
	public JSONObject getUserById(String userId,String username) {
		//如果加了@RequestParam注解，那么请求url里必须包含这一参数，否则会报400。那么如果允许不传呢？有两种办法：1）使用default值2）使用required值
		User user = mybatisService.getUserById(Integer.valueOf(userId));
		JSONObject json = new JSONObject();
		json.put("user",user);

		return json;
	}
}
