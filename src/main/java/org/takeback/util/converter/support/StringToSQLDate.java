package org.takeback.util.converter.support;

import java.sql.Date;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

public class StringToSQLDate
  implements Converter<String, Date>
{
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  
  public Date convert(String source)
  {
    return new Date(DateTimeFormat.forPattern("yyyy-MM-dd").parseMillis(source));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToSQLDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */