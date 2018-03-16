package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.PyConverter;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class PY extends Expression
{
  public PY()
  {
    this.symbol = "pingyin";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try
    {
      Object lso = ls.get(1);
      String str = null;
      if ((lso instanceof List)) {
        str = (String)processor.run((List)lso);
      } else {
        str = (String)ConversionUtils.convert(lso, String.class);
      }
      return PyConverter.getFirstLetter(str);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor)
    throws ExprException
  {
    return this.symbol + "(" + processor.toString((List)ls.get(1)) + ")";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\PY.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */