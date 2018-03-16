package org.takeback.core.accredit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Element;
import org.takeback.core.accredit.condition.Condition;
import org.takeback.core.accredit.condition.ConditionFactory;
import org.takeback.core.accredit.list.AccreditList;
import org.takeback.core.accredit.list.BlackList;
import org.takeback.core.accredit.list.WhiteList;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.core.accredit.result.ConditionResult;

public class AccreditStore implements Serializable
{
  private static final long serialVersionUID = 900088790085008371L;
  List<Condition> cds = new ArrayList();
  HashMap<String, AccreditStoreItem> items = new HashMap();
  private AccreditList ls;
  private String acValue = "0000";
  
  public AccreditStore(Element define) {
    if (define == null) {
      return;
    }
    this.acValue = define.attributeValue("acValue", "1111");
    Element it = define.element("items");
    if (it != null) {
      String acType = it.attributeValue("acType", "whitelist");
      if (acType.equals("whitelist")) {
        initWhiteList(it);
      }
      else {
        initBlackList(it);
      }
    }
    List<Element> els = define.selectNodes("conditions/condition");
    for (Element el : els) {
      Condition cd = ConditionFactory.createCondition(el);
      this.cds.add(cd);
    }
  }
  
  private void initBlackList(Element parent) {
    this.ls = new BlackList();
    List<Element> els = parent.elements();
    for (Element el : els) {
      this.ls.add(el.attributeValue("id"), el);
    }
  }
  
  private void initWhiteList(Element parent) {
    this.ls = new WhiteList();
    List<Element> els = parent.elements();
    for (Element el : els)
      if (el.getName().equals("others")) {
        this.ls.add("$others$", el);
      }
      else {
        String id = el.attributeValue("id");
        this.ls.add(id, el);
        this.items.put(id, new AccreditStoreItem(el));
      }
  }
  
  public AuthorizeResult authorize(String id) {
    if (this.ls == null) {
      return AuthorizeResult.NegativeResult;
    }
    AuthorizeResult r = this.ls.authorize(id);
    r.setContextList(this.ls);
    if (r.getResult() != 1) {
      return r;
    }
    AccreditStoreItem item = (AccreditStoreItem)this.items.get(id);
    if (item == null) {
      return r;
    }
    return item.getResult();
  }
  
  public AuthorizeResult getResult()
  {
    ConditionResult r = new ConditionResult();
    r.setAuthorizeValue(this.acValue);
    r.setAllConditions(this.cds);
    return r;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\AccreditStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */