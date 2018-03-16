package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class SUBSTR extends Expression
{
  public SUBSTR()
  {
    this.symbol = "substring";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      String str = (String)processor.run((List)ls.get(1));
      int start = ((Integer)ConversionUtils.convert(ls.get(2), Integer.class)).intValue();
      if (ls.size() == 4) {
        int end = ((Integer)ConversionUtils.convert(ls.get(3), Integer.class)).intValue();
        return str.substring(start, end);
      }
      return str.substring(start);
    }
    catch (Exception e)
    {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      String str = processor.toString((List)ls.get(1));
      int start = ((Integer)ConversionUtils.convert(ls.get(2), Integer.class)).intValue();
      
      StringBuffer sb = new StringBuffer(this.symbol).append("(").append(str).append(",").append(start);
      if (ls.size() == 4) {
        int end = ((Integer)ConversionUtils.convert(ls.get(3), Integer.class)).intValue();
        sb.append(",").append(end);
      }
      sb.append(")");
      return sb.toString();
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\SUBSTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */