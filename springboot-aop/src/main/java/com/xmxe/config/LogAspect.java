package com.xmxe.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect//表示这是一个切面
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);
    /**
     * 可以统一定义切点
     */
    @Pointcut("@annotation(com.xmxe.config.AopAction)")
    public void pointcut() {}
    /**
     * 可以统一定义切点
     * 第一个 * 表示要拦截的目标方法返回值任意（也可以明确指定返回值类型
     * 第二个 * 表示包中的任意类（也可以明确指定类
     * 第三个 * 表示类中的任意方法
     * 最后面的两个点表示方法参数任意，个数任意，类型任意
     */
    @Pointcut("execution(* com.xmxe.service.*.*(..))")
    public void pointcut2() {}

    /**
     *
     * @param joinPoint 包含了目标方法的关键信息
     * @deprecated @Before 注解表示这是一个前置通知，即在目标方法执行之前执行，注解中，需要填入切点
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        logger.info("@Before==[{}] ",name);
    }

    /**
     * 后置通知
     * @param joinPoint 包含了目标方法的所有关键信息
     * @deprecated @After 表示这是一个后置通知，即在目标方法执行之后执行
     */
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        logger.info("@After==[{}] ",name);
    }

    /**
     * @param joinPoint
     * @deprecated @AfterReturning 表示这是一个返回通知，即有目标方法有返回值的时候才会触发，
     * 该注解中的 returning 属性表示目标方法返回值的变量名，这个需要和参数一一对应吗，
     * 注意：目标方法的返回值类型要和这里方法返回值参数的类型一致，否则拦截不到，
     * 如果想拦截所有（包括返回值为 void），则方法返回值参数可以为 Object
     */
    @AfterReturning(value = "pointcut()",returning = "r")
    public void returing(JoinPoint joinPoint,Integer r) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        logger.info("@AfterReturning--->r===[{}],name===[{}]",r,name);

        //1.获取切入点所在目标对象
        Object targetObj = joinPoint.getTarget();
        logger.info("获取切入点所在目标对象==[{}]",targetObj.getClass().getName());
        // 2.获取切入点方法的名字
        String methodName = joinPoint.getSignature().getName();
        logger.info("切入方法名字==[{}]",methodName);
        // 3. 获取方法上的注解
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            AopAction apiLog = method.getAnnotation(AopAction.class);
            logger.info("切入方法注解==[{}]",apiLog);
        }

        //4. 获取方法的参数
        Object[] args = joinPoint.getArgs();
        for(Object o :args){
            logger.info("切入方法的参数==[{}]",o);
        }
    }

    /**
     * 异常通知
     * @param joinPoint
     * @param e 目标方法所抛出的异常，注意，这个参数必须是目标方法所抛出的异常或者所抛出的异常的父类，只有这样，才会捕获。
     * 如果想拦截所有，参数类型声明为 Exception，如果在@Around中捕获了异常 catch中没有重新抛出新的异常  不会触发AfterThrowing
     */
    @AfterThrowing(value = "pointcut()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,Exception e) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        logger.info("@AfterThrowing--->e===>[{}],name==[{}] ",e.getMessage(),name);

    }

    /**
     * 环绕通知
     * 环绕通知是集大成者，可以用环绕通知实现上面的四个通知，这个方法的核心有点类似于在这里通过反射执行方法
     * @param pjp 相比JoinPoint多个了proceed()方法
     * @return 注意这里的返回值类型最好是 Object ，和拦截到的方法相匹配
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
        Object proceed = null;
        try {

            //这个相当于 method.invoke 方法，我们可以在这个方法的前后分别添加日志，就相当于是前置/后置通知
            proceed = pjp.proceed();
            logger.info("try @Around环绕通知");
        } catch (Throwable throwable) {
        	throwable.printStackTrace();
        	logger.error("catch @Around环绕通知");
        	throw new RuntimeException("为了触发AfterThrowing抛出的异常");
            
        }
        return proceed;
    }
}
