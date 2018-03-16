package org.takeback.chat.service.support.ord.g05;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcMasterRecord;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;




@Component("finishBetCmd")
public class FinishBetCmd
  implements Command
{
  @Autowired
  GameG05Service gameG05Service;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (!user.getId().equals(room.getOwner())) {
      MessageUtils.sendCMD(session, "alert", "非法操作!");
      return;
    }
    
    if ((room.getStep() == Room.STEP_START_BET) || (room.getStep() == Room.STEP_FINISH_BET))
    {
      if (this.gameG05Service.getBetRecords(room.getMasterRecordId()).size() == 0) {
        GcMasterRecord gmr = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, room.getMasterRecordId());
        this.gameG05Service.restoreMasterMoney(room.getMasterRecordId());
        
        room.setMaster(Integer.valueOf(0));
        room.setStep(Room.STEP_FREE);
        room.setMasterRecordId(Integer.valueOf(0));
        Message msg = new Message("TXT", room.getManager(), "<span style='color:#B22222'>无人下注," + gmr.getFreeze() + " 已退还庄主账户!</span>");
        msg.setHeadImg(user.getHeadImg());
        msg.setNickName(user.getNickName());
        MessageUtils.broadcast(room, msg);
        return;
      }
      
      room.setStep(Room.STEP_FINISH_BET);
      Message msg = new Message("TXT_SYS", user.getId(), "<span style='color:red'>截止下注!</span>");
      MessageUtils.broadcast(room, msg);
      MessageUtils.sendCMD(session, "roomStep", Room.STEP_FINISH_BET);
    } else {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\FinishBetCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */