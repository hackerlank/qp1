package org.takeback.core.accredit.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.takeback.core.accredit.condition.Condition;
import org.takeback.core.accredit.list.AccreditList;

public class ConditionResult implements AuthorizeResult
{
  AccreditList contextList;
  HashMap<String, Condition> cnds = new HashMap();
  String acValue = "";
  
  public void setContextList(AccreditList list) {
    this.contextList = list;
  }
  
  public AccreditList getContextList() { return this.contextList; }
  
  public void addCondition(Condition cd)
  {
    this.cnds.put(cd.getQueryType(), cd);
  }
  
  public void setAuthorizeValue(String acValue) {
    this.acValue = acValue;
  }
  
  public void setAllConditions(List<Condition> cds) {
    for (Condition cd : cds) {
      addCondition(cd);
    }
  }
  
  public List<Condition> getAllConditions() {
    List<Condition> ls = new ArrayList();
    ls.addAll(this.cnds.values());
    return ls;
  }
  
  public int conditionCount() {
    return this.cnds.size();
  }
  
  public String getAuthorizeValue() {
    return this.acValue;
  }
  
  public Condition getCondition(String target) {
    return (Condition)this.cnds.get(target);
  }
  
  public int getResult() {
    return 2;
  }
  
  public AuthorizeResult unite(AuthorizeResult cr) {
    if ((cr == null) || (cr.getResult() != 2)) {
      return this;
    }
    if (cr.conditionCount() > 0) {
      setAllConditions(cr.getAllConditions());
    }
    int i1 = Integer.valueOf(this.acValue, 2).intValue();
    int i2 = Integer.valueOf(cr.getAuthorizeValue(), 2).intValue();
    this.acValue = Integer.toBinaryString(i1 | i2);
    return this;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\result\ConditionResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */