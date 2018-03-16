package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class IF extends Expression
{
  public IF()
  {
    this.symbol = "if";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      boolean status = ((Boolean)processor.run((List)ls.get(1))).booleanValue();
      Object result = null;
      if (status) {
        result = ls.get(2);
        if ((result instanceof List)) {
          result = processor.run((List)result);
        }
      } else {
        result = ls.get(3);
        if (!(result instanceof List)) {} }
      return processor.run((List)result);

    }
    catch (Exception e)
    {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      StringBuffer sb = new StringBuffer(processor.toString((List)ls.get(1)));
      sb.append(" ? ");
      
      Object lso = ls.get(2);
      if ((lso instanceof List)) {
        sb.append(processor.toString((List)lso));
      } else {
        sb.append((String)ConversionUtils.convert(lso, String.class));
      }
      sb.append(" : ");
      lso = ls.get(3);
      if ((lso instanceof List)) {
        sb.append(processor.toString((List)ls.get(3)));
      } else {
        sb.append((String)ConversionUtils.convert(lso, String.class));
      }
      return sb.toString();
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\IF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */