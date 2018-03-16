package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class GE extends Expression
{
  public GE()
  {
    this.symbol = ">=";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Object lso = ls.get(1);
      Number v1 = null;
      if ((lso instanceof List)) {
        v1 = (Number)ConversionUtils.convert(processor.run((List)lso), Number.class);
      } else {
        v1 = (Number)ConversionUtils.convert(lso, Number.class);
      }
      int i = 2; for (int size = ls.size(); i < size; i++) {
        Number v2 = null;
        lso = ls.get(i);
        if ((lso instanceof List)) {
          v2 = (Number)ConversionUtils.convert(processor.run((List)lso), Number.class);
        } else {
          v1 = (Number)ConversionUtils.convert(lso, Number.class);
        }
        if (v1.doubleValue() < v2.doubleValue()) {
          return Boolean.valueOf(false);
        }
      }
      return Boolean.valueOf(true);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\GE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */