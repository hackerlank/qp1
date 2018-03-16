package org.takeback.chat.websocket.listener;

import org.springframework.web.socket.WebSocketSession;

public abstract interface ConnectListener
{
  public abstract void onConnect(WebSocketSession paramWebSocketSession);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\listener\ConnectListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */