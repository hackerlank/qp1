package org.takeback.util.exp.standard;

import java.util.HashSet;
import java.util.List;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.Expression;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class IN extends Expression
{
  public IN()
  {
    this.symbol = "in";
  }
  
  public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Object o = processor.run((List)ls.get(1));
      List<?> rang = (List)ls.get(2);
      if (rang.get(0).equals("$")) {
        rang = (List)processor.run(rang);
      }
      HashSet<Object> set = new HashSet();
      set.addAll(rang);
      return Boolean.valueOf(set.contains(o));
    } catch (Exception e) {
      throw new ExprException(e.getMessage());
    }
  }
  
  public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException
  {
    try {
      Object o = ls.get(1);
      StringBuffer sb = new StringBuffer();
      if ((o instanceof List)) {
        List<?> ls1 = (List)o;
        sb.append(processor.toString(ls1));
      } else {
        sb.append((String)o);
      }
      
      sb.append(" ").append(this.symbol).append("(");
      List<?> rang = (List)ls.get(2);
      if (rang.get(0).equals("$")) {
        String s = processor.toString(rang);
        sb.append(s);
      } else {
        int i = 0; for (int size = rang.size(); i < size; i++) {
          if (i > 0) {
            sb.append(",");
          }
          Object r = rang.get(i);
          String s = (String)ConversionUtils.convert(r, String.class);
          if ((r instanceof Number)) {
            sb.append(s);
          } else {
            sb.append("'").append(s).append("'");
          }
        }
      }
      return ")";
    } catch (Exception e) {
      e.printStackTrace();
      throw new ExprException(e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\standard\IN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */