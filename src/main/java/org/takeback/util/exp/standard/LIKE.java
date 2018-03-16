package org.takeback.util.exp.standard;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.takeback.util.context.Context;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class LIKE extends Expression
{
  public LIKE()
  {
    this.symbol = "like";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Object lso = ls.get(1);
      String str1 = null;
      if ((lso instanceof List)) {
        str1 = (String)ConversionUtils.convert(processor.run((List)lso), String.class);
      } else {
        str1 = (String)ConversionUtils.convert(lso, String.class);
      }
      
      lso = ls.get(2);
      String str2 = null;
      if ((lso instanceof List)) {
        str2 = (String)ConversionUtils.convert(processor.run((List)lso), String.class);
      } else {
        str2 = (String)ConversionUtils.convert(lso, String.class);
      }
      
      if (!StringUtils.contains(str2, "%")) {
        str2 = str2 + "%";
      }
      
      Pattern pattern = Pattern.compile(str2.replaceAll("%", ".*"));
      return Boolean.valueOf(pattern.matcher(str1).find());
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor)
    throws ExprException
  {
    StringBuffer sb = new StringBuffer();
    Object lso = ls.get(1);
    String str1 = null;
    if ((lso instanceof List)) {
      str1 = processor.toString((List)lso);
    } else {
      str1 = (String)ConversionUtils.convert(lso, String.class);
    }
    sb.append(str1).append(" ").append(this.symbol).append(" ");
    
    lso = ls.get(2);
    String str2 = null;
    
    if ((lso instanceof List)) {
      str2 = processor.toString((List)lso);
      Context ctx = org.takeback.util.context.ContextUtils.getContext();
      Boolean forPreparedStatement = (Boolean)ctx.get("$exp.forPreparedStatement", Boolean.class);
      if ((forPreparedStatement != null) && (forPreparedStatement.booleanValue() == true) && (str2.startsWith(":"))) {
        sb.append(str2);
        HashMap<String, Object> parameters = (HashMap)ctx.get("$exp.statementParameters", HashMap.class);
        String key = str2.substring(1);
        String val = (String)ConversionUtils.convert(parameters.get(key), String.class);
        if (!StringUtils.endsWith(val, "%")) {
          parameters.put(key, val + "%");
        }
      } else {
        if (!str2.startsWith("'")) {
          sb.append("'");
        }
        
        if (str2.endsWith("'")) {
          str2 = str2.substring(0, str2.length() - 1);
        }
        sb.append(str2);
        if (!StringUtils.contains(str2, "%")) {
          sb.append("%");
        }
        sb.append("'");
      }
    } else {
      str2 = (String)ConversionUtils.convert(lso, String.class);
      sb.append("'").append(str2);
      if (!StringUtils.contains(str2, "%")) {
        sb.append("%");
      }
      sb.append("'");
    }
    
    return sb.toString();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\LIKE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */