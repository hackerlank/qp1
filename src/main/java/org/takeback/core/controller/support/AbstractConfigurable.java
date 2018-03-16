package org.takeback.core.controller.support;

import java.util.HashMap;
import java.util.Map;
import org.takeback.core.controller.Configurable;
import org.takeback.util.converter.ConversionUtils;

public abstract class AbstractConfigurable implements Configurable
{
  private static final long serialVersionUID = 4078730957151852441L;
  protected Long lastModi;
  protected String id;
  protected Map<String, Object> properties;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public void setProperty(String nm, Object v)
  {
    if (this.properties == null) {
      this.properties = new HashMap();
    }
    this.properties.put(nm, v);
  }
  
  public Object getProperty(String nm)
  {
    if (this.properties == null) {
      return null;
    }
    return this.properties.get(nm);
  }
  
  public <T> T getProperty(String nm, Class<T> targetType)
  {
    return (T)ConversionUtils.convert(getProperty(nm), targetType);
  }
  
  public Map<String, Object> getProperties()
  {
    if ((this.properties == null) || (this.properties.size() == 0)) {
      return null;
    }
    return this.properties;
  }
  
  public Long getlastModify()
  {
    return this.lastModi;
  }
  
  public void setLastModify(Long lastModi)
  {
    this.lastModi = lastModi;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\support\AbstractConfigurable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */