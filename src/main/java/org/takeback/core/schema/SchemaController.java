package org.takeback.core.schema;

import org.takeback.core.controller.support.AbstractController;

public class SchemaController extends AbstractController<Schema>
{
  private static SchemaController instance;
  
  public SchemaController() {
    setLoader(new SchemaLocalLoader());
    instance = this;
  }
  
  public static SchemaController instance() {
    if (instance == null) {
      instance = new SchemaController();
    }
    return instance;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\schema\SchemaController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */