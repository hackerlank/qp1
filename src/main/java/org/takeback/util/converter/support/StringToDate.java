package org.takeback.util.converter.support;

import java.io.PrintStream;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.takeback.util.converter.ConversionUtils;

public class StringToDate implements org.springframework.core.convert.converter.Converter<String, Date>
{
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String DATETIME1_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String DATETIME2_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  private static final String DATETIME3_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  
  public static void main(String[] args)
  {
    String s = "2015-08-02 11:20:33";
    System.out.println(ConversionUtils.convert(s, Date.class));
    
    s = "2015-08-02T11:20:33";
    System.out.println(ConversionUtils.convert(s, Date.class));
  }
  
  public static Date toDate(String s)
  {
    return DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC().parseLocalDate(s).toDate();
  }
  
  public static Date toDatetime(String s) {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC().parseDateTime(s).toDate();
  }
  
  public Date convert(String source)
  {
    if (StringUtils.isEmpty(source)) {
      return null;
    }
    if (StringUtils.contains(source, "T")) {
      if (StringUtils.contains(source, "Z")) {
        if (source.length() == 20) {
          return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZoneUTC().parseDateTime(source).toDate();
        }
        
        return org.joda.time.format.ISODateTimeFormat.dateTime().parseDateTime(source).toDate();
      }
      
      return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZoneUTC().parseDateTime(source).toDate();
    }
    if (StringUtils.contains(source, ":")) {
      return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(source).toDate();
    }
    if (StringUtils.contains(source, "-")) {
      return DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(source).toDate();
    }
    if (StringUtils.equals(source.toLowerCase(), "now")) {
      return new Date();
    }
    if (StringUtils.equals(source.toLowerCase(), "today")) {
      return new DateTime().withTimeAtStartOfDay().toDate();
    }
    if (StringUtils.equals(source.toLowerCase(), "yesterday")) {
      return new LocalDate().minusDays(1).toDate();
    }
    if (StringUtils.equals(source.toLowerCase(), "tomorrow")) {
      return new LocalDate().plusDays(1).toDate();
    }
    
    throw new IllegalArgumentException("Invalid date string value '" + source + "'");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */