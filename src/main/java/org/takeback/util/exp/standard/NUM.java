package org.takeback.util.exp.standard;

import java.math.BigDecimal;
import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class NUM extends Expression
{
  public NUM()
  {
    this.name = "d";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Number result = Integer.valueOf(0);
      Object lso = ls.get(1);
      if ((lso instanceof List)) {
        result = (Number)ConversionUtils.convert(processor.run((List)lso), Number.class);
      } else
        result = (Number)ConversionUtils.convert(ls.get(1), Number.class);
      int scale;
      if (ls.size() == 3)
        scale = ((Integer)ConversionUtils.convert(ls.get(2), Integer.TYPE)).intValue();
      return Double.valueOf(BigDecimal.valueOf(((Double)ConversionUtils.convert(result, Double.class)).doubleValue())
        .setScale(scale, 4).doubleValue());
    }
    catch (Exception e)
    {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    Number result = Integer.valueOf(0);
    Object lso = ls.get(1);
    if ((lso instanceof List)) {
      result = (Number)ConversionUtils.convert(processor.run((List)lso), Number.class);
    } else {
      result = (Number)ConversionUtils.convert(ls.get(1), Number.class);
    }
    return String.valueOf(result);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\NUM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */