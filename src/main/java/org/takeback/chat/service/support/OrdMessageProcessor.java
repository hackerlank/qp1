package org.takeback.chat.service.support;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.ApplicationContextHolder;

@Component("ordMessageProcessor")
public class OrdMessageProcessor extends RedMessageProcessor
{
  public void process(Message message, WebSocketSession session, Room room, User user)
  {
    try
    {
      String cmd = message.getCmd();
      Map<String, Object> data = (Map)message.getContent();
      Command command = (Command)ApplicationContextHolder.getBean(cmd);
      command.exec(data, message, session, room, user);
    } catch (Exception e) {
      e.printStackTrace();
      MessageUtils.sendCMD(session, "alert", "非法指令");
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\OrdMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */