package org.takeback.core.dictionary;

import org.takeback.core.controller.support.AbstractController;

public class DictionaryController extends AbstractController<Dictionary> {
  private static DictionaryController instance;
  
  private DictionaryController() {
    setLoader(new DictionaryLocalLoader());
    instance = this;
  }
  
  public static DictionaryController instance() {
    if (instance == null) {
      instance = new DictionaryController();
    }
    return instance;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\DictionaryController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */