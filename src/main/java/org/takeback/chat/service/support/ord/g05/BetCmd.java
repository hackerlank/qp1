package org.takeback.chat.service.support.ord.g05;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;


@Component("betCmd")
public class BetCmd
  implements Command
{
  @Autowired
  GameG05Service gameG05Service;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (room.getStep() == Room.STEP_START_BET) {
      if (room.getMaster().equals(user.getId())) {
        MessageUtils.sendCMD(session, "alert", "庄家无需下注!");
        return;
      }
      
      String type = (String)data.get("type");
      Double money = Double.valueOf(data.get("money").toString());
      Double deposit = Double.valueOf(0.0D);
      if ("1".equals(type)) {
        Double maxTypes = Double.valueOf(room.getProperties().get("conf_n15").toString());
        deposit = Double.valueOf(money.doubleValue() * maxTypes.doubleValue());
      } else if ("2".equals(type)) {
        deposit = money;
      } else {
        MessageUtils.sendCMD(session, "alert", "错误的下注命令!");
        return;
      }
      
      Integer masterRecordId = room.getMasterRecordId();
      try {
        this.gameG05Service.bet(room, user, money, deposit, masterRecordId, type);
        MessageUtils.sendCMD(session, "alert", "下注成功!");
        
        Message msg = new Message("TXT", user.getId(), buildMessage(money));
        msg.setHeadImg(user.getHeadImg());
        msg.setNickName(user.getNickName());
        MessageUtils.broadcast(room, msg);
      }
      catch (Exception e) {
        MessageUtils.sendCMD(session, "alert", e.getMessage());
      }
    } else {
      MessageUtils.sendCMD(session, "alert", "非投注时间!");
    }
  }
  
  private String buildMessage(Double money) {
    return "<span style='color:#B22222'>[注] </span><span style='color:orange;font-style:italic;font-weight:bold;font-size:18px;'>" + money + "</span> ";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\BetCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */