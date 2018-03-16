package org.takeback.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.ExpressionProcessor;


public class StringValueParser
{
  public static boolean isStaticString(String str)
  {
    return (str.charAt(0) != '%') && (str.charAt(0) != '[');
  }
  
  public static <T> T parse(String str, Class<T> type) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    switch (str.charAt(0)) {
    case '%': 
      str = str.trim();
      return (T)ConversionUtils.convert(ContextUtils.get(str.substring(1)), type);
    
    case '[': 
      str = str.trim();
      try {
        List<Object> exp = (List)JSONUtils.parse(str, List.class);
        return (T)ConversionUtils.convert(ExpressionProcessor.instance().run(exp), type);
      } catch (Exception e) {
        throw new IllegalStateException("error config args:" + str);
      }
    }
    return (T)ConversionUtils.convert(str, type);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\StringValueParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */