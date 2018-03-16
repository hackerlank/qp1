package org.takeback.mvc.controller.core;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.takeback.core.organ.OrganController;
import org.takeback.core.organ.Organization;
import org.takeback.core.resource.ResourceCenter;
import org.takeback.core.role.Role;
import org.takeback.core.role.RoleController;
import org.takeback.core.user.User;
import org.takeback.util.ApplicationContextHolder;

@Component("systemDicProcessor")
public class SystemDicProcessor
{
  public Document getUsers()
  {
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("dic").addAttribute("name", "用户列表");
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean("sessionFactory", SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = ss.createQuery("from User a where a.status is null or a.status = '1'");
      List<User> users = q.list();
      for (User user : users) {
        root.addElement("item").addAttribute("key", user.getId()).addAttribute("text", user.getName());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return doc;
  }
  
  public Document getRoles() {
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("dic").addAttribute("name", "角色列表");
    try {
      Resource r = ResourceCenter.load("roles");
      if (r.exists()) {
        File file = r.getFile();
        String[] rolesName = file.list();
        for (String roleName : rolesName) {
          Role role = (Role)RoleController.instance().get("roles." + StringUtils.substringBeforeLast(roleName, ".r"));
          root.addElement("item").addAttribute("key", role.getId()).addAttribute("text", role.getName());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return doc;
  }
  
  public Document getUnits() {
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("dic").addAttribute("name", "机构列表");
    Organization unit = OrganController.getRoot();
    Collection<Organization> units = unit.getChildren();
    for (Organization u : units) {
      root.addElement("item").addAttribute("key", u.getId()).addAttribute("text", u.getName());
    }
    return doc;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\core\SystemDicProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */