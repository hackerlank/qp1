package org.takeback.thirdparty.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;












public class HttpHelper
{
  public static JSONObject getJson(String url)
    throws IOException
  {
    String result = "";
    HttpURLConnection conn = null;
    try {
      URL realUrl = new URL(url);
      conn = (HttpURLConnection)realUrl.openConnection();
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setReadTimeout(5000);
      conn.setConnectTimeout(5000);
      conn.setRequestMethod("GET");
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result = result + line;
      }
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return JSONObject.fromObject(result);
  }
  
  public static JSONObject postForJson(String url, String data, String encoding) throws IOException {
    System.out.println(data);
    String result = "";
    HttpURLConnection conn = null;
    try {
      URL realUrl = new URL(url);
      conn = (HttpURLConnection)realUrl.openConnection();
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setReadTimeout(5000);
      conn.setConnectTimeout(5000);
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.getOutputStream().write(data.getBytes(encoding));
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
      String line;
      while ((line = in.readLine()) != null) {
        result = result + line;
      }
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return JSONObject.fromObject(result);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\thirdparty\support\HttpHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */