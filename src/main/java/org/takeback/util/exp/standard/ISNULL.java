package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class ISNULL extends Expression
{
  public ISNULL()
  {
    this.name = "isNull";
  }
  
  public Object run(List ls, ExpressionProcessor processor) throws ExprException {
    Object lso = ls.get(1);
    if ((lso instanceof List)) {
      lso = processor.run((List)lso);
    }
    return Boolean.valueOf(lso == null);
  }
  
  public String toString(List ls, ExpressionProcessor processor) throws ExprException {
    Object lso = ls.get(1);
    if ((lso instanceof List)) {
      lso = processor.toString((List)lso);
    }
    StringBuffer sb = new StringBuffer((String)ConversionUtils.convert(lso, String.class));
    sb.append(" is null");
    return sb.toString();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\ISNULL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */