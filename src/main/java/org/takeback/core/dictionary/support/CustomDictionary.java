package org.takeback.core.dictionary.support;

import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.core.dictionary.DictionaryItem;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.converter.ConversionUtils;

public class CustomDictionary extends XMLDictionary
{
  private static final long serialVersionUID = -29426695300041656L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomDictionary.class);
  private String className;
  private String method;
  private String bean;
  
  public String getBean() {
    return this.bean;
  }
  
  public void setBean(String bean) {
    this.bean = bean;
  }
  
  public void setClassName(String clazz) {
    this.className = clazz;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  public String getMethod() {
    return this.method;
  }
  
  public void init()
  {
    try
    {
      Object obj = null;
      if (!StringUtils.isEmpty(this.bean)) {
        obj = ApplicationContextHolder.getBean(this.bean);
      } else {
        obj = Class.forName(this.className).newInstance();
      }
      Method m = obj.getClass().getMethod(this.method, new Class[0]);
      Document newDefineDoc = (Document)m.invoke(obj, new Object[0]);
      List<Element> els = newDefineDoc.getRootElement().selectNodes("//item");
      for (Element el : els) {
        DictionaryItem item = (DictionaryItem)ConversionUtils.convert(el, DictionaryItem.class);
        if ((el.elements("item").size() == 0) && (!"false".equals(el.attributeValue("leaf")))) {
          item.setLeaf(true);
        }
        Element parent = el.getParent();
        if (parent != null) {
          String pKey = parent.attributeValue("key", "");
          item.setProperty("parent", pKey);
        }
        addItem(item);
      }
      setDefineDoc(newDefineDoc);
    } catch (Exception e) {
      LOGGER.error("get custom dic[{}] for class[{}], method[{}] occur error.", new Object[] { this.id, this.className, this.method, e });
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\support\CustomDictionary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */