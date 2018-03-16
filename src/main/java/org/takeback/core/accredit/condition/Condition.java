package org.takeback.core.accredit.condition;

import java.io.Serializable;
import java.util.HashMap;
import org.dom4j.Element;
import org.takeback.util.context.Context;

public abstract interface Condition
  extends Serializable
{
  public static final int EXP = 1;
  public static final int OVERRIDE = 2;
  public static final int LIMIT = 3;
  
  public abstract void setDefine(Element paramElement);
  
  public abstract Object run(Context paramContext);
  
  public abstract Element getDefine();
  
  public abstract String getMessage();
  
  public abstract HashMap<String, Object> data();
  
  public abstract int type();
  
  public abstract String getQueryType();
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\condition\Condition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */