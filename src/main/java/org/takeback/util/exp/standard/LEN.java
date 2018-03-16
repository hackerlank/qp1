package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class LEN extends Expression
{
  public LEN()
  {
    this.symbol = "len";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    Object o = ls.get(1);
    if ((o instanceof List)) {
      o = processor.run((List)o);
    }
    String str = (String)ConversionUtils.convert(o, String.class);
    return Integer.valueOf(str.length());
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    StringBuffer sb = new StringBuffer(this.symbol).append("(");
    Object lso = ls.get(1);
    if ((lso instanceof List)) {
      sb.append(processor.toString((List)lso));
    } else {
      sb.append("'").append((String)ConversionUtils.convert(lso, String.class)).append("'");
    }
    sb.append(")");
    return sb.toString();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\LEN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */