package org.takeback.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;









public class PayOrderNoGenerator
{
  private static long orderNum = 0L;
  private static String date;
  private static final byte[] sync = new byte[0];
  


  public static String generator(boolean useUUID)
  {
    if (useUUID) {
      return UUID.randomUUID().toString().replace("-", "");
    }
    synchronized (sync) {
      String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      if ((date == null) || (!date.equals(str))) {
        date = str;
        orderNum = 0L;
      }
      orderNum += 1L;
      long orderNo = Long.parseLong(date) * 10000L;
      orderNo += orderNum;
      return orderNo + "";
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\pay\PayOrderNoGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */