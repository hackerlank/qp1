package org.takeback.util.mail;

public class Mail
{
  private String title;
  private String content;
  private String receiver;
  
  public Mail(String title, String content, String receiver) {
    this.title = title;
    this.content = content;
    this.receiver = receiver;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getContent() {
    return this.content;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public String getReceiver() {
    return this.receiver;
  }
  
  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\mail\Mail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */