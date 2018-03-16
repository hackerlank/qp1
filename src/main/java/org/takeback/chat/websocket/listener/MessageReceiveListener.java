package org.takeback.chat.websocket.listener;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public abstract interface MessageReceiveListener
{
  public abstract void onMessageReceive(WebSocketSession paramWebSocketSession, WebSocketMessage<?> paramWebSocketMessage);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\listener\MessageReceiveListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */