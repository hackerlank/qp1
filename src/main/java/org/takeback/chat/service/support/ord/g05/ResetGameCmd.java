package org.takeback.chat.service.support.ord.g05;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;


@Component("resetGameCmd")
public class ResetGameCmd
  implements Command
{
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    room.setStep(Room.STEP_FREE);
    Message msg = new Message("TXT_SYS", user.getId(), "<span style='color:red'>游戏重置!</span>");
    MessageUtils.broadcast(room, msg);
    
    MessageUtils.sendCMD(session, "roomStep", Room.STEP_FREE);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\ResetGameCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */