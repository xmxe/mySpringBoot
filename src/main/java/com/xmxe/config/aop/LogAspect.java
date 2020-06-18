package com.xmxe.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect//表示这是一个切面
public class LogAspect {

    /**
     * 可以统一定义切点
     */
    @Pointcut("@annotation(AopAction)")
    public void pointcut() {

    }
    /**
     * 可以统一定义切点
     * 第一个 * 表示要拦截的目标方法返回值任意（也可以明确指定返回值类型
     * 第二个 * 表示包中的任意类（也可以明确指定类
     * 第三个 * 表示类中的任意方法
     * 最后面的两个点表示方法参数任意，个数任意，类型任意
     */
    @Pointcut("execution(* com.xmxe.service.*.*(..))")
    public void pointcut2() {

    }

    /**
     * @param joinPoint 包含了目标方法的关键信息
     * @Before 注解表示这是一个前置通知，即在目标方法执行之前执行，注解中，需要填入切点
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name + "方法开始执行了... @Before");
    }

    /**
     * 后置通知
     * @param joinPoint 包含了目标方法的所有关键信息
     * @After 表示这是一个后置通知，即在目标方法执行之后执行
     */
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name + "方法执行结束了... @After");
    }

    /**
     * @param joinPoint
     * @AfterReturning 表示这是一个返回通知，即有目标方法有返回值的时候才会触发，
     * 该注解中的 returning 属性表示目标方法返回值的变量名，这个需要和参数一一对应吗，
     * 注意：目标方法的返回值类型要和这里方法返回值参数的类型一致，否则拦截不到，
     * 如果想拦截所有（包括返回值为 void），则方法返回值参数可以为 Object
     */
    @AfterReturning(value = "pointcut()",returning = "r")
    public void returing(JoinPoint joinPoint,Integer r) {
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println(name + "@AfterReturning 方法返回："+r);
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
        System.out.println(name + "@AfterThrowing 方法抛异常了："+e.getMessage());
    }

    /**
     * 环绕通知
     * 环绕通知是集大成者，可以用环绕通知实现上面的四个通知，这个方法的核心有点类似于在这里通过反射执行方法
     * @param pjp
     * @return 注意这里的返回值类型最好是 Object ，和拦截到的方法相匹配
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
        Object proceed = null;
        try {
            //这个相当于 method.invoke 方法，我们可以在这个方法的前后分别添加日志，就相当于是前置/后置通知
            proceed = pjp.proceed();
            System.out.println("try @Around环绕通知");
        } catch (Throwable throwable) {
        	throwable.printStackTrace();
            System.out.println("catch @Around环绕通知");
        }
        return proceed;
    }
}
