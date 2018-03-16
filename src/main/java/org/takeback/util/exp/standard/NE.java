package org.takeback.util.exp.standard;

import java.util.List;
import org.springframework.util.ObjectUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class NE extends Expression
{
  public NE()
  {
    this.symbol = "!=";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Object v1 = ls.get(1);
      if ((v1 instanceof List)) {
        v1 = processor.run((List)v1);
      }
      int i = 2; for (int size = ls.size(); i < size; i++) {
        Object v2 = ls.get(i);
        if ((v2 instanceof List)) {
          v2 = processor.run((List)v2);
        }
        if (ObjectUtils.nullSafeEquals(v1, v2)) {
          return Boolean.valueOf(false);
        }
      }
      return Boolean.valueOf(true);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\NE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */