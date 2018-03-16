package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class DEC extends Expression
{
  public DEC()
  {
    this.symbol = "-";
    this.needBrackets = true;
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Number result = null;
      Object lso = ls.get(1);
      if ((lso instanceof List)) {
        result = (Number)processor.run((List)lso);
      } else {
        result = (Number)ConversionUtils.convert(lso, Number.class);
      }
      
      int i = 2; for (int size = ls.size(); i < size; i++) {
        Number v = null;
        lso = ls.get(i);
        if ((lso instanceof List)) {
          v = (Number)processor.run((List)lso);
        } else {
          v = (Number)ConversionUtils.convert(lso, Number.class);
        }
        result = Double.valueOf(result.doubleValue() - v.doubleValue());
      }
      return result;
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\DEC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */