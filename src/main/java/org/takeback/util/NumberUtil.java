package org.takeback.chat.utils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;







public class NumberUtil
{
  public static BigDecimal getDecimalPart(BigDecimal d)
  {
    return d.subtract(d.setScale(0, 1));
  }
  




  public static Integer getDecimalPartSum(BigDecimal d)
  {
    DecimalFormat df = new DecimalFormat("#.00");
    String str = df.format(d);
    String str1 = "" + str.charAt(str.length() - 1);
    String str2 = "" + str.charAt(str.length() - 2);
    Integer i1 = Integer.valueOf(str1);
    Integer i2 = Integer.valueOf(str2);
    
    return Integer.valueOf(i1.intValue() + i2.intValue());
  }
  




  public static Integer getDecimalPartSum4G22(BigDecimal d)
  {
    DecimalFormat df = new DecimalFormat("0.00");
    String str = df.format(d);
    String str1 = "" + str.charAt(str.length() - 1);
    String str2 = "" + str.charAt(str.length() - 2);
    

    String bzstr = str.replace(".", "");
    String mod = "";
    for (int i = 0; i < bzstr.length(); i++) {
      mod = mod + "1";
    }
    if (Integer.valueOf(bzstr).intValue() % Integer.valueOf(mod).intValue() == 0) {
      return Integer.valueOf(13);
    }
    
    if ((str1.equals(str2)) && ("0".equals(str1))) {
      return Integer.valueOf(12);
    }
    
    if (str1.equals(str2)) {
      return Integer.valueOf(11);
    }
    
    Integer i1 = Integer.valueOf(str1);
    Integer i2 = Integer.valueOf(str2);
    
    int point = i1.intValue() + i2.intValue();
    if (point > 10) {
      point %= 10;
    }
    
    return Integer.valueOf(point);
  }
  




  public static Integer getTailPoint(BigDecimal d)
  {
    DecimalFormat df = new DecimalFormat("0.00");
    String str = df.format(d);
    
    String str2 = "" + str.charAt(str.length() - 1);
    
    return Integer.valueOf(str2);
  }
  







  public static Integer getPoint(BigDecimal d)
  {
    Integer n = getDecimalPartSum(d);
    n = Integer.valueOf(n.intValue() % 10);
    if (n.equals(Integer.valueOf(0))) {
      return Integer.valueOf(10);
    }
    return n;
  }
  
  public static BigDecimal round(BigDecimal d) {
    return d.setScale(2, 4);
  }
  
  public static double round(double d) {
    BigDecimal b = new BigDecimal(d);
    return b.setScale(2, 4).doubleValue();
  }
  
  public static String format(double d) {
    BigDecimal b = new BigDecimal(d);
    return b.setScale(2, 4).toString();
  }
  
  public static String format(BigDecimal d) {
    return d.setScale(2, 4).toString();
  }
  
  public static void main(String... args) {
    System.out.println(getTailPoint(new BigDecimal(0.53D)));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\NumberUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */