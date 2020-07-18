package com.wisely.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware, InitializingBean {
	
	public static ApplicationContextProvider singleton;
	
	private ApplicationContext applicationContext = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		singleton = this;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
}
