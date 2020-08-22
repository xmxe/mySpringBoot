package com.xmxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskSchedulerAppication {
    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerAppication.class,args);
    }
}
