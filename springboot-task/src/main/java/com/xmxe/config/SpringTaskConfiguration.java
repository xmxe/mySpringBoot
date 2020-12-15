package com.xmxe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SpringTaskConfiguration {

    //不使用代码配置 在.yml里面配置也可以
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        return threadPoolTaskScheduler;
    }

}