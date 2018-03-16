package org.takeback.util.exp.standard;

import java.util.Date;
import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class DATE extends Expression
{
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try
    {
      Date result = null;
      Object lso = ls.get(1);
      if ((lso instanceof List)) {
        result = (Date)ConversionUtils.convert(processor.run((List)lso), Date.class);
      }
      return (Date)ConversionUtils.convert(ls.get(1), Date.class);
    }
    catch (Exception e)
    {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    return "'" + (String)ConversionUtils.convert(run(ls, processor), String.class) + "'";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\DATE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */