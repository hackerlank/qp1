package org.takeback.util.converter.support;

import java.util.Date;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

public class DateToString implements Converter<Date, String>
{
  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  public String convert(Date source)
  {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(source.getTime());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\DateToString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */