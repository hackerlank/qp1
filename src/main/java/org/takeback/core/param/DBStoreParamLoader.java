package org.takeback.core.param;

import java.util.Map;
import org.takeback.service.BaseService;
import org.takeback.util.params.Param;
import org.takeback.util.params.support.MemeryParamLoader;

public class DBStoreParamLoader extends MemeryParamLoader
{
  @org.springframework.beans.factory.annotation.Autowired
  private BaseService baseService;
  
  public String getParam(String parName, String defaultValue, String paramalias)
  {
    Param p = (Param)this.params.get(parName);
    if (p != null) {
      return p.getParamvalue();
    }
    p = (Param)this.baseService.getUnique(Param.class, "paramname", parName);
    if (p != null) {
      this.params.put(parName, new Param(parName, p.getParamvalue(), p.getParamalias()));
      return p.getParamvalue();
    }
    if (defaultValue == null) {
      return null;
    }
    p = new Param(parName, defaultValue, paramalias);
    this.params.put(parName, p);
    this.baseService.save(Param.class, p);
    return defaultValue;
  }
  

  public void setParam(String parName, String value)
  {
    super.setParam(parName, value);
    Param param = (Param)this.baseService.getUnique(Param.class, "paramname", parName);
    if (null == param) {
      this.baseService.save(Param.class, new Param(parName, value));
    } else {
      param.setParamvalue(value);
      this.baseService.update(Param.class, param);
    }
  }
  

  public void removeParam(String parName)
  {
    super.removeParam(parName);
    Param p = (Param)this.baseService.getUnique(Param.class, "paramname", parName);
    if (p != null) {
      this.baseService.delete(Param.class, p);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\param\DBStoreParamLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */