package org.takeback.verification.message.support;

import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.util.MD5StringUtil;
import org.takeback.util.httpclient.HttpClientUtils;

public class MomingMessageProcessor
  extends DefaultMessageProcessor
{
  private static final Logger log = LoggerFactory.getLogger(MomingMessageProcessor.class);
  
  private String user;
  private String password;
  private String api = "http://api.momingsms.com/";
  private String send_action = "send";
  private String balance_action = "getBalance";
  
  public String sendSMS(String phoneNumber, String content)
  {
    LinkedHashMap<String, String> ps = new LinkedHashMap();
    ps.put("action", this.send_action);
    ps.put("username", this.user);
    ps.put("password", MD5StringUtil.MD5Encode(this.password));
    ps.put("phone", phoneNumber);
    ps.put("content", content);
    ps.put("encode", "utf8");
    String result = HttpClientUtils.post(this.api, ps);
    if (!"100".equals(result)) {
      log.error("send content {} to {} failed, responseCode is {}", new Object[] { content, phoneNumber, result });
    }
    return content;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public void setUser(String user) {
    this.user = user;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getApi() {
    return this.api;
  }
  
  public void setApi(String api) {
    this.api = api;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\support\MomingMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */