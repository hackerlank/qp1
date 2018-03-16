package org.takeback.core.dictionary.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryItem;


public class XMLDictionary
  extends Dictionary
{
  private static final long serialVersionUID = -596194603210170948L;
  private static final String LEAF_CNDS = "count(./*) = 0";
  private static final String FOLDER_CNDS = "count(./*) > 0";
  private Document defineDoc;
  
  public XMLDictionary() {}
  
  public XMLDictionary(String id)
  {
    setId(id);
  }
  
  public void setDefineDoc(Document doc) {
    this.defineDoc = doc;
  }
  
  @JsonIgnore
  public Document getDefineDoc() {
    return this.defineDoc;
  }
  
  public List<DictionaryItem> getSlice(String parentKey, int sliceType, String query)
  {
    List<DictionaryItem> result = null;
    switch (sliceType) {
    case 2: 
      result = getAllFolder(parentKey, query);
      break;
    
    case 1: 
      result = getAllLeaf(parentKey, query);
      break;
    
    case 3: 
      result = getAllChild(parentKey, query);
      break;
    
    case 5: 
      result = getChildFolder(parentKey, query);
      break;
    
    case 4: 
      result = getChildLeaf(parentKey, query);
      break;
    
    default: 
      result = getAllItems(parentKey, query);
    }
    
    return result;
  }
  
  private void linkQueryXPath(StringBuffer xpath, String query, String exCnd) {
    if (!StringUtils.isEmpty(query)) {
      xpath.append("contains(lower-case(@");
      char first = query.charAt(0);
      if (first == this.searchKeySymbol) {
        xpath.append("key").append("),lower-case('").append(query.substring(1)).append("')");
      } else if (first == this.searchExSymbol) {
        xpath.append(this.searchFieldEx).append("),lower-case('").append(query.substring(1)).append("')");
      } else {
        xpath.append(this.searchField).append("),lower-case('").append(query).append("')");
      }
      xpath.append(")");
      if (!StringUtils.isEmpty(exCnd)) {
        xpath.append(" and ").append(exCnd);
      }
    }
    else if (!StringUtils.isEmpty(exCnd)) {
      xpath.append(exCnd);
    }
  }
  
  private List<DictionaryItem> toDictionaryItemList(List<Element> ls)
  {
    List<DictionaryItem> result = new ArrayList();
    
    for (Element el : ls) {
      String key = el.attributeValue("key");
      result.add(this.items.get(key));
    }
    return result;
  }
  
  private List<DictionaryItem> getAllItems(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']");
    }
    
    if (!StringUtils.isEmpty(query)) {
      xpath.append("//item[");
      linkQueryXPath(xpath, query, null);
      xpath.append("]");
    } else {
      xpath.append("//item");
    }
    
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
  
  private List<DictionaryItem> getAllChild(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']");
    } else {
      xpath.append(".");
    }
    
    if (!StringUtils.isEmpty(query)) {
      xpath.append("/item[");
      linkQueryXPath(xpath, query, null);
      xpath.append("]");
    } else {
      xpath.append("/item");
    }
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
  
  private List<DictionaryItem> getAllLeaf(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']/item[");
    } else {
      xpath.append("//item[");
    }
    linkQueryXPath(xpath, query, "count(./*) = 0");
    xpath.append("]");
    
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
  
  private List<DictionaryItem> getAllFolder(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']//item[");
    } else {
      xpath.append("//item[");
    }
    linkQueryXPath(xpath, query, "count(./*) > 0");
    xpath.append("]");
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
  
  private List<DictionaryItem> getChildFolder(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']/item[");
    } else {
      xpath.append("item[");
    }
    linkQueryXPath(xpath, query, "count(./*) > 0");
    xpath.append("]");
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
  
  private List<DictionaryItem> getChildLeaf(String parentKey, String query)
  {
    if (this.defineDoc == null) {
      return null;
    }
    Element define = this.defineDoc.getRootElement();
    
    StringBuffer xpath = new StringBuffer();
    if (!StringUtils.isEmpty(parentKey)) {
      xpath.append("//item[@key='").append(parentKey).append("']/item[");
    } else {
      xpath.append("item[");
    }
    linkQueryXPath(xpath, query, "count(./*) = 0");
    xpath.append("]");
    List<Element> ls = define.selectNodes(xpath.toString());
    return toDictionaryItemList(ls);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\support\XMLDictionary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */