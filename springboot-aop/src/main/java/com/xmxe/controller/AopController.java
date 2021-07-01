package com.xmxe.controller;

import com.xmxe.config.AopAction;
import com.xmxe.service.AopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AopController {

	@Autowired
	AopService aopService;

	@AopAction//日志注解
	@GetMapping("aop")
	public String aop(){
		try {
			int c = 1/0;
			aopService.aopAnno();
		}catch (Exception e){
			e.printStackTrace();
		}

		return "cc--oo--rr";
	}

}
