package com.xmxe;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)//排除druid快速配置类
//@EnableScheduling
public class TaskSchedulerAppication {
    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerAppication.class,args);
    }
}
