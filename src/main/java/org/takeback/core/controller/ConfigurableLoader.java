package org.takeback.core.controller;

public abstract interface ConfigurableLoader<T extends Configurable>
{
  public abstract T load(String paramString);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\ConfigurableLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */