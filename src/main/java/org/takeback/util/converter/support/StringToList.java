package org.takeback.util.converter.support;

import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.takeback.util.JSONUtils;



public class StringToList
  implements Converter<String, List>
{
  public List convert(String source)
  {
    return (List)JSONUtils.parse(source, List.class);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */