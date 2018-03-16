package org.takeback.util.converter.support;

import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.takeback.util.JSONUtils;


public class StringToMap
  implements Converter<String, Map>
{
  public Map convert(String source)
  {
    return (Map)JSONUtils.parse(source, Map.class);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */