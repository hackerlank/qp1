package org.takeback.chat.service.support;

import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;

public abstract interface MessageProcessor
{
  public abstract void process(Message paramMessage, WebSocketSession paramWebSocketSession, Room paramRoom, User paramUser);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\MessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */