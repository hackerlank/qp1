package org.takeback.util.converter.support;

import java.util.Date;
import org.springframework.core.convert.converter.Converter;

public class LongToDate
  implements Converter<Long, Date>
{
  public Date convert(Long source)
  {
    return new Date(source.longValue());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\LongToDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */