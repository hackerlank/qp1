package org.takeback.core.app;

import java.util.List;

public class Category extends ApplicationNode {
  private static final long serialVersionUID = 6735979041711571147L;
  
  public List<Module> getModules() {
    if (this.deep >= getRequestDeep()) {
      return null;
    }
    return super.getItems();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\app\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */