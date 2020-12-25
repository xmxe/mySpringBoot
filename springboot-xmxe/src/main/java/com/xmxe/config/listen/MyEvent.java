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
