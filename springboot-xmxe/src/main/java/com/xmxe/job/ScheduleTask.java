package com.xmxe.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling // 该注解必须要加
public class ScheduleTask { 
     public void scheduleTest() {
         System.out.println("scheduleTest开始定时执行");
    }
}
