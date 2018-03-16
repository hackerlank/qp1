package org.takeback.util.params.support;

import com.google.common.collect.Maps;
import java.util.Map;
import org.takeback.util.params.Param;
import org.takeback.util.params.ParamLoader;

public class MemeryParamLoader
  implements ParamLoader
{
  protected Map<String, Param> params = Maps.newConcurrentMap();
  
  public String getParam(String parName, String defaultValue, String paramalias)
  {
    Param p = (Param)this.params.get(parName);
    if (p != null) {
      return p.getParamvalue();
    }
    if (defaultValue == null) {
      return null;
    }
    p = new Param(parName, defaultValue, paramalias);
    this.params.put(parName, p);
    return defaultValue;
  }
  
  public String getParam(String parName, String defaultValue)
  {
    return getParam(parName, defaultValue, null);
  }
  
  public String getParam(String parName)
  {
    return getParam(parName, null, null);
  }
  
  public void setParam(String parName, String value)
  {
    Param p = new Param(parName, value);
    this.params.put(parName, p);
  }
  
  public void removeParam(String parName)
  {
    reload(parName);
  }
  
  public void reload(String parName)
  {
    this.params.remove(parName);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\params\support\MemeryParamLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */