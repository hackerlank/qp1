package org.takeback.util.params;

import org.takeback.util.params.support.MemeryParamLoader;





public class ParamUtils
{
  private static ParamLoader paramLoader = new MemeryParamLoader();
  
  public static void setParamLoader(ParamLoader paramLoader) {
    paramLoader = paramLoader;
  }
  
  public static String getParam(String parName, String defaultValue, String paramalias) {
    return paramLoader.getParam(parName, defaultValue, paramalias);
  }
  
  public static String getParam(String parName, String defaultValue) {
    return paramLoader.getParam(parName, defaultValue);
  }
  
  public static String getParam(String parName) {
    return paramLoader.getParam(parName);
  }
  




  public static String getParamSafe(String parName)
  {
    return getParam(parName) == null ? "" : getParam(parName);
  }
  
  public static void setParam(String parName, String value) {
    paramLoader.setParam(parName, value);
  }
  
  public static void removeParam(String parName) {
    paramLoader.removeParam(parName);
  }
  
  public static void reload(String parName) {
    paramLoader.reload(parName);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\params\ParamUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */