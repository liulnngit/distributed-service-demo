package com.nbtv.commons.context;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContext {
	
	private static volatile AbstractApplicationContext appContext;
	  private static final String XML_EXPRESSION = "classpath*:applicationContext*.xml";
	  
	  public static synchronized void initConfig(String regularExpression)
	  {
	    if (appContext == null) {
	      if (StringUtils.isEmpty(regularExpression)) {
	        appContext = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
	      } else {
	        appContext = new ClassPathXmlApplicationContext(regularExpression.split("[,\\s]+"));
	      }
	    }
	  }
	  
	  public static void setAppContext(ApplicationContext applicationContext)
	  {
	    appContext = (AbstractApplicationContext)applicationContext;
	  }
	  
	  public static AbstractApplicationContext getAppContext()
	  {
	    if (appContext == null) {
	      initConfig(null);
	    }
	    return appContext;
	  }
	  
	  public static Object getBean(String name)
	  {
	    if (appContext == null) {
	      initConfig(null);
	    }
	    return appContext.getBean(name);
	  }
	  
	  public static void start()
	  {
	    if (appContext == null) {
	      initConfig(null);
	    }
	    appContext.start();
	  }
	  
	  public static synchronized void stop()
	  {
	    if (appContext != null)
	    {
	      appContext.stop();
	      appContext.close();
	      appContext = null;
	    }
	  }
}
