package com.xmxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling
public class TaskSchedulerAppication {
    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerAppication.class,args);
    }
}
