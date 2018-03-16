package org.takeback.core.user;

import org.takeback.core.organ.Organization;
import org.takeback.core.role.Role;
import org.takeback.core.role.RoleController;

public class AccountCenter
{
  public static User getUser(String id)
  {
    return (User)UserController.instance().get(id);
  }
  
  public static Role getRole(String id) {
    return (Role)RoleController.instance().get(id);
  }
  
  public static Organization getOrgan(String id) {
    return (Organization)org.takeback.core.organ.OrganController.instance().get(id);
  }
  
  public static void reloadUser(String id)
  {
    UserController.instance().reload(id);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\AccountCenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */