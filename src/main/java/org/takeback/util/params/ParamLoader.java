package org.takeback.util.params;

public abstract interface ParamLoader
{
  public abstract String getParam(String paramString1, String paramString2, String paramString3);
  
  public abstract String getParam(String paramString1, String paramString2);
  
  public abstract String getParam(String paramString);
  
  public abstract void setParam(String paramString1, String paramString2);
  
  public abstract void removeParam(String paramString);
  
  public abstract void reload(String paramString);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\params\ParamLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */