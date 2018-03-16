package org.takeback.core.controller;

public abstract interface Controller<T extends Configurable>
{
  public abstract T get(String paramString);
  
  public abstract void add(T paramT);
  
  public abstract void reload(String paramString);
  
  public abstract boolean isLoaded(String paramString);
  
  public abstract void reloadAll();
  
  public abstract void setLoader(ConfigurableLoader<T> paramConfigurableLoader);
  
  public abstract ConfigurableLoader<T> getLoader();
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\Controller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */