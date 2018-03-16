package org.takeback.util.exp;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.exception.ExprException;

public class ExpressionUtils
{
  public static Number toNumber(Object lso, ExpressionProcessor processor) throws ExprException
  {
    try
    {
      Number v = null;
      if ((lso instanceof List)) {
        v = (Number)ConversionUtils.convert(processor.run((List)lso), Number.class);
      }
      return (Number)ConversionUtils.convert(lso, Number.class);
    }
    catch (Exception e)
    {
      throw new ExprException(e);
    }
  }
  
  public static String toString(Object lso, ExpressionProcessor processor) throws ExprException {
    try {
      String s = null;
      if ((lso instanceof List)) {
        s = processor.toString((List)lso);
      }
      else if ((lso instanceof String)) {
        s = "'" + lso + "'";
      } else {
        s = (String)ConversionUtils.convert(lso, String.class);
        if ((lso instanceof java.util.Date)) {
          return "'" + s + "'";
        }
      }
      
      return s;
    } catch (Exception e) {
      throw new ExprException(e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\ExpressionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */