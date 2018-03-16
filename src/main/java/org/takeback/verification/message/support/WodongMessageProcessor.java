package org.takeback.verification.message.support;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.util.httpclient.HttpClientUtils;

public class WodongMessageProcessor extends DefaultMessageProcessor
{
  private static final Logger log = LoggerFactory.getLogger(WodongMessageProcessor.class);
  
  private String API_URL = "http://115.29.242.32:8888/sms.aspx";
  private String API_URL_GBK = "http://115.29.242.32:8888/smsGBK.aspx";
  
  private String userid;
  
  private String account;
  private String password;
  
  public String sendSMS(String phoneNumber, String content)
  {
    LinkedHashMap<String, String> ps = new LinkedHashMap();
    ps.put("action", "send");
    ps.put("userid", this.userid);
    ps.put("account", this.account);
    ps.put("password", this.password);
    ps.put("mobile", phoneNumber);
    ps.put("content", content);
    String result = HttpClientUtils.post(this.API_URL, ps);
    System.out.println(result);
    try {
      Document doc = DocumentHelper.parseText(result);
      Element root = doc.getRootElement();
      if ((!"Success".equals(root.elementText("returnstatus"))) && (!"ok".equals(root.elementText("message"))))
      {

        log.error("send content {} to {} failed, responseMsg is {}", new Object[] { content, phoneNumber, root.elementText("message") });
      }
    } catch (DocumentException e) {
      log.error("send content {} to {} failed, responseMsg is {}", new Object[] { content, phoneNumber, result });
    }
    return content;
  }
  



  public static void main(String[] args) {}
  



  public String getAPI_URL()
  {
    return this.API_URL;
  }
  
  public void setAPI_URL(String API_URL) {
    this.API_URL = API_URL;
  }
  
  public String getAPI_URL_GBK() {
    return this.API_URL_GBK;
  }
  
  public void setAPI_URL_GBK(String API_URL_GBK) {
    this.API_URL_GBK = API_URL_GBK;
  }
  
  public String getUserid() {
    return this.userid;
  }
  
  public void setUserid(String userid) {
    this.userid = userid;
  }
  
  public String getAccount() {
    return this.account;
  }
  
  public void setAccount(String account) {
    this.account = account;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\support\WodongMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */