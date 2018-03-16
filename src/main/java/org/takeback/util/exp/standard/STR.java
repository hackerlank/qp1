package org.takeback.util.exp.standard;

import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class STR extends Expression
{
  public STR()
  {
    this.name = "s";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      return ConversionUtils.convert(ls.get(1), String.class);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    return "'" + run(ls, processor) + "'";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\STR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */