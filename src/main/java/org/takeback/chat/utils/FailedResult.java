package org.takeback.chat.utils;

import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;

public class FailedResult
{
  private WebSocketSession session;
  private Message message;
  private Throwable error;
  
  public FailedResult(WebSocketSession session, Message message)
  {
    this.session = session;
    this.message = message;
  }
  
  public FailedResult(WebSocketSession session, Message message, Throwable error) {
    this(session, message);
    this.error = error;
  }
  
  public WebSocketSession getSession() {
    return this.session;
  }
  
  public void setSession(WebSocketSession session) {
    this.session = session;
  }
  
  public Message getMessage() {
    return this.message;
  }
  
  public void setMessage(Message message) {
    this.message = message;
  }
  
  public Throwable getError() {
    return this.error;
  }
  
  public void setError(Throwable error) {
    this.error = error;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\utils\FailedResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */