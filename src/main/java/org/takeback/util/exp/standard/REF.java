package org.takeback.util.exp.standard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class REF extends Expression
{
  public REF()
  {
    this.symbol = "$";
    this.name = this.symbol;
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      String nm = (String)ls.get(1);
      if (nm.startsWith("%")) {
        nm = nm.substring(1);
      }
      return ContextUtils.get(nm);
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try
    {
      String nm = (String)ls.get(1);
      if (!nm.startsWith("%")) {
        return nm;
      }
      boolean forPreparedStatement = ((Boolean)ContextUtils.get("$exp.forPreparedStatement", Boolean.TYPE)).booleanValue();
      Object o = run(ls, processor);
      
      if (forPreparedStatement)
      {
        Map<String, Object> parameters = (Map)ContextUtils.get("$exp.statementParameters", HashMap.class);
        String key = "arg" + parameters.size();
        parameters.put(key, o);
        return ":" + key;
      }
      String s = (String)ConversionUtils.convert(o, String.class);
      if ((o instanceof Number)) {
        return s;
      }
      return "'" + s + "'";
    }
    catch (Exception e)
    {
      throw new ExprException(e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\REF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */