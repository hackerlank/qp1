package org.takeback.util.identity;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BalanceIdGenerator
{
  private static String ORGANIZATION_PREFIX = "CZ";
  


  private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
  



  private static final Format dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  



  private static final NumberFormat numberFormat = new DecimalFormat("0000");
  



  private static int seq = 0;
  


  private static final int MAX = 9999;
  


  public static synchronized String generateSequenceNo()
  {
    Calendar rightNow = Calendar.getInstance();
    StringBuffer sb = new StringBuffer(ORGANIZATION_PREFIX);
    dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
    numberFormat.format(seq, sb, HELPER_POSITION);
    if (seq == 9999) {
      seq = 0;
    } else {
      seq += 1;
    }
    return sb.toString();
  }
  
  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      Thread.sleep(50L);
      System.out.println(generateSequenceNo());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\identity\BalanceIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */