package org.takeback.core.accredit.result;

import org.takeback.core.accredit.list.AccreditList;

public class PositiveResult implements AuthorizeResult
{
  AccreditList contextList;
  
  public void setContextList(AccreditList list)
  {
    this.contextList = list;
  }
  
  public AccreditList getContextList() { return this.contextList; }
  
  public int conditionCount()
  {
    return 0;
  }
  
  public String getAuthorizeValue() {
    return "1111";
  }
  
  public org.takeback.core.accredit.condition.Condition getCondition(String target) {
    return null;
  }
  
  public int getResult() {
    return 1;
  }
  
  public java.util.List<org.takeback.core.accredit.condition.Condition> getAllConditions() {
    return null;
  }
  
  public AuthorizeResult unite(AuthorizeResult cr) {
    if ((cr == null) || (getResult() > cr.getResult())) {
      return this;
    }
    
    return cr;
  }
  
  public String toString()
  {
    return "true";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\result\PositiveResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */