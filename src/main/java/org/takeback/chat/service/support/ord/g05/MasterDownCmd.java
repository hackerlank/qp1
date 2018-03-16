package org.takeback.chat.service.support.ord.g05;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;


@Component("masterDownCmd")
public class MasterDownCmd
  implements Command
{
  @Autowired
  GameG05Service gameG05Service;
  @Autowired
  UserStore userStore;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (!user.getId().equals(room.getOwner())) {
      MessageUtils.sendCMD(session, "alert", "非法操作!");
      return;
    }
    
    if ((room.getStep() == Room.STEP_CHECK3) || (room.getStep() == Room.STEP_PLAY_FINISHED)) {
      this.gameG05Service.restoreMasterMoney(room.getMasterRecordId());
      PubUser master = (PubUser)this.gameG05Service.get(PubUser.class, room.getMaster());
      Message msg = new Message("TXT_SYS", user.getId(), "<span style='color:#B22222'>" + master.getNickName() + "已下庄,剩余金币已退还账户!</span>");
      MessageUtils.broadcast(room, msg);
      room.setMaster(Integer.valueOf(0));
      room.setStep(Room.STEP_FREE);
      room.setMasterRecordId(Integer.valueOf(0));
    } else {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
  
  private String buildMessage(Double money) {
    return "<span style='color:#B22222'>[注] </span><span style='color:orange;font-style:italic;font-weight:bold;font-size:18px;'>" + money + "</span> ";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\MasterDownCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */