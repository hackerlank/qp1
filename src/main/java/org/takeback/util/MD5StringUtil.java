package org.takeback.util;

import java.security.MessageDigest;

public class MD5StringUtil
{
  private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
  
  public static String MD5Encode(byte[] data) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return byteArrayToHexString(md.digest(data));
    }
    catch (Exception localException) {}
    
    return null;
  }
  
  public static String MD5Encode(String origin) {
    String resultString = null;
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteArrayToHexString(md.digest(origin.getBytes()));
    }
    catch (Exception localException) {}
    

    return resultString;
  }
  
  public static String MD5EncodeUTF8(String origin) {
    String resultString = null;
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteArrayToHexString(md.digest(origin.getBytes("UTF-8")));
    }
    catch (Exception localException) {}
    

    return resultString;
  }
  
  private static String byteArrayToHexString(byte[] b) {
    StringBuilder resultSb = new StringBuilder();
    for (int i = 0; i < b.length; i++) {
      resultSb.append(byteToHexString(b[i]));
    }
    return resultSb.toString();
  }
  
  private static String byteToHexString(byte b) {
    int n = b;
    if (n < 0)
      n = 256 + n;
    int d1 = n / 16;
    int d2 = n % 16;
    return hexDigits[d1] + hexDigits[d2];
  }
  
  public static void main(String[] args) {
    System.err.println(MD5Encode("123123张三"));
    System.err.println(MD5EncodeUTF8("123123张三"));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\MD5StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */