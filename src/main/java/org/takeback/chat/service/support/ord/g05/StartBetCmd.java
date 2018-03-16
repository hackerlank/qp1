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




@Component("startBetCmd")
public class StartBetCmd
  implements Command
{
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (!user.getId().equals(room.getOwner())) {
      MessageUtils.sendCMD(session, "alert", "非法操作!");
      return;
    }
    
    if ((room.getStep() == Room.STEP_CHECK3) || (room.getStep() == Room.STEP_PLAY_FINISHED) || (room.getStep() == Room.STEP_START_BET)) {
      room.setStep(Room.STEP_START_BET);
      Message msg = new Message("TXT_SYS", user.getId(), "<span style='color:red'>开始下注!</span>");
      MessageUtils.broadcast(room, msg);
      MessageUtils.sendCMD(session, "roomStep", Room.STEP_START_BET);
    } else {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\StartBetCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */