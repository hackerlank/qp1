package org.takeback.chat.service.support;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;

@Component("txtMessageProcessor")
public class TxtMessageProcessor implements MessageProcessor
{
  public void process(Message message, WebSocketSession session, Room room, User user)
  {
    MessageUtils.broadcast(room, message);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\TxtMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */