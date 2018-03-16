package org.takeback.core.controller.support;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.takeback.core.controller.Configurable;
import org.takeback.core.controller.ConfigurableLoader;
import org.takeback.core.resource.ResourceCenter;
import org.takeback.util.BeanUtils;
import org.takeback.util.JSONUtils;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.xml.XMLHelper;

public abstract class AbstractConfigurableLoader<T extends Configurable>
  implements ConfigurableLoader<T>
{
  protected String postfix = ".xml";
  
  public void setPostfix(String postfix) {
    this.postfix = postfix;
  }
  
  public String getPostfix() {
    return this.postfix;
  }
  
  public T load(String id)
  {
    String path = id.replaceAll("\\.", "/") + this.postfix;
    try {
      Resource r = ResourceCenter.load("classpath:", path);
      Document doc = XMLHelper.getDocument(r.getInputStream());
      return createInstanceFormDoc(id, doc, r.lastModified());
    } catch (Exception e) {}
    return null;
  }
  
  public abstract T createInstanceFormDoc(String paramString, Document paramDocument, long paramLong);
  
  protected void setupProperties(Object o, Element el)
  {
    List<Element> ls = el.selectNodes("properties/p");
    try {
      for (Element p : ls) {
        Object value = parseToObject(p.getTextTrim());
        if (value != null)
        {

          BeanUtils.setPropertyInMap(o, p.attributeValue("name"), value); }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private Object parseToObject(String s) {
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
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\support\AbstractConfigurableLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */