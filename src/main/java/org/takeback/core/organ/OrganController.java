package org.takeback.core.organ;

import org.takeback.core.controller.support.AbstractController;

public class OrganController extends AbstractController<Organization> {
  private static OrganController instance;
  
  public OrganController() {
    setLoader(new OrganLocalLoader());
    instance = this;
  }
  
  public static OrganController instance() {
    if (instance == null) {
      instance = new OrganController();
    }
    return instance;
  }
  
  public static Organization getRoot() {
    return (Organization)instance().get("unit");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\organ\OrganController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */