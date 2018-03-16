package org.takeback.util;

import java.security.MessageDigest;

public final class SHA1
{
  private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  






  private static String getFormattedText(byte[] bytes)
  {
    int len = bytes.length;
    StringBuilder buf = new StringBuilder(len * 2);
    
    for (int j = 0; j < len; j++) {
      buf.append(HEX_DIGITS[(bytes[j] >> 4 & 0xF)]);
      buf.append(HEX_DIGITS[(bytes[j] & 0xF)]);
    }
    return buf.toString();
  }
  
  public static String encode(String str) {
    if (str == null) {
      return null;
    }
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      messageDigest.update(str.getBytes());
      return getFormattedText(messageDigest.digest());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\SHA1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */