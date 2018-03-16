package org.takeback.core.accredit.result;

import java.util.List;
import org.takeback.core.accredit.condition.Condition;
import org.takeback.core.accredit.list.AccreditList;

public abstract interface AuthorizeResult
{
  public static final int UNDERCONDITION = 2;
  public static final int YES = 1;
  public static final int NO = 0;
  public static final AuthorizeResult NegativeResult = new NegativeResult();
  public static final AuthorizeResult PositiveResult = new PositiveResult();
  
  public abstract void setContextList(AccreditList paramAccreditList);
  
  public abstract AccreditList getContextList();
  
  public abstract int getResult();
  
  public abstract int conditionCount();
  
  public abstract Condition getCondition(String paramString);
  
  public abstract List<Condition> getAllConditions();
  
  public abstract String getAuthorizeValue();
  
  public abstract AuthorizeResult unite(AuthorizeResult paramAuthorizeResult);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\result\AuthorizeResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */