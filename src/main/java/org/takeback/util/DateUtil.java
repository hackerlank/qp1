package org.takeback.chat.utils;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.takeback.util.exception.CodedBaseRuntimeException;





public class DateUtil
{
  public static final String SHORT = "yyy-MM-dd";
  public static final String LONG = "yyy-MM-dd HH:mm:ss";
  public static final String VERY_SHORT = "MM-dd HH:mm";
  
  public static String toShortTime(Date date)
  {
    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    return format.format(date);
  }
  
  public static Date toDate(String dateStr) {
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    try {
      return format.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static Date getStartOfToday() {
    Date d = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(14, 0);
    c.set(13, 0);
    c.set(12, 0);
    c.set(11, 0);
    return c.getTime();
  }
  
  public static Date getEndOfToday() {
    Date d = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(13, 59);
    c.set(12, 59);
    c.set(11, 23);
    return c.getTime();
  }
  
  public static Date getStartOfTheDay(String dateStr)
  {
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
    try {
      return format.parse(dateStr);
    }
    catch (Exception e) {
      throw new CodedBaseRuntimeException("错误的时间格式!");
    }
  }
  
  public static Date getStartOfTheDay(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(11, 0);
    c.set(12, 0);
    c.set(13, 0);
    return c.getTime();
  }
  
  public static Date getEndOfTheDay(String dateStr)
  {
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
    try {
      Date d = format.parse(dateStr);
      Calendar c = Calendar.getInstance();
      c.setTime(d);
      c.add(5, 1);
      c.add(14, -1);
      return c.getTime();
    } catch (Exception e) {
      throw new CodedBaseRuntimeException("错误的时间格式!");
    }
  }
  
  public static Date getEndOfTheDay(Date date)
  {
    try
    {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(5, 1);
      c.add(14, -1);
      return c.getTime();
    } catch (Exception e) {
      throw new CodedBaseRuntimeException("错误的时间格式!");
    }
  }
  
  public static void main(String... ags) {
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    System.out.println(format.format(getStartOfToday()));
    System.out.println(format.format(getEndOfToday()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\DateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */