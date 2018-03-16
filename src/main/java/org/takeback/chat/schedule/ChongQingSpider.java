package org.takeback.chat.schedule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.LotteryLog;
import org.takeback.chat.service.LotteryOpenService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.JSONUtils;

public class ChongQingSpider
{
  @Autowired
  private BaseDAO dao;
  @Autowired
  private LotteryOpenService lotteryOpenService;
  @Autowired
  private org.takeback.chat.service.LotteryService lotteryService;
  static final int BEAT = 2;
  static final int MAX = 120;
  static final int CACHE_SIZE = 10;
  private String url = "http://api.caipiaokong.com/lottery/?name=cqssc&format=json&uid=176977&token=838ce074b53cdcdb5123ea8310140a50f2b87c98";
  private Integer currentOpen;
  private Boolean init = Boolean.valueOf(true);
  public static List<LotteryLog> cache = new ArrayList();
  



  public static LotteryLog getLastLog()
  {
    return (LotteryLog)cache.get(0);
  }
  
  public static String getLuckyNum(String sscNum) {
    for (int i = 0; i < cache.size(); i++) {
      LotteryLog ll = (LotteryLog)cache.get(i);
      if (ll.getId().equals(sscNum)) {
        return ll.getLuckyNumber();
      }
    }
    return null;
  }
  





  private boolean checkCache(String sscNum)
  {
    for (int i = 0; i < cache.size(); i++) {
      LotteryLog l = (LotteryLog)cache.get(i);
      if (l.getId().equals(sscNum))
        return true;
    }
    return false;
  }
  



  private void addCache(LotteryLog log)
  {
    if (cache.size() == 10)
    {
      cache.remove(cache.size() - 1);
    }
    
    if (cache.size() > 0) {
      LotteryLog localLotteryLog = (LotteryLog)cache.get(0);
    }
    cache.add(log);
    System.out.println("抓取到开奖号码：" + log);
    Collections.sort(cache);
  }
  



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
  


  @Transactional
  public void doStuff()
  {
    String urlAll = this.url;
    String charset = "UTF-8";
    String jsonResult = get(urlAll, charset);
    Map newData = (Map)JSONUtils.parse(jsonResult, Map.class);
    if ((newData == null) || (newData.size() == 0))
      return;
    Iterator it = newData.keySet().iterator();
    while (it.hasNext()) {
      String key = (String)it.next();
      if (key.length() == 11)
      {

        if ((false == checkCache(key)) || (this.init.booleanValue() == true)) {
          Map<String, String> value = (Map)newData.get(key);
          
          LotteryLog log = new LotteryLog();
          log.setId(key);
          log.setLuckyNumber((String)value.get("number"));
          log.setDateline((String)value.get("dateline"));
          log.setCatchTime(new Date());
          addCache(log);
          try {
            LotteryLog ll = (LotteryLog)this.dao.get(LotteryLog.class, key);
            if (ll == null)
            {

              this.dao.save(LotteryLog.class, log); }
          } catch (Exception e) {
            e.printStackTrace();
          }
        } }
    }
    this.init = Boolean.valueOf(false);
  }
  
  public static void main(String... args) {
    ChongQingSpider s = new ChongQingSpider();
    for (;;) {
      try {
        Thread.sleep(2000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      s.doStuff();
    }
  }
  
  private void showCache() {
    System.out.print("cache@[");
    for (int i = 0; i < cache.size(); i++) {
      System.out.print(cache.get(i) + ",");
    }
    System.out.println("]");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\ChongQingSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */