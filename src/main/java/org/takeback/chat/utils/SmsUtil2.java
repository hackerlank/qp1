package org.takeback.chat.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URLDecoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class SmsUtil2
{
  static String url = "http://sms.253.com/msg/";
  static String un = "N3360826";
  static String pw = "cQEm9uVayYad6e";
  static String sign = "【百城网】";
  static String codeTpl = "您的验证码为:${code}";
  
  public static String sendSmsCode(String phoneNo, String code) {
    return send(phoneNo, codeTpl.replace("${code}", code) + sign);
  }
  
  public static void main(String... args) {
    send("13614416609", "SB" + sign);
  }
  
  public static String send(String phone, String msg) {
    HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
    GetMethod method = new GetMethod();
    try {
      URI base = new URI(url, false);
      method.setURI(new URI(base, "send", false));
      method.setQueryString(new NameValuePair[] { new NameValuePair("un", un), new NameValuePair("pw", pw), new NameValuePair("phone", phone), new NameValuePair("rd", "0"), new NameValuePair("msg", msg), new NameValuePair("ex", "") });
      






      int result = client.executeMethod(method);
      if (result == 200) {
        InputStream in = method.getResponseBodyAsStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte['Ѐ'];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
          baos.write(buffer, 0, len);
        }
        System.out.println(URLDecoder.decode(baos.toString(), "UTF-8"));
      } else {
        throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
      }
    } catch (Exception e) {
      method.releaseConnection();
    }
    return "";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\utils\SmsUtil2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */