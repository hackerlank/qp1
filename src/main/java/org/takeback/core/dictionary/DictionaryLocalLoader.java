package org.takeback.core.dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.takeback.core.controller.Configurable;
import org.takeback.core.controller.support.AbstractConfigurableLoader;
import org.takeback.core.dictionary.support.XMLDictionary;
import org.takeback.util.converter.ConversionUtils;

public class DictionaryLocalLoader extends AbstractConfigurableLoader<Dictionary>
{
  private static final String DEFAULT_DIC_PACKAGE = "org.takeback.core.dictionary.support.";
  
  public DictionaryLocalLoader()
  {
    this.postfix = ".dic";
  }
  

  private static void setupProperties(Configurable o, Element el)
  {
    List<Element> ls = el.selectNodes("properties/p");
    for (Element p : ls) {
      String nm = p.attributeValue("name");
      List<Attribute> attrs = p.attributes();
      if (attrs.size() > 1) {
        Map<String, Object> map = new HashMap();
        for (Attribute attr : attrs) {
          map.put(attr.getName(), attr.getValue());
        }
        o.setProperty(nm, map);
      } else {
        String v = p.getTextTrim();
        o.setProperty(nm, v);
      }
    }
  }
  
  public static Dictionary parseDocument(String id, Document doc, long lastModi)
  {
    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }
    String className = root.attributeValue("class", "XMLDictionary");
    try {
      if (!className.contains(".")) {
        className = org.apache.commons.lang3.StringUtils.join(new String[] { "org.takeback.core.dictionary.support.", className });
      }
      Class<Dictionary> clz = Class.forName(className);
      Dictionary dic = (Dictionary)ConversionUtils.convert(root, clz);
      dic.setId(id);
      dic.setLastModify(Long.valueOf(lastModi));
      setupProperties(dic, root);
      List<Element> els = root.selectNodes("//item");
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
        dic.addItem(item);
      }
      if ((dic instanceof XMLDictionary)) {
        ((XMLDictionary)dic).setDefineDoc(doc);
      }
      dic.init();
      return dic;
    } catch (Exception e) {
      e.printStackTrace(); }
    return null;
  }
  


  public Dictionary createInstanceFormDoc(String id, Document doc, long lastModi)
  {
    return parseDocument(id, doc, lastModi);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\DictionaryLocalLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */