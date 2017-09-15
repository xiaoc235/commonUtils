package com.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 9)
public final class ToolSpring implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;//声明一个静态变量保存
	private static final Logger _logger = LoggerFactory.getLogger(ToolSpring.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(ToolSpring.applicationContext == null){
			ToolSpring.applicationContext  = applicationContext;
			_logger.info("========ApplicationContext配置成功,在普通类可以通过调用ToolSpring.getAppContext()获取applicationContext对象,applicationContext="+applicationContext+"========");
		}
		
	}
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name){
		return getApplicationContext().getBean(name);
	}

}
