package org.takeback.verification.message.support;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.httpclient.HttpClientUtils;

public class YunpianMessageProcessor
  extends DefaultMessageProcessor
{
  private static final Logger log = LoggerFactory.getLogger(YunpianMessageProcessor.class);
  
  private String apiKey;
  
  private String URI_GET_USER_INFO = "http://yunpian.com/v1/user/get.json";
  

  private String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";
  

  private String URI_TPL_SEND_SMS = "http://yunpian.com/v1/sms/tpl_send.json";
  

  private String URI_SEND_VOICE = "http://yunpian.com/v1/voice/send.json";
  

  private String ENCODING = "UTF-8";
  
  public String sendSMS(String phoneNumber, String content)
  {
    LinkedHashMap<String, String> ps = new LinkedHashMap();
    ps.put("apikey", this.apiKey);
    ps.put("text", content);
    ps.put("mobile", phoneNumber);
    String result = HttpClientUtils.post(this.URI_SEND_SMS, ps);
    Map<String, Object> map = (Map)ConversionUtils.convert(result, Map.class);
    if (0 != ((Integer)map.get("code")).intValue()) {
      log.error("send content {} to {} failed, responseCode is {}", new Object[] { content, phoneNumber, result });
    }
    return content;
  }
  
  public String getApiKey() {
    return this.apiKey;
  }
  
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
  
  public String getURI_GET_USER_INFO() {
    return this.URI_GET_USER_INFO;
  }
  
  public void setURI_GET_USER_INFO(String URI_GET_USER_INFO) {
    this.URI_GET_USER_INFO = URI_GET_USER_INFO;
  }
  
  public String getURI_SEND_SMS() {
    return this.URI_SEND_SMS;
  }
  
  public void setURI_SEND_SMS(String URI_SEND_SMS) {
    this.URI_SEND_SMS = URI_SEND_SMS;
  }
  
  public String getURI_TPL_SEND_SMS() {
    return this.URI_TPL_SEND_SMS;
  }
  
  public void setURI_TPL_SEND_SMS(String URI_TPL_SEND_SMS) {
    this.URI_TPL_SEND_SMS = URI_TPL_SEND_SMS;
  }
  
  public String getURI_SEND_VOICE() {
    return this.URI_SEND_VOICE;
  }
  
  public void setURI_SEND_VOICE(String URI_SEND_VOICE) {
    this.URI_SEND_VOICE = URI_SEND_VOICE;
  }
  
  public String getENCODING() {
    return this.ENCODING;
  }
  
  public void setENCODING(String ENCODING) {
    this.ENCODING = ENCODING;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\support\YunpianMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */