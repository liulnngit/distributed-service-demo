package com.nbtv.commons.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static Log log = LogFactory.getLog(SpringContextUtil.class);
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		applicationContext = applicationContext;
		log.info("setting application!!!");
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}
}
