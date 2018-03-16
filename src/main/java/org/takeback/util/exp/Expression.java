package org.takeback.util.exp;

import java.util.List;
import org.takeback.util.exp.exception.ExprException;

public abstract class Expression
{
  protected String symbol;
  protected String name;
  protected boolean needBrackets = false;
  
  public abstract Object run(List<?> paramList, ExpressionProcessor paramExpressionProcessor) throws ExprException;
  
  public String getName() {
    if (this.name != null) {
      return this.name;
    }
    this.name = getClass().getSimpleName().toLowerCase();
    
    return this.name;
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException {
    try {
      StringBuffer sb = new StringBuffer();
      if (this.needBrackets) {
        sb.append("(");
      }
      int i = 1; for (int size = ls.size(); i < size; i++) {
        if (i > 1) {
          sb.append(" ").append(this.symbol).append(" ");
        }
        Object lso = ls.get(i);
        sb.append(ExpressionUtils.toString(lso, processor));
      }
      if (this.needBrackets) {
        sb.append(")");
      }
      return sb.toString();
    } catch (Exception e) {
      throw new ExprException(e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */