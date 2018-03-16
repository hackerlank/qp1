package org.takeback.core.accredit.list;

import java.util.HashMap;
import org.takeback.core.accredit.AccreditStore;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.core.accredit.result.ConditionResult;

public class StorgeWhiteList extends WhiteList
{
  private static final long serialVersionUID = -5231699451569983293L;
  
  public AuthorizeResult authorize(String id)
  {
    int p = id.indexOf("/");
    String itemId = "";
    if (p != -1) {
      itemId = id.substring(p + 1);
      id = id.substring(0, p);
    }
    AuthorizeResult r = null;
    if (this.list.containsKey(id)) {
      AccreditStore acs = (AccreditStore)this.list.get(id);
      if (itemId.length() == 0) {
        r = acs.getResult();
      }
      else {
        r = acs.authorize(itemId);
      }
    }
    else if (this.list.containsKey("$others$")) {
      ConditionResult cdr = new ConditionResult();
      org.dom4j.Element el = (org.dom4j.Element)this.list.get("$others$");
      cdr.setAuthorizeValue(el.attributeValue("acValue", "1111"));
      cdr.setContextList(this);
      r = cdr;
    }
    else {
      r = AuthorizeResult.NegativeResult;
    }
    
    r.setContextList(this);
    return r;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\list\StorgeWhiteList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */