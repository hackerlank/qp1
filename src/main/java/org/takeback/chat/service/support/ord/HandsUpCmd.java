package org.takeback.chat.service.support.ord;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;

@Component("handsUpCmd")
public class HandsUpCmd
  implements Command
{
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    user.setHandsUp(Boolean.valueOf(true));
    







    RoomAndLotteryListener listener = room.getRoomAndLotteryListener();
    try {
      if (listener != null) {
        if (listener.onBeforeStart(room)) {
          room.start();
        }
        

      }
      else
      {
        room.start();
      }
    } catch (GameException e) {
      MessageUtils.sendCMD(session, "alert", e.getMessage());
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\HandsUpCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */