package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class CONCAT extends Expression
{
  public CONCAT()
  {
    this.symbol = "concat";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor)
    throws ExprException
  {
    try
    {
      StringBuffer sb = new StringBuffer();
      int i = 1; for (int size = ls.size(); i < size; i++) {
        Object o = ls.get(i);
        if ((o instanceof List)) {
          o = processor.run((List)o);
        }
        sb.append((String)ConversionUtils.convert(o, String.class));
      }
      return sb.toString();
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor)
    throws ExprException
  {
    return "(" + super.toString(ls, processor) + ")";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\CONCAT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */