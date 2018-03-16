package org.takeback.core.accredit.list;

import java.util.HashMap;
import java.util.List;
import org.dom4j.Element;
import org.takeback.core.accredit.result.AuthorizeResult;

public class ApplicationWhiteList
  extends WhiteList
{
  private static final long serialVersionUID = -2788057587279673397L;
  HashMap<String, AccreditList> lss = new HashMap();
  
  public void add(String id, Object ctx) {
    super.add(id, ctx);
    add(id, (Element)ctx);
  }
  
  public void add(String id, Element ctx) {
    int n = ctx.elements().size();
    if (n > 0)
    {
      String acType = ctx.attributeValue("acType", "whitelist");
      AccreditList mls; AccreditList mls; if (acType.equals("whitelist")) {
        mls = new ModuleWhiteList();
      }
      else {
        mls = new BlackList();
      }
      this.lss.put(id, mls);
      List<Element> els = ctx.elements();
      for (int i = 0; i < n; i++) {
        Element el = (Element)els.get(i);
        if (el.getName().equals("others")) {
          mls.add("$others$", el);
        }
        else {
          String moduleId = el.attributeValue("id");
          mls.add(moduleId, el);
        }
      }
    }
  }
  
  public AuthorizeResult authorize(String id) { int p = id.indexOf("/");
    String itemId = "";
    if (p != -1) {
      itemId = id.substring(p + 1);
      id = id.substring(0, p);
    }
    if (this.list.containsKey(id)) {
      if (itemId.length() == 0) {
        return AuthorizeResult.PositiveResult;
      }
      if (this.lss.containsKey(id)) {
        AccreditList mls = (AccreditList)this.lss.get(id);
        return mls.authorize(itemId);
      }
      return AuthorizeResult.NegativeResult;
    }
    if (this.list.containsKey("$others$")) {
      return AuthorizeResult.PositiveResult;
    }
    return AuthorizeResult.NegativeResult;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\list\ApplicationWhiteList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */