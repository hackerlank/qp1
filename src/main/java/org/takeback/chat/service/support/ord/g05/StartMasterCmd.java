package org.takeback.chat.service.support.ord.g05;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;


@Component("startMasterCmd")
public class StartMasterCmd
  implements Command
{
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (!user.getId().equals(room.getOwner())) {
      MessageUtils.sendCMD(session, "alert", "非法操作!");
      return;
    }
    
    if ((room.getStep() == Room.STEP_FREE) || (room.getStep() == Room.STEP_MASTER)) {
      room.setStep(Room.STEP_MASTER);
      Message msg = new Message("TXT_SYS", user.getId(), "<span style='color:red'>=====开始标庄=====</span>");
      MessageUtils.broadcast(room, msg);
      MessageUtils.sendCMD(session, "roomStep", Room.STEP_MASTER);
    } else {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\StartMasterCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */