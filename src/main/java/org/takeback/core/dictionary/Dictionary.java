package org.takeback.core.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.takeback.core.controller.support.AbstractConfigurable;

public abstract class Dictionary extends AbstractConfigurable
{
  private static final long serialVersionUID = 5186888641454350567L;
  protected HashMap<String, DictionaryItem> items = new LinkedHashMap();
  protected String clazz = "XMLDictionary";
  protected String searchField = "mCode";
  protected String searchFieldEx = "text";
  protected String alias;
  protected boolean isPrivate = false;
  protected boolean queryOnly = false;
  
  protected char searchExSymbol = '.';
  protected char searchKeySymbol = '/';
  

  public Dictionary() {}
  
  public void setClass(String clazz)
  {
    this.clazz = clazz;
  }
  
  public String getCls() {
    return this.clazz;
  }
  
  public Dictionary(String id) {
    this.id = id;
  }
  
  public String getAlias() {
    return this.alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public void addItem(DictionaryItem item) {
    this.items.put(item.getKey(), item);
  }
  
  public void removeItem(String key) {
    this.items.remove(key);
  }
  
  public DictionaryItem getItem(String key) {
    return (DictionaryItem)this.items.get(key);
  }
  
  public boolean keyExist(String key) {
    return this.items.containsKey(key);
  }
  
  public String getText(String key) {
    if (this.items.containsKey(key)) {
      return ((DictionaryItem)this.items.get(key)).getText();
    }
    return "";
  }
  
  public List<String> getKey(String text) {
    List<String> list = new ArrayList();
    for (String key : this.items.keySet()) {
      if (text.equals(((DictionaryItem)this.items.get(key)).getText())) {
        list.add(key);
      }
    }
    return list;
  }
  
  public List<DictionaryItem> itemsList() {
    List<DictionaryItem> ls = new ArrayList();
    for (DictionaryItem di : this.items.values()) {
      ls.add(di);
    }
    return ls;
  }
  
  public boolean isPrivate() {
    return this.isPrivate;
  }
  
  public void setIsPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }
  
  public String getSearchField() {
    return this.searchField;
  }
  
  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }
  
  public String getSearchFieldEx() {
    return this.searchFieldEx;
  }
  
  public boolean isQueryOnly() {
    return this.queryOnly;
  }
  
  public void setQueryOnly(boolean queryOnly) {
    this.queryOnly = queryOnly;
  }
  
  public void setSearchFieldEx(String searchFieldEx) {
    this.searchFieldEx = searchFieldEx;
  }
  
  public char getSearchExSymbol() {
    return this.searchExSymbol;
  }
  
  public void setSearchExSymbol(char searchExSymbol) {
    this.searchExSymbol = searchExSymbol;
  }
  
  public char getSearchKeySymbol() {
    return this.searchKeySymbol;
  }
  
  public void setSearchKeySymbol(char searchKeySymbol) {
    this.searchKeySymbol = searchKeySymbol;
  }
  


  public abstract List<DictionaryItem> getSlice(String paramString1, int paramInt, String paramString2);
  

  public void init() {}
  

  public HashMap<String, DictionaryItem> getItems()
  {
    return this.items;
  }
  
  public void setItems(HashMap<String, DictionaryItem> items) {
    this.items = items;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\Dictionary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */