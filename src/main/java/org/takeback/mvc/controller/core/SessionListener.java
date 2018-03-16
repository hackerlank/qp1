package org.takeback.mvc.controller.core;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

public class SessionListener implements javax.servlet.http.HttpSessionListener, javax.servlet.http.HttpSessionAttributeListener
{
  public static final String Anonymous = "Anonymous";
  private static Map<String, Object> users = ;
  
  public static Map<String, Object> getUsers() {
    return users;
  }
  
  public void sessionCreated(HttpSessionEvent se)
  {
    HttpSession session = se.getSession();
    Object uid = session.getAttribute("$token") == null ? "Anonymous" : session.getAttribute("$token");
    users.put(session.getId(), uid);
  }
  
  public void sessionDestroyed(HttpSessionEvent se)
  {
    HttpSession session = se.getSession();
    users.remove(session.getId());
  }
  
  public void attributeAdded(HttpSessionBindingEvent se)
  {
    HttpSession session = se.getSession();
    if (session.getAttribute("$token") != null) {
      users.put(session.getId(), session.getAttribute("$token"));
    }
  }
  


  public void attributeRemoved(HttpSessionBindingEvent se) {}
  

  public void attributeReplaced(HttpSessionBindingEvent se)
  {
    attributeAdded(se);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\core\SessionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */