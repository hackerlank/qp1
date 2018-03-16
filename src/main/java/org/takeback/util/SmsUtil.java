package org.takeback.chat.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class SmsUtil
{
  static String httpUrl = "http://api.smsbao.com/sms";
  static String testUsername = "test02";
  static String testPassword = "test02";
  static String sign = "【疯狂的红包】";
  
  public static void main(String[] args) {
    send("13625974105", "听闻,安溪有个姓李的2XD!");
  }
  
  public static boolean send(String phone, String content) {
    StringBuffer httpArg = new StringBuffer();
    httpArg.append("u=").append(testUsername).append("&");
    httpArg.append("p=").append(md5(testPassword)).append("&");
    httpArg.append("m=").append(phone).append("&");
    httpArg.append("c=").append(encodeUrlString(content, "UTF-8")).append(encodeUrlString(sign, "UTF-8"));
    String result = request(httpUrl, httpArg.toString());
    if ("0".equals(result)) {
      return true;
    }
    return false;
  }
  
  public static String request(String httpUrl, String httpArg) {
    BufferedReader reader = null;
    String result = null;
    StringBuffer sbf = new StringBuffer();
    httpUrl = httpUrl + "?" + httpArg;
    try
    {
      URL url = new URL(httpUrl);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      InputStream is = connection.getInputStream();
      reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String strRead = reader.readLine();
      if (strRead != null) {
        sbf.append(strRead);
        while ((strRead = reader.readLine()) != null) {
          sbf.append("\n");
          sbf.append(strRead);
        }
      }
      reader.close();
      result = sbf.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public static String md5(String plainText) {
    StringBuffer buf = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(plainText.getBytes());
      byte[] b = md.digest();
      
      buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        int i = b[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return buf.toString();
  }
  
  public static String encodeUrlString(String str, String charset) {
    String strret = null;
    if (str == null)
      return str;
    try {
      strret = URLEncoder.encode(str, charset);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return strret;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\SmsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */