package org.takeback.chat.schedule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.PK10;
import org.takeback.chat.service.PK10Service;
import org.takeback.chat.utils.DateUtil;
import org.takeback.util.JSONUtils;


public class PK10Schedule
{
  @Autowired
  PK10Service pk10Service;
  static final int CACHE_SIZE = 10;
  private String url = "http://api.caipiaokong.com/lottery/?name=bjpks&format=json&uid=539488&token=eee615c9d2e418a4137ce5d16e5f6c80bb9fd7cb";
  
  public static Queue<PK10> cache = new LinkedBlockingQueue();
  




  public String get(String urlAll, String charset)
  {
    BufferedReader reader = null;
    String result = null;
    StringBuffer sbf = new StringBuffer();
    String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    try {
      URL url = new URL(urlAll);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.setReadTimeout(30000);
      connection.setConnectTimeout(30000);
      connection.setRequestProperty("User-agent", userAgent);
      connection.connect();
      InputStream is = connection.getInputStream();
      reader = new BufferedReader(new InputStreamReader(is, charset));
      String strRead = null;
      while ((strRead = reader.readLine()) != null) {
        sbf.append(strRead);
        sbf.append("\r\n");
      }
      reader.close();
      result = sbf.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public void doStuff() {
    try {
      String urlAll = this.url;
      String charset = "UTF-8";
      String jsonResult = get(urlAll, charset);
      Map newData = (Map)JSONUtils.parse(jsonResult, Map.class);
      if ((newData == null) || (newData.size() == 0))
        return;
      Iterator it = newData.keySet().iterator();
      while (it.hasNext()) {
        String key = (String)it.next();
        if (!exists(key)) {
          addNew(key, (Map)newData.get(key));
        }
      }
    }
    catch (Exception localException) {}
  }
  
  public void addNew(String key, Map<String, String> data)
  {
    if (cache.size() == 10) {
      cache.poll();
    }
    PK10 pk = new PK10();
    pk.setLucky((String)data.get("number"));
    pk.setNumber(key);
    pk.setOpenTime(DateUtil.toDate((String)data.get("dateline")));
    cache.add(pk);
    try {
      this.pk10Service.save(PK10.class, pk);
    }
    catch (Exception localException) {}
  }
  
  public boolean exists(String key)
  {
    for (PK10 pk : cache) {
      if (pk.getNumber().equals(key)) {
        return true;
      }
    }
    return false;
  }
  
  public static void main(String... args) {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\PK10Schedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */