package org.takeback.core.controller;

import java.io.Serializable;
import java.util.Map;

public abstract interface Configurable
  extends Serializable
{
  public abstract String getId();
  
  public abstract void setId(String paramString);
  
  public abstract void setProperty(String paramString, Object paramObject);
  
  public abstract Object getProperty(String paramString);
  
  public abstract Map<String, Object> getProperties();
  
  public abstract Long getlastModify();
  
  public abstract void setLastModify(Long paramLong);
  
  public abstract <T> T getProperty(String paramString, Class<T> paramClass);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\Configurable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */