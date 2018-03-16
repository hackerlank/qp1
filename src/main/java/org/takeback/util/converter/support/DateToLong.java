package org.takeback.util.converter.support;

import java.util.Date;
import org.springframework.core.convert.converter.Converter;

public class DateToLong
  implements Converter<Date, Long>
{
  public Long convert(Date source)
  {
    return Long.valueOf(source.getTime());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\DateToLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */