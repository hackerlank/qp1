package org.takeback.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware
{
  private static ApplicationContext applicationContext;
  private static boolean devMode = true;
  private static String name;
  
  public void setApplicationContext(ApplicationContext ctx) throws BeansException
  {
    applicationContext = ctx;
  }
  
  public static boolean containBean(String beanName) {
    return applicationContext.containsBean(beanName);
  }
  
  public static Object getBean(String beanName) {
    return applicationContext.getBean(beanName);
  }
  
  public static <T> T getBean(String beanName, Class<T> type) {
    return (T)applicationContext.getBean(beanName, type);
  }
  
  public static <T> T getBean(Class<T> cls) {
    return (T)applicationContext.getBean(cls);
  }
  
  public static boolean isDevMode() {
    return devMode;
  }
  
  public static void setDevMode(boolean devMode) {
    devMode = devMode;
  }
  



  public static String getName()
  {
    return name;
  }
  
  public static void setName(String name) {
    name = name;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\ApplicationContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */