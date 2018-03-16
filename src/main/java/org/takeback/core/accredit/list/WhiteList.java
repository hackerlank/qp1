package org.takeback.core.accredit.list;

import java.io.Serializable;
import java.util.HashMap;
import org.dom4j.Element;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.core.accredit.result.ConditionResult;

public class WhiteList
  implements AccreditList, Serializable
{
  private static final long serialVersionUID = 467436151043085270L;
  HashMap<String, Object> list = new HashMap();
  
  public void add(String id, Object ctx) { this.list.put(id, ctx); }
  
  public AuthorizeResult authorize(String id)
  {
    AuthorizeResult r = null;
    if (this.list.containsKey(id)) {
      r = AuthorizeResult.PositiveResult;
    }
    else if (this.list.containsKey("$others$")) {
      Element el = (Element)this.list.get("$others$");
      String acValue = el.attributeValue("acValue", "");
      if (acValue.length() > 0) {
        ConditionResult cr = new ConditionResult();
        cr.setAuthorizeValue(acValue);
        r = cr;
      } else {
        r = AuthorizeResult.PositiveResult;
      }
    } else {
      r = AuthorizeResult.NegativeResult;
    }
    
    r.setContextList(this);
    return r;
  }
  
  public HashMap<String, Object> containers() {
    return this.list;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\list\WhiteList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */