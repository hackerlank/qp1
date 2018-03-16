package org.takeback.chat.store.user;

import org.springframework.web.socket.WebSocketSession;

public class AnonymousUser extends User
{
  public AnonymousUser(WebSocketSession webSocketSession) {
    super.setWebSocketSession(webSocketSession);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\AnonymousUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */