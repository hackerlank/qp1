package org.takeback.core.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryIndicator implements Serializable
{
  private static final long serialVersionUID = 2542057660051407081L;
  private String id;
  private String render;
  private String parentKey;
  private Integer slice;
  private List<?> filter;
  private boolean internal;
  private Map<String, String> properties;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getRender() {
    return this.render;
  }
  
  public void setRender(String render) {
    this.render = render;
  }
  
  public String getParentKey() {
    return this.parentKey;
  }
  
  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }
  
  public Integer getSlice() {
    return this.slice;
  }
  
  public void setSlice(Integer slice) {
    this.slice = slice;
  }
  
  public List<?> getFilter() {
    return this.filter;
  }
  
  public void setFilter(String sFilter) {
    this.filter = ((List)org.takeback.util.JSONUtils.parse(sFilter, List.class));
  }
  
  public boolean isInternal() {
    return this.internal;
  }
  
  public void setInternal(boolean internal) {
    this.internal = internal;
  }
  
  public Map<String, String> getProperties() {
    return this.properties;
  }
  
  public void setProperty(String nm, String v) {
    if (this.properties == null) {
      this.properties = new HashMap();
    }
    this.properties.put(nm, v);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\schema\DictionaryIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */