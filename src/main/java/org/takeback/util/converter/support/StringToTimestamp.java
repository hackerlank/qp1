package org.takeback.util.converter.support;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.takeback.util.converter.ConversionUtils;

public class StringToTimestamp
  implements Converter<String, Timestamp>
{
  public Timestamp convert(String source)
  {
    Date dt = (Date)ConversionUtils.convert(source, Date.class);
    return new Timestamp(dt.getTime());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToTimestamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */