package org.takeback.core.app;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.takeback.core.controller.support.AbstractConfigurableLoader;
import org.takeback.core.resource.ResourceCenter;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.xml.XMLHelper;

public class ApplicationLocalLoader
  extends AbstractConfigurableLoader<Application>
{
  public ApplicationLocalLoader()
  {
    this.postfix = ".app";
  }
  
  public Application createInstanceFormDoc(String id, Document doc, long lastModi)
  {
    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }
    try {
      Application app = (Application)ConversionUtils.convert(root, Application.class);
      app.setId(id);
      app.setLastModify(Long.valueOf(lastModi));
      
      setupProperties(app, root);
      parseChilds(app, root);
      return app;
    } catch (Exception e) {}
    return null;
  }
  

  private void parseChilds(ApplicationNode appNode, Element el)
  {
    List<Element> ls = el.elements("catagory");
    for (Element catEl : ls) {
      String ref = catEl.attributeValue("ref", "");
      if (!StringUtils.isEmpty(ref)) {
        catEl = loadRefNode(ref, catEl);
      }
      Category category = (Category)ConversionUtils.convert(catEl, Category.class);
      appNode.appendChild(category);
      setupProperties(category, catEl);
      parseRefNode(category);
      parseChilds(category, catEl);
    }
    
    ls = el.elements("module");
    for (Element modEl : ls) {
      String ref = modEl.attributeValue("ref", "");
      if (!StringUtils.isEmpty(ref)) {
        modEl = loadRefNode(ref, modEl);
      }
      Module mod = (Module)ConversionUtils.convert(modEl, Module.class);
      appNode.appendChild(mod);
      setupProperties(mod, modEl);
      parseRefNode(mod);
      parseChilds(mod, modEl);
    }
    
    ls = el.elements("action");
    for (Element actEl : ls) {
      Action action = (Action)ConversionUtils.convert(actEl, Action.class);
      appNode.appendChild(action);
    }
  }
  
  private void parseRefNode(ApplicationNode node)
  {
    ApplicationNode it = node.getParent();
    if (it != null) {
      if ((it.hasRef()) && (!node.hasRef())) {
        node.setRef(it.getRef() + "/" + node.getId());
      }
      if (node.hasRef()) {
        while (it.getParent() != null) {
          it = it.getParent();
        }
        ((Application)it).addRefItem(node);
      }
    }
  }
  
  private Element loadRefNode(String ref, Element el)
  {
    Element node = null;
    if (StringUtils.isEmpty(ref)) {
      return null;
    }
    String[] nodes = ref.split("/");
    String path; if (nodes.length > 1) {
      path = nodes[0].replaceAll("\\.", "/") + this.postfix;
      try {
        Resource r = ResourceCenter.load("classpath:", path);
        Document doc = XMLHelper.getDocument(r.getInputStream());
        node = doc.getRootElement();
        for (int i = 1; i < nodes.length; i++) {
          node = (Element)node.selectSingleNode("*[@id='" + nodes[i] + "']");
          if (node == null) {
            break;
          }
        }
      } catch (Exception e) {
        return null;
      }
    }
    if (node == null) {
      return null;
    }
    for (Attribute att : el.attributes()) {
      node.addAttribute(att.getName(), att.getValue());
    }
    return node;
  }
  
  private void setupProperties(ApplicationNode o, Element el)
  {
    List<Element> ls = el.selectNodes("properties/p");
    for (Element p : ls) {
      o.setProperty(p.attributeValue("name"), p.getTextTrim());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\app\ApplicationLocalLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */