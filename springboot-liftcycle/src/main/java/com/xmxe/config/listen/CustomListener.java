package com.xmxe.config.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听器：listener是servlet规范中定义的一种特殊类。
 * 用于监听servletContext、HttpSession和servletRequest等域对象的创建和销毁事件。
 * 监听域对象的属性发生修改的事件。用于在事件发生前、发生后做一些必要的处理。
 * 其主要可用于以下方面：1、统计在线人数和在线用户2、系统启动时加载初始化信息3、统计网站访问量4、记录用户访问路径。
 * 个人理解 虽然不是很像但类似于数据库的触发器 当某一个事件执行后触发另外的事件。 监听器就是在监听某一个事件  当这个事件执行后触发监听动作
 */
@Component
/*public class CustomListener implements ApplicationListener<AvailabilityChangeEvent> {
    Logger logger = LogManager.getLogger(CustomListener.class);
    @Override
    public void onApplicationEvent(AvailabilityChangeEvent availabilityChangeEvent) {
        logger.info("监听到事件：" + availabilityChangeEvent);
        if (ReadinessState.ACCEPTING_TRAFFIC == availabilityChangeEvent.getState()){
            logger.info("应用启动完成，可以请求了……");
        }
    }
}*/
/**
 * Spring Boot 启动事件顺序
 * 1、ApplicationStartingEvent
 * 这个事件在 Spring Boot 应用运行开始时，且进行任何处理之前发送（除了监听器和初始化器注册之外）。
 * 2、ApplicationEnvironmentPreparedEvent
 * 这个事件在当已知要在上下文中使用 Spring 环境（Environment）时，在 Spring 上下文（context）创建之前发送。
 * 3、ApplicationContextInitializedEvent
 * 这个事件在当  Spring 应用上下文（ApplicationContext）准备好了，并且应用初始化器（ApplicationContextInitializers）已经被调用，在 bean 的定义（bean definitions）被加载之前发送。
 * 4、ApplicationPreparedEvent
 * 这个事件是在  Spring 上下文（context）刷新之前，且在 bean 的定义（bean definitions）被加载之后发送。
 * 5、ApplicationStartedEvent
 * 这个事件是在  Spring 上下文（context）刷新之后，且在 application/ command-line runners 被调用之前发送。
 * 6、AvailabilityChangeEvent
 * 这个事件紧随上个事件之后发送，状态：ReadinessState.CORRECT，表示应用已处于活动状态。
 * 7、ApplicationReadyEvent
 * 这个事件在任何 application/ command-line runners 调用之后发送。
 * 8、AvailabilityChangeEvent
 * 这个事件紧随上个事件之后发送，状态：ReadinessState.ACCEPTING_TRAFFIC，表示应用可以开始准备接收请求了。
 * 9、ApplicationFailedEvent
 * 这个事件在应用启动异常时进行发送
 */
public class CustomListener implements ApplicationListener<MyEvent>{

    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        myEvent.printMsg();
    }
}