package org.takeback.core.user;

import org.takeback.core.controller.support.AbstractController;

public class UserController extends AbstractController<User> {
  private static UserController instance;
  
  public UserController() {
    setLoader(new UserLocalLoader());
    instance = this;
  }
  
  public static UserController instance() {
    if (instance == null) {
      instance = new UserController();
    }
    return instance;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\UserController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */