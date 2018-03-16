package org.takeback.util.context.beans;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.takeback.util.converter.ConversionUtils;

public class DateBean
{
  public Date parse(String source)
  {
    return (Date)ConversionUtils.convert(source, Date.class);
  }
  
  public Date getToday() {
    LocalDate dt = new LocalDate();
    return dt.toDate();
  }
  
  public Date getDatetime() {
    return new Date();
  }
  
  public Date getNow() {
    return getDatetime();
  }
  
  public Date getTomorrow() {
    LocalDate dt = new LocalDate();
    return dt.plusDays(1).toDate();
  }
  
  public Date getDatetimeOfNextDay() {
    DateTime dt = new DateTime();
    return dt.plusDays(1).toDate();
  }
  
  public Date getDateOfNextMonth() {
    LocalDate dt = new LocalDate();
    return dt.plusMonths(1).toDate();
  }
  
  public Date getDatetimeOfNextMonth() {
    DateTime dt = new DateTime();
    return dt.plusMonths(1).toDate();
  }
  
  public Date getDateOfNextYear() {
    LocalDate dt = new LocalDate();
    return dt.plusYears(1).toDate();
  }
  
  public Date getDatetimeOfNextYear() {
    DateTime dt = new DateTime();
    return dt.plusYears(1).toDate();
  }
  
  public Date getYesterday() {
    LocalDate dt = new LocalDate();
    return dt.minusDays(1).toDate();
  }
  
  public Date getDatetimeOfLastDay() {
    DateTime dt = new DateTime();
    return dt.minusDays(1).toDate();
  }
  
  public Date getDateOfLastMonth() {
    LocalDate dt = new LocalDate();
    return dt.minusMonths(1).toDate();
  }
  
  public Date getDatetimeOfLastMonth() {
    DateTime dt = new DateTime();
    return dt.minusMonths(1).toDate();
  }
  
  public Date getDateOfLastYear() {
    LocalDate dt = new LocalDate();
    return dt.minusYears(1).toDate();
  }
  
  public Date getDatetimeOfLastYear() {
    DateTime dt = new DateTime();
    return dt.minusYears(1).toDate();
  }
  
  public Date getDateOfLastWeek() {
    LocalDate dt = new LocalDate();
    return dt.minusWeeks(1).toDate();
  }
  
  public Date getDatetimeOfLastWeek() {
    DateTime dt = new DateTime();
    return dt.minusWeeks(1).toDate();
  }
  
  public int getYear() {
    LocalDate dt = new LocalDate();
    return dt.getYear();
  }
  
  public int getMonth() {
    LocalDate dt = new LocalDate();
    return dt.getMonthOfYear();
  }
  
  public int getDay() {
    LocalDate dt = new LocalDate();
    return dt.getDayOfMonth();
  }
  
  public int getWeekDay() {
    LocalDate dt = new LocalDate();
    return dt.getDayOfWeek();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\context\beans\DateBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */