package org.takeback.util.identity;

import java.util.Calendar;

public class ProjectIdGenerator
{
  private static String ORGANIZATION_PREFIX = "SQS";
  


  private static final java.text.FieldPosition HELPER_POSITION = new java.text.FieldPosition(0);
  



  private static final java.text.Format dateFormat = new java.text.SimpleDateFormat("YYMMddHHmm");
  



  private static final java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00");
  



  private static int seq = 0;
  


  private static final int MAX = 99;
  


  public static synchronized String generateSequenceNo()
  {
    Calendar rightNow = Calendar.getInstance();
    StringBuffer sb = new StringBuffer(ORGANIZATION_PREFIX);
    dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
    numberFormat.format(seq, sb, HELPER_POSITION);
    if (seq == 99) {
      seq = 0;
    } else {
      seq += 1;
    }
    return sb.toString();
  }
  
  public static void main(String[] args) {
    for (int i = 0; i < 111; i++) {
      System.out.println(generateSequenceNo());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\identity\ProjectIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */