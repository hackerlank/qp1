package org.takeback.verification.message;

import java.util.Map;

public abstract interface MessageProcessor
{
  public abstract String sendCode(String paramString);
  
  public abstract String sendCode(String paramString1, String paramString2);
  
  public abstract String sendSMS(String paramString1, String paramString2, Map<String, String> paramMap);
  
  public abstract String sendSMS(String paramString1, String paramString2);
  
  public abstract void setDefaultCodeLength(int paramInt);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\MessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */