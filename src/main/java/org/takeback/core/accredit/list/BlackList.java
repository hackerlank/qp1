package org.takeback.core.accredit.list;

import java.io.Serializable;
import java.util.HashMap;
import org.takeback.core.accredit.result.AuthorizeResult;

public class BlackList implements AccreditList, Serializable
{
  private static final long serialVersionUID = 5815488500591951520L;
  private HashMap<String, Object> list = new HashMap();
  
  public void add(String id, Object ctx) { this.list.put(id, ctx); }
  
  public AuthorizeResult authorize(String id)
  {
    AuthorizeResult r = null;
    if (this.list.containsKey(id)) {
      r = AuthorizeResult.NegativeResult;
    } else {
      r = AuthorizeResult.PositiveResult;
    }
    r.setContextList(this);
    return r;
  }
  
  public HashMap<String, Object> containers() {
    return this.list;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\list\BlackList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */