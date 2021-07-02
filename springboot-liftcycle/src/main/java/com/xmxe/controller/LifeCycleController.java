package com.xmxe.controller;

import com.xmxe.comonent.InvokeMethod;
import com.xmxe.config.listen.MyEvent;
import com.xmxe.entity.Book;
import com.xmxe.entity.ResultInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class LifeCycleController {

	@Resource
	ApplicationContext context;

	@RequestMapping(value="/global")//ip:port/global?date=2020-12-12 12:12:12
	public void customGlobal(Date date,Model model){

		System.out.println(date);
		System.out.println(model.getAttribute("global"));
		int a = 1/0;

	}

	@RequestMapping("/index")
	public String index() {
		// 增加监听器 当跳转到首页的时候触发监听器事件
		MyEvent myEvent = new MyEvent(this);
		// 发布事件，这样才能在CustomListener监听到
		context.publishEvent(myEvent);
		return "index";
	}



	@PostMapping("validated")//localhost:8080/validated
	public ResultInfo<Book> validated(@Validated(Book.group2.class) Book book){

		return new ResultInfo<>(200,"请求成功",book);
	}

	@InvokeMethod(param="ppppp")
	public String invoke(String param){
		System.out.println("结果"+param);
		return param;
	}
}
