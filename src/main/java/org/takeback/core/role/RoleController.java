package org.takeback.core.role;

import org.takeback.core.controller.support.AbstractController;

public class RoleController extends AbstractController<Role> {
  private static RoleController instance;
  
  public RoleController() {
    setLoader(new RoleLocalLoader());
    instance = this;
  }
  
  public static RoleController instance() {
    if (instance == null) {
      instance = new RoleController();
    }
    return instance;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\role\RoleController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */