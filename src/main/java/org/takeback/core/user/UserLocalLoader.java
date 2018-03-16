package org.takeback.core.user;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.util.ApplicationContextHolder;

public class UserLocalLoader implements org.takeback.core.controller.ConfigurableLoader<User>
{
  private static final Logger log = LoggerFactory.getLogger(UserLocalLoader.class);
  private static final String UserQueryHQL = "from User a where a.id = :id and (a.status is null or a.status = '1')";
  private static final String UserRolesQueryHQL = "from UserRoleToken a where a.userid = :id";
  
  public User load(String id)
  {
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean("sessionFactory", SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = ss.createQuery("from User a where a.id = :id and (a.status is null or a.status = '1')");
      q.setString("id", id);
      user = (User)q.uniqueResult();
      if (user == null) {
        return null;
      }
      q = ss.createQuery("from UserRoleToken a where a.userid = :id");
      q.setString("id", id);
      Object urs = q.list();
      for (Object localObject1 = ((List)urs).iterator(); ((Iterator)localObject1).hasNext();) { UserRoleToken ur = (UserRoleToken)((Iterator)localObject1).next();
        user.addUserRoleToken(ur);
      }
      return user;
    } catch (Exception e) {
      User user;
      log.error("load user " + id + " failed", e);
      return null;
    }
    finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\UserLocalLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */