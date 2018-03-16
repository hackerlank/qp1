package org.takeback.core.accredit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import org.takeback.core.accredit.condition.Condition;
import org.takeback.core.accredit.condition.ConditionFactory;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.core.accredit.result.ConditionResult;

public class AccreditStoreItem implements Serializable
{
  private static final long serialVersionUID = -6747079386253280933L;
  List<Condition> cds = new ArrayList();
  private String acValue = "0000";
  
  public AccreditStoreItem(Element define) {
    if (define == null) {
      return;
    }
    define.attributeValue("id");
    this.acValue = define.attributeValue("acValue", "1111");
    List<Element> els = define.elements("condition");
    for (Element el : els) {
      Condition cd = ConditionFactory.createCondition(el);
      this.cds.add(cd);
    }
  }
  
  public AuthorizeResult getResult() {
    ConditionResult r = new ConditionResult();
    r.setAuthorizeValue(this.acValue);
    r.setAllConditions(this.cds);
    return r;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\AccreditStoreItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */