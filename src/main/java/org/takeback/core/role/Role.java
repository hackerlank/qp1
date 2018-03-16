package org.takeback.core.role;

import java.util.HashMap;
import java.util.List;
import org.dom4j.Element;
import org.takeback.core.accredit.AccreditStore;
import org.takeback.core.accredit.list.AccreditList;
import org.takeback.core.accredit.list.BlackList;
import org.takeback.core.accredit.list.StorgeWhiteList;
import org.takeback.core.accredit.list.WhiteList;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.core.controller.support.AbstractConfigurable;

public class Role extends AbstractConfigurable
{
  private static final long serialVersionUID = -2219302553517602005L;
  private String name;
  private String desc;
  private String type;
  private String parent;
  private HashMap<String, AccreditList> accredits = new HashMap();
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return this.desc;
  }
  
  public void setDescription(String desc) {
    this.desc = desc;
  }
  
  public String getType() {
    return this.type != null ? this.type : "";
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public Role getParent() {
    if (!org.apache.commons.lang3.StringUtils.isEmpty(this.parent)) {
      return org.takeback.core.user.AccountCenter.getRole(this.parent);
    }
    return null;
  }
  
  public void setParent(String parent)
  {
    this.parent = parent;
  }
  
  public void initAccreditList(Element el) {
    String acType = el.attributeValue("acType", "whitelist");
    if (acType.equals("whitelist")) {
      String nm = el.getName();
      if (nm.equals("storage")) {
        initStorageWhiteList(el);
      }
      else if (nm.equals("apps")) {
        initAppsWhiteList(el);
      }
      else {
        initWhiteList(el);
      }
    }
    else {
      initBlackList(el);
    }
  }
  
  private void initAppsWhiteList(Element parent) {
    WhiteList White = new org.takeback.core.accredit.list.ApplicationWhiteList();
    List<Element> items = parent.elements();
    for (Element el : items) {
      if (el.getName().equals("others")) {
        White.add("$others$", el);
      }
      else
        White.add(el.attributeValue("id"), el);
    }
    this.accredits.put(parent.getName(), White);
  }
  
  private void initStorageWhiteList(Element parent) {
    StorgeWhiteList White = new StorgeWhiteList();
    List<Element> items = parent.elements();
    for (Element el : items) {
      if (el.getName().equals("others")) {
        White.add("$others$", el);
      }
      else
        White.add(el.attributeValue("id"), new AccreditStore(el));
    }
    this.accredits.put(parent.getName(), White);
  }
  
  private void initWhiteList(Element parent) {
    WhiteList White = new WhiteList();
    List<Element> items = parent.elements();
    for (Element el : items) {
      if (el.getName().equals("others")) {
        White.add("$others$", el);
      }
      else
        White.add(el.attributeValue("id"), el);
    }
    this.accredits.put(parent.getName(), White);
  }
  
  private void initBlackList(Element parent) {
    BlackList black = new BlackList();
    List<Element> items = parent.elements();
    for (Element el : items) {
      black.add(el.attributeValue("id"), el);
    }
    this.accredits.put(parent.getName(), black);
  }
  
  public AccreditList getAccreditList(String name) {
    return (AccreditList)this.accredits.get(name);
  }
  






  public AuthorizeResult authorize(String name, String id)
  {
    AuthorizeResult p = null;
    Role parentRole = getParent();
    if (parentRole != null) {
      p = parentRole.authorize(name, id);
    }
    AuthorizeResult r = null;
    if (!this.accredits.containsKey(name)) {
      r = new org.takeback.core.accredit.result.NegativeResult();
      return r.unite(p);
    }
    AccreditList ls = (AccreditList)this.accredits.get(name);
    r = ls.authorize(id);
    return r.unite(p);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\role\Role.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */