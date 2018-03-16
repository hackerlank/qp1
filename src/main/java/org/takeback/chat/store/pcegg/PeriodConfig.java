package org.takeback.chat.store.pcegg;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;








public class PeriodConfig
{
  private LocalTime beginTime;
  private LocalTime endTime;
  private int periodSeconds;
  private String dataSourceUrl;
  private String lastNo;
  
  public PeriodConfig(String config)
  {
    String[] tmp = config.split("->");
    setPeriodSeconds(tmp[1]);
    this.dataSourceUrl = tmp[2];
    
    tmp = tmp[0].split("~");
    this.beginTime = parseTime(tmp[0]);
    this.endTime = parseTime(tmp[1]);
  }
  




  public boolean match(LocalTime time)
  {
    DateTime dateTime = time.toDateTimeToday();
    DateTime beginDateTime = this.beginTime.toDateTimeToday();
    DateTime endDateTime = this.endTime.toDateTimeToday();
    if ((dateTime.isAfter(endDateTime)) && (endDateTime.isBefore(beginDateTime))) {
      endDateTime = endDateTime.plusDays(1);
    } else if ((dateTime.isBefore(endDateTime)) && (endDateTime.isBefore(beginDateTime))) {
      beginDateTime = beginDateTime.plusDays(-1);
    }
    return (dateTime.compareTo(beginDateTime) >= 0) && (dateTime.compareTo(endDateTime) < 0);
  }
  
  public int getPeriodSeconds() {
    return this.periodSeconds;
  }
  
  public String getDataSourceUrl() {
    return this.dataSourceUrl;
  }
  
  private void setPeriodSeconds(String periodConfig) {
    char periodUnit = periodConfig.charAt(periodConfig.length() - 1);
    String periodString = periodConfig.substring(0, periodConfig.length() - 1);
    switch (periodUnit) {
    case 'h': 
      this.periodSeconds = (Integer.parseInt(periodString) * 60 * 60);
      break;
    case 'm': 
      this.periodSeconds = (Integer.parseInt(periodString) * 60);
      break;
    case 's': 
      this.periodSeconds = Integer.parseInt(periodString);
      break;
    default: 
      throw new IllegalArgumentException("Invalid unit of period: " + periodUnit);
    }
  }
  
  private LocalTime parseTime(String timeConfig) {
    String[] tmp = timeConfig.split(":");
    LocalTime time;
    if (tmp.length == 2) {
      time = new LocalTime(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), 0); } else { LocalTime time;
      if (tmp.length == 3) {
        time = new LocalTime(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
      } else
        throw new IllegalArgumentException("Invalid time configuration: " + timeConfig);
    }
    LocalTime time;
    return time;
  }
  
  public String getLastNo() {
    return this.lastNo;
  }
  
  public void setLastNo(String lastNo) {
    this.lastNo = lastNo;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\pcegg\PeriodConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */