package org.takeback.chat.utils;

import java.text.DecimalFormat;
import java.util.Date;
import org.joda.time.LocalDateTime;

public class LotteryUtilBean
{
  private static DecimalFormat decimalFormat = new DecimalFormat("000");
  private String currentStage;
  private String[] currentLuckyNumber;
  private String nextStage;
  private boolean canBet = true;
  private LocalDateTime nextOpenTime;
  private long nextOpenRestTime;
  public static final long freezeBetTime = 60L;
  private static Integer SED = Integer.valueOf(10);
  
  public LotteryUtilBean() {
    LocalDateTime now = LocalDateTime.now();
    int hour = now.getHourOfDay();
    String date = now.toString("yyyyMMdd");
    long stage = 0L;
    if ((hour >= 0) && (hour < 2)) {
      LocalDateTime time0 = new LocalDateTime().withTime(0, 0, 0, 0);
      long phase = (now.toDate().getTime() - time0.toDate().getTime()) / 60000L;
      stage = phase / 5L;
      this.nextOpenTime = time0.plusMinutes((int)(5L * (stage + 1L)));
    } else if ((hour >= 2) && (hour < 10)) {
      stage = 23L;
      this.nextOpenTime = LocalDateTime.now().withTime(10, 0, 0, 0);
    } else if ((hour >= 10) && (hour < 22)) {
      LocalDateTime time10 = new LocalDateTime().withTime(10, 0, 0, 0);
      long phase = (now.toDate().getTime() - time10.toDate().getTime()) / 60000L;
      stage = phase / 10L + 24L;
      this.nextOpenTime = time10.plusMinutes((int)(10L * (phase / 10L + 1L)));
    } else if (hour >= 22) {
      LocalDateTime time22 = new LocalDateTime().withTime(22, 0, 0, 0);
      long phase = (now.toDate().getTime() - time22.toDate().getTime()) / 60000L;
      stage = phase / 5L + 96L;
      this.nextOpenTime = time22.plusMinutes((int)(5L * (phase / 5L + 1L)));
    }
    long next = stage + 1L;
    if (stage == 0L) {
      next = 1L;
      stage = 120L;
      this.nextStage = (date + decimalFormat.format(next));
      this.currentStage = (now.minusDays(1).toString("yyyyMMdd") + decimalFormat.format(stage));
    } else {
      this.currentStage = (date + decimalFormat.format(stage));
      this.nextStage = (date + decimalFormat.format(next));
    }
    if (this.nextOpenTime.getHourOfDay() == 2) {
      this.nextOpenTime = this.nextOpenTime.withHourOfDay(10);
    }
    this.nextOpenRestTime = ((this.nextOpenTime.toDate().getTime() - now.toDate().getTime()) / 1000L);
    this.canBet = (this.nextOpenRestTime - 60L > 0L);
  }
  
  public String getCurrentStage() {
    return this.currentStage;
  }
  
  public String getNextStage() {
    return this.nextStage;
  }
  
  public boolean isCanBet() {
    return this.canBet;
  }
  
  public long getNextOpenRestTime() {
    return this.nextOpenRestTime;
  }
  
  public String[] getCurrentLuckyNumber() {
    return this.currentLuckyNumber;
  }
  
  public void setCurrentLuckyNumber(String currentLuckyNumber) {
    this.currentLuckyNumber = currentLuckyNumber.split(",");
  }
  
  public static String getSequence() {
    StringBuffer sb = new StringBuffer(String.valueOf(System.currentTimeMillis()));
    synchronized (SED) {
      Integer localInteger1 = SED;Integer localInteger2 = SED = Integer.valueOf(SED.intValue() + 1);sb.append(localInteger1);
      if (SED.intValue() % 100 == 0) {
        SED = Integer.valueOf(10);
      }
    }
    return sb.toString();
  }
  
  public static void main(String[] args) {
    LotteryUtilBean lub = new LotteryUtilBean();
    System.out.println(lub.getCurrentStage());
    System.out.println(lub.getNextStage());
    
    System.out.println(lub.getNextOpenRestTime());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\LotteryUtilBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */