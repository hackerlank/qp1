package org.takeback.util;

import java.util.Random;



public class SimpleEncryptUtil
{
  private static final char[] r = { '9', '4', '5', '2', '1', '0', '7', '6', '8' };
  


  private static final char b = '3';
  

  private static final int binLen = r.length;
  


  private static final int s = 6;
  



  public static String toSerialCode(long id)
  {
    char[] buf = new char[32];
    int charPos = 32;
    
    while (id / binLen > 0L) {
      int ind = (int)(id % binLen);
      
      buf[(--charPos)] = r[ind];
      id /= binLen;
    }
    buf[(--charPos)] = r[((int)(id % binLen))];
    
    String str = new String(buf, charPos, 32 - charPos);
    
    if (str.length() < 6) {
      StringBuilder sb = new StringBuilder();
      sb.append('3');
      Random rnd = new Random();
      for (int i = 1; i < 6 - str.length(); i++) {
        sb.append(r[(binLen / i - 1)]);
      }
      str = str + sb.toString();
    }
    return str;
  }
  
  public static long codeToId(String code) {
    char[] chs = code.toCharArray();
    long res = 0L;
    for (int i = 0; i < chs.length; i++) {
      int ind = 0;
      for (int j = 0; j < binLen; j++) {
        if (chs[i] == r[j]) {
          ind = j;
          break;
        }
      }
      if (chs[i] == '3') {
        break;
      }
      if (i > 0) {
        res = res * binLen + ind;
      } else {
        res = ind;
      }
    }
    
    return res;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\SimpleEncryptUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */