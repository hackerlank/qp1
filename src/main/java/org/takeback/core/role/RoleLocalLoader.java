package org.takeback.core.role;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.takeback.core.controller.support.AbstractConfigurableLoader;
import org.takeback.util.converter.ConversionUtils;

public class RoleLocalLoader extends AbstractConfigurableLoader<Role>
{
  public RoleLocalLoader()
  {
    this.postfix = ".r";
  }
  

  public Role createInstanceFormDoc(String id, Document doc, long lastModi)
  {
    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }
    try {
      Role r = (Role)ConversionUtils.convert(root, Role.class);
      r.setId(id);
      r.setLastModify(Long.valueOf(lastModi));
      List<Element> els = root.selectNodes("accredit/*");
      for (Element el : els) {
        r.initAccreditList(el);
      }
      setupProperties(r, root);
      return r;
    } catch (Exception e) {}
    return null;
  }
  
  private void setupProperties(Role o, Element el)
  {
    List<Element> ls = el.selectNodes("properties/p");
    for (Element p : ls) {
      o.setProperty(p.attributeValue("name"), p.getTextTrim());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\role\RoleLocalLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */