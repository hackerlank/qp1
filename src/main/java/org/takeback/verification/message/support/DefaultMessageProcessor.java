package org.takeback.verification.message.support;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.takeback.verification.image.VerifyCodeUtils;
import org.takeback.verification.message.MessageProcessor;
import org.takeback.verification.message.SmsTemplates;

public class DefaultMessageProcessor
  implements MessageProcessor
{
  protected int defaultCodeLength = 4;
  protected static final String VERIFY_CODES = "1234567890";
  
  protected String generateCode() {
    return VerifyCodeUtils.generateVerifyCode(this.defaultCodeLength, "1234567890");
  }
  
  public String sendCode(String phoneNumber)
  {
    return sendCode(phoneNumber, "0");
  }
  
  public String sendCode(String phoneNumber, String tpl)
  {
    String code = generateCode();
    sendSMS(phoneNumber, tpl, ImmutableMap.of("code", code));
    return code;
  }
  
  public String sendSMS(String phoneNumber, String tpl, Map<String, String> params)
  {
    String content = SmsTemplates.getTemplate(tpl, params);
    if (content == null) {
      throw new IllegalArgumentException("tpl " + tpl + " is not exists");
    }
    return sendSMS(phoneNumber, content);
  }
  
  public String sendSMS(String phoneNumber, String content)
  {
    return content;
  }
  
  public void setDefaultCodeLength(int length)
  {
    this.defaultCodeLength = length;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\support\DefaultMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */