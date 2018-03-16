package org.takeback.core.schema;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.takeback.core.controller.support.AbstractConfigurable;


public class Schema
  extends AbstractConfigurable
{
  private static final long serialVersionUID = -271602734048406147L;
  public static final String KEY_GEN_AUTO = "identity";
  public static final String KEY_GEN_ASSIGN = "assigned";
  private String name;
  private String mapping;
  private String sort;
  private String pkey;
  private Map<String, SchemaItem> items = Maps.newLinkedHashMap();
  

  public Schema() {}
  
  public Schema(String id)
  {
    this.id = id;
  }
  
  public void addItem(SchemaItem it) {
    this.items.put(it.getId(), it);
  }
  
  public void removeItem(String id) {
    this.items.remove(id);
  }
  
  public SchemaItem getItem(String id) {
    return (SchemaItem)this.items.get(id);
  }
  
  public List<SchemaItem> getItems() {
    List<SchemaItem> sis = new ArrayList();
    sis.addAll(this.items.values());
    return sis;
  }
  
  public int getSize() {
    return this.items.size();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getMapping() {
    return this.mapping;
  }
  
  public void setMapping(String mapping) {
    this.mapping = mapping;
  }
  
  public String getSort() {
    return this.sort;
  }
  
  public void setSort(String sort) {
    this.sort = sort;
  }
  
  public String getPkey() {
    return this.pkey;
  }
  
  public void setPkey(String pkey) {
    this.pkey = pkey;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\schema\Schema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */