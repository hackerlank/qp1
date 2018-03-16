package org.takeback.core.dictionary;

import java.io.Serializable;
import java.util.HashMap;

public class DictionaryItem implements Serializable
{
  private static final long serialVersionUID = -2624948204291546508L;
  private String key;
  private String text;
  private String mCode;
  private boolean leaf;
  private HashMap<String, Object> properties;
  
  public DictionaryItem() {}
  
  public DictionaryItem(String key, String text)
  {
    setKey(key);
    setText(text);
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
    return this.properties.get(nm);
  }
  
  public HashMap<String, Object> getProperties() {
    if ((this.properties != null) && (this.properties.isEmpty())) {
      return null;
    }
    return this.properties;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getText() {
    return this.text;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public String getMCode() {
    return this.mCode;
  }
  
  public void setMCode(String mCode) {
    this.mCode = mCode;
  }
  
  public boolean isLeaf() {
    return this.leaf;
  }
  
  public void setLeaf(boolean leaf) {
    this.leaf = leaf;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\DictionaryItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */