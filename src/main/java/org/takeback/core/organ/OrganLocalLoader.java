package org.takeback.core.organ;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.takeback.core.controller.support.AbstractConfigurableLoader;
import org.takeback.util.converter.ConversionUtils;

public class OrganLocalLoader extends AbstractConfigurableLoader<Organization>
{
  public OrganLocalLoader()
  {
    this.postfix = ".org";
  }
  

  public Organization createInstanceFormDoc(String id, Document doc, long lastModi)
  {
    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }
    try {
      Organization organ = (Organization)ConversionUtils.convert(root, Organization.class);
      organ.setLastModify(Long.valueOf(lastModi));
      
      List<Element> installedApps = root.selectNodes("installedApps/app");
      for (Element appEl : installedApps) {
        organ.addInstalledApp(appEl.attributeValue("id"));
      }
      parseChilds(organ, root);
      return organ;
    }
    catch (Exception e) {}
    return null;
  }
  
  private void parseChilds(Organization parent, Element el)
  {
    setupProperties(parent, el);
    List<Element> children = el.elements("unit");
    for (Element u : children) {
      Organization unit = (Organization)ConversionUtils.convert(u, Organization.class);
      parent.appendChild(unit);
      parseChilds(unit, u);
    }
  }
  
  private void setupProperties(Organization unit, Element el) {
    List<Element> ls = el.selectNodes("properties/p");
    for (Element p : ls) {
      unit.setProperty(p.attributeValue("name"), p.getTextTrim());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\organ\OrganLocalLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */