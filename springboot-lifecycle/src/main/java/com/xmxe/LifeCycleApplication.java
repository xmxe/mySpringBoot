package com.xmxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.xmxe.config.filter"})//springboot启动类扫描servlet组件(过滤器filter)
public class LifeCycleApplication {
	public static void main(String[] args) {
		SpringApplication.run(LifeCycleApplication.class, args);
	}
}
