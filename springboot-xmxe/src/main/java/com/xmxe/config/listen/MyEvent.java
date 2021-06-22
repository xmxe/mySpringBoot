package com.xmxe.config.listen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

public class MyEvent extends ApplicationEvent {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Object obj ;

	public MyEvent(Object source) {
		super(source);
		this.obj = source;
	}
	public void printMsg(){
		logger.info("监听到自定义事件了[{}]",obj.toString());
	}

}
/**
 * 除了继承ApplicationEvent之外还可以通过注解@EventListener实现监听，
 * 也可以在application.properties配置context.listener.classes=com.xmxe.config.listen.MyEvent指定监听类
 * [springboot自定义监听事件](https://mp.weixin.qq.com/s/ylmU2rT0JlnYJA9f1w065A)
 */
