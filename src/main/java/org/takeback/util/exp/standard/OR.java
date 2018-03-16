package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class OR extends Expression
{
  public OR()
  {
    this.symbol = "or";
    this.needBrackets = true;
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      int i = 1; for (int size = ls.size(); i < size; i++) {
        boolean r = ((Boolean)processor.run((List)ls.get(i))).booleanValue();
        if (r) {
          return Boolean.valueOf(true);
        }
      }
      return Boolean.valueOf(false);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\OR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */