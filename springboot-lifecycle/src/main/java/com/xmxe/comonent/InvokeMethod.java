package com.xmxe.comonent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spring启动时执行此注解标记的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeMethod {

	/*
	 * 执行方法的参数
 	 */
	String param() default "" ;
}
