package org.takeback.mvc.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener
  implements HttpSessionListener, HttpSessionAttributeListener
{
  private static final Logger log = LoggerFactory.getLogger(SessionListener.class);
  private static Map<Integer, List<String>> users = Maps.newConcurrentMap();
  
  public static Map<Integer, List<String>> getUsers() {
    return Collections.unmodifiableMap(users);
  }
  
  public static List<String> getUser(int uid) {
    return (List)getUsers().get(Integer.valueOf(uid));
  }
  
  public static int getOnlineNumber() {
    int c = 0;
    for (List<String> sids : users.values()) {
      c += sids.size();
    }
    return c;
  }
  
  public static boolean isOnline(Integer uid) {
    if (users.get(uid) != null) {
      return true;
    }
    return false;
  }
  
  private void login(int uid, String sid) {
    List<String> sids = (List)users.get(Integer.valueOf(uid));
    if (sids == null) {
      sids = Lists.newCopyOnWriteArrayList();
      users.put(Integer.valueOf(uid), sids);
    }
    if (!sids.contains(sid)) {
      sids.add(sid);
    }
    log.info("user {} login with session id {}, he login from {} place, online users is {} now", new Object[] { Integer.valueOf(uid), sid, Integer.valueOf(sids.size()), Integer.valueOf(getOnlineNumber()) });
  }
  
  private void logout(int uid, String sid) {
    List<String> sids = (List)users.get(Integer.valueOf(uid));
    if (sids != null) {
      sids.remove(sid);
      if (sids.size() == 0) {
        users.remove(Integer.valueOf(uid));
      }
    }
    log.info("user {} left, online users is {} now", Integer.valueOf(uid), Integer.valueOf(getOnlineNumber()));
  }
  
  public void attributeAdded(HttpSessionBindingEvent se)
  {
    if ("$uid".equals(se.getName())) {
      int uid = ((Integer)se.getValue()).intValue();
      String sid = se.getSession().getId();
      login(uid, sid);
    }
  }
  
  public void attributeReplaced(HttpSessionBindingEvent se)
  {
    attributeAdded(se);
  }
  


  public void sessionDestroyed(HttpSessionEvent se) {}
  


  public void sessionCreated(HttpSessionEvent se) {}
  


  public void attributeRemoved(HttpSessionBindingEvent se)
  {
    if ("$uid".equals(se.getName())) {
      int uid = ((Integer)se.getValue()).intValue();
      String sid = se.getSession().getId();
      logout(uid, sid);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\listener\SessionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */