package org.takeback.core.accredit.list;

import java.util.HashMap;
import org.takeback.core.accredit.result.AuthorizeResult;

public abstract interface AccreditList
{
  public abstract void add(String paramString, Object paramObject);
  
  public abstract AuthorizeResult authorize(String paramString);
  
  public abstract HashMap<String, Object> containers();
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\list\AccreditList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */