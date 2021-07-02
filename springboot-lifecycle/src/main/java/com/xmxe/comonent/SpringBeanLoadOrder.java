package com.xmxe.comonent;

import com.xmxe.controller.LifeCycleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component("SpringBeanLoadOrderName")
public class SpringBeanLoadOrder implements BeanNameAware,
											BeanFactoryAware,
											BeanPostProcessor,
											InitializingBean,
											ApplicationContextAware,
											SmartInitializingSingleton,
											DisposableBean {

	private Logger logger = LoggerFactory.getLogger(SpringBeanLoadOrder.class);

	private ApplicationContext applicationContext = null;

	private BeanFactory beanFactory = null;

	private String beanName = "";

	/**
	 * implements BeanNameAware
	 * 获取实现该接口Bean的beanName
	 * order 1
	 */
	@Override
	public void setBeanName(String currentBeanName) {
		beanName = currentBeanName;
		logger.info("order 1 - BeanNameAware.setBeanName,beanName==[{}],applicationContext==[{}]",beanName,applicationContext);
	}

	/**
	 * implements BeanFactoryAware
	 * 获取beanFactory
	 * order 2
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		logger.info("order 2 - BeanFactoryAware.setBeanFactory,beanName==[{}],isSingleton()==[{}],applicationContext==[{}]",beanName,beanFactory.isSingleton(beanName),applicationContext);
	}

	/**
	 * implements BeanPostProcessor
	 * bean初始化的前置方法
	 * order 3
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		logger.info("order 3 - BeanPostProcessor前置方法bean==[{}],beanName==[{}]",bean,beanName);
		return bean;
	}

	/**
	 * implements InitializingBean
	 * bean在它的所有必须属性被BeanFactory设置后，来执行初始化的工作,调用其afterPropertiesSet()方法
	 * order 4
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("order 4 - InitializingBean.afterPropertiesSet,beanName==[{}],applicationCntext==[{}]",beanName,applicationContext);
	}

	/**
	 * implements BeanPostProcess
	 * bean初始化后的后置方法
	 * order 5
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		logger.info("order 5 - BeanPostProcessor后置方法bean==[{}],beanName==[{}]",bean,beanName);
		return bean;
	}

	/**
	 * implements ApplicationContextAware
	 * order 6
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		logger.info("order 6 - ApplicationContextAware.setApplicationContext,beanName==[{}],applicationCntext==[{}]",beanName,applicationContext);
	}

	/**
	 * 当所有单例 bean 都初始化完成以后， Spring的IOC容器会回调该接口的 afterSingletonsInstantiated()方法,
	 * 主要应用场合就是在所有单例 bean 创建完成之后，可以在该回调中做一些事情。
	 * 执行时机在ApplicationContextAware执行之后
	 * order 7
	 */
	@Override
	public void afterSingletonsInstantiated() {
		logger.info("order 7 - SmartInitializingSingleton.afterSingletonsInstantiated,此时beanName==[{}],applicationCntext==[{}]",beanName,applicationContext);
		// 第一个参数type表示要查找的bean的类型
		// includeNonSingletons 是否考虑非单例bean
		// allowEagerInit 是否允许提早初始化
		String[] beanDefinitionsNames = applicationContext.getBeanNamesForType(LifeCycleController.class,false,true);
		for (String beanDefinitionName : beanDefinitionsNames){
//			logger.info("beanDefinitionName==[{}]",beanDefinitionName);
			Object bean = applicationContext.getBean(beanDefinitionName);

			Map<Method,InvokeMethod> annotatedMethods = null;
			try{

				// 查找bean里指定注解的方法
				annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), new MethodIntrospector.MetadataLookup<InvokeMethod>() {
					@Override
					public InvokeMethod inspect(Method method) {
						return AnnotatedElementUtils.findMergedAnnotation(method,InvokeMethod.class);
					}
				});
			}catch (Exception e){
				e.printStackTrace();
			}
			if(annotatedMethods == null || annotatedMethods.isEmpty()){
				continue;
			}

			for(Map.Entry<Method,InvokeMethod> entry : annotatedMethods.entrySet()){
				Method method = entry.getKey();
				InvokeMethod invokeMethod = entry.getValue();
				if(invokeMethod == null){
					continue;
				}
				// 如果要执行的方法有参数的话可以传入
				String param = invokeMethod.param();
				try {
					// 参数1 ：方法所在的类
					// 参数2 ： 执行的方法的参数
					Object result = method.invoke(bean,param);
					logger.info("方法返回结果==[{}]",result);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}

			}

		}
	}

	/**
	 * implements DisposableBean
	 */
	@Override
	public void destroy() throws Exception {
		logger.info("destory bean -- DisposableBean.destory,beanName==[{}],applicationCntext==[{}]",beanName,applicationContext);
	}

}
