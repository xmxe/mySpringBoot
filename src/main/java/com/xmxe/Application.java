package com.xmxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan//springboot启动类扫描servlet组件(过滤器filter)
//@MapperScan("com.xmxe.dao")
//@ImportResource(value = {"classpath:applicationContext.xml"})如果使用xml配置spring 使用@ImportResource导入配置文件
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
