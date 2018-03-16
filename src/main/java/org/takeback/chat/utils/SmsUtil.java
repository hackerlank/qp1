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
  static String testUsername = "a835177944";
  static String testPassword = "a15947861389";
  static String sign = "【模拟红包】";
  static String codeTpl = "您的验证码为:${code}";
  
  public static void main(String[] args) {
    send1("13625974105", "听闻,安溪有个姓李的2XD!");
  }
  


  public static boolean send1(String phone, String content)
  {
    String httpArg = "u=" + testUsername + "&p=" + md5(testPassword) + "&m=" + phone + "&c=" + encodeUrlString(content, "UTF-8") + encodeUrlString(sign, "UTF-8");
    String result = request(httpUrl, httpArg);
    return "0".equals(result);
  }
  
  public static boolean sendSmsCode1(String phoneNo, String code) {
    return send1(phoneNo, codeTpl.replace("${code}", code));
  }
  
  public static String request(String httpUrl, String httpArg) {
    BufferedReader reader = null;
    String result = null;
    StringBuilder sbf = new StringBuilder();
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
  
  public String getSign() {
    return sign;
  }
  
  public void setSign(String sign) {
    sign = sign;
  }
  
  public String getCodeTpl() {
    return codeTpl;
  }
  
  public void setCodeTpl(String codeTpl) {
    codeTpl = codeTpl;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\utils\SmsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */