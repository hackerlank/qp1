package org.takeback.core.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.takeback.core.controller.Configurable;
import org.takeback.util.converter.ConversionUtils;

public class Application extends ApplicationNode implements Configurable
{
  private static final long serialVersionUID = 3549131476086910545L;
  protected Long lastModi;
  private Integer pageCount;
  private Map<String, String> refMap = new HashMap();
  
  public List<ApplicationNode> getItems() {
    if (this.deep >= getRequestDeep()) {
      return null;
    }
    return super.getItems();
  }
  
  public <T> T getProperty(String nm, Class<T> targetType)
  {
    return (T)ConversionUtils.convert(getProperty(nm), targetType);
  }
  
  public Long getlastModify()
  {
    return this.lastModi;
  }
  
  public void setLastModify(Long lastModi)
  {
    this.lastModi = lastModi;
  }
  
  public Integer getPageCount() {
    if (this.pageCount == null) {
      return Integer.valueOf(0);
    }
    return this.pageCount;
  }
  
  public void setPageCount(Integer pageCount) {
    this.pageCount = pageCount;
  }
  
  @JsonIgnore
  public Map<String, String> getRefItems() {
    return Collections.unmodifiableMap(this.refMap);
  }
  
  public void addRefItem(ApplicationNode node) {
    this.refMap.put(node.getRef(), node.getFullId());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\app\Application.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */