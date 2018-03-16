package org.takeback.core.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.takeback.util.JSONUtils;
import org.takeback.util.context.ContextUtils;

public abstract class ApplicationNode implements java.io.Serializable
{
  private static final long serialVersionUID = 5829201367508285016L;
  public static final String MAIN_TYPE = "1";
  public static final String SIGN = "/";
  protected ApplicationNode parent;
  protected Map<String, ApplicationNode> items = new java.util.LinkedHashMap();
  protected String id;
  protected String name;
  protected String iconCls;
  protected int deep;
  private String type;
  private String ref;
  private Map<String, Object> properties;
  
  public <T extends ApplicationNode> List<T> getItems()
  {
    List<T> ls = new java.util.ArrayList();
    java.util.Collection<ApplicationNode> c = this.items.values();
    for (ApplicationNode item : c) {
      if (!"1".equals(item.getType()))
      {

        ls.add(item); }
    }
    return ls;
  }
  
  public void appendChild(ApplicationNode item) {
    item.setParent(this);
    this.items.put(item.getId(), item);
  }
  
  public ApplicationNode getChild(String id) {
    return (ApplicationNode)this.items.get(id);
  }
  
  @JsonIgnore
  public Map<String, ApplicationNode> items() {
    return java.util.Collections.unmodifiableMap(this.items);
  }
  
  public void clearItems() {
    this.items.clear();
  }
  
  protected int getRequestDeep() {
    if (ContextUtils.hasKey("$requestAppNodeDeep")) {
      return ((Integer)ContextUtils.get("$requestAppNodeDeep")).intValue();
    }
    return Integer.MAX_VALUE;
  }
  
  protected String[] getNodePath() {
    int size = this.deep + 1;
    String[] paths = new String[size];
    ApplicationNode item = this;
    while (item != null) {
      paths[item.deep()] = item.getId();
      item = item.getParent();
    }
    return paths;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getIconCls() {
    return this.iconCls;
  }
  
  public void setIconCls(String icon) {
    this.iconCls = icon;
  }
  
  @JsonIgnore
  protected ApplicationNode getParent() {
    return this.parent;
  }
  
  public int deep() {
    return this.deep;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getRef() {
    return this.ref;
  }
  
  public void setRef(String ref) {
    this.ref = ref;
  }
  
  public void setParent(ApplicationNode parent) {
    if (this.parent != null) {
      return;
    }
    this.parent = parent;
    this.deep = (parent.deep() + 1);
  }
  
  public int hashCode() {
    return this.deep * 31 + this.id.hashCode();
  }
  
  public void setProperty(String nm, Object v) {
    if (this.properties == null) {
      this.properties = new HashMap();
    }
    this.properties.put(nm, v);
  }
  
  public Object getProperty(String nm) {
    if (this.properties == null) {
      return null;
    }
    String s = (String)this.properties.get(nm);
    if (StringUtils.isEmpty(s)) {
      return null;
    }
    Object v = null;
    switch (s.charAt(0)) {
    case '%': 
      v = ContextUtils.get(s.substring(1));
      break;
    case '[': 
      v = JSONUtils.parse(s, List.class);
      break;
    case '{': 
      v = JSONUtils.parse(s, HashMap.class);
      break;
    default: 
      v = s;
    }
    
    return v;
  }
  
  public void setProperties(Map<String, Object> ps) {
    this.properties = ps;
  }
  
  public Map<String, Object> getProperties() {
    if ((this.properties == null) || (this.properties.size() == 0)) {
      return null;
    }
    return this.properties;
  }
  
  public String getFullId() {
    if (this.parent != null) {
      return this.parent.getFullId() + "/" + this.id;
    }
    return this.id;
  }
  
  public boolean hasRef() {
    return !StringUtils.isEmpty(this.ref);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\app\ApplicationNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */