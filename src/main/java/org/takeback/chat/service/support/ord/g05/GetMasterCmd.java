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

@Component("getMasterCmd")
public class GetMasterCmd implements Command
{
  static Double MIN_FREEZE = Double.valueOf(8000.0D);
  @Autowired
  GameG05Service gameG05Service;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    Double freeze = Double.valueOf(data.get("freeze").toString());
    if (freeze.doubleValue() < MIN_FREEZE.doubleValue()) {
      MessageUtils.sendCMD(session, "alert", "房间起标:" + MIN_FREEZE);
      return;
    }
    try {
      if ((room.getStep() == Room.STEP_MASTER) || (room.getStep() == Room.STEP_CHECK1) || (room.getStep() == Room.STEP_CHECK2)) {
        List<GcMasterRecord> masterRecords = this.gameG05Service.getMasterRecrods(room.getId());
        if (masterRecords.size() > 0) {
          GcMasterRecord maxRecord = (GcMasterRecord)masterRecords.get(0);
          Double maxFreeze = Double.valueOf(maxRecord.getFreeze());
          if (freeze.doubleValue() <= maxFreeze.doubleValue()) {
            MessageUtils.sendCMD(session, "alert", "竞标必须大于:" + maxFreeze);
            return;
          }
          
          for (GcMasterRecord rec : masterRecords) {
            if (rec.getUid().equals(user.getId())) {
              this.gameG05Service.addMasterFreeze(user, rec.getId(), Double.valueOf(freeze.doubleValue() - rec.getFreeze()));
              
              Message msg = new Message("TXT", user.getId(), buildMessage(freeze));
              msg.setHeadImg(user.getHeadImg());
              msg.setNickName(user.getNickName());
              MessageUtils.broadcast(room, msg);
              room.setMaster(user.getId());
              room.setStep(Room.STEP_MASTER);
              return;
            }
          }
        }
        GcMasterRecord masterRecord = this.gameG05Service.newMasterRecord(user, room, freeze);
        
        room.setMasterRecordId(masterRecord.getId());
        room.setMaster(user.getId());
        Message msg = new Message("TXT", user.getId(), buildMessage(freeze));
        msg.setHeadImg(user.getHeadImg());
        msg.setNickName(user.getNickName());
        MessageUtils.broadcast(room, msg);
        room.setStep(Room.STEP_MASTER);
      } else {
        MessageUtils.sendCMD(session, "alert", "非竞标时间!");
      }
    } catch (Exception e) {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
  
  private String buildMessage(Double freeze) {
    return "<span style='color:orange;font-style:italic;font-weight:bold;font-size:26px;'>" + freeze + "</span> <span style='color:#B22222;'>参与竞标</span>";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\GetMasterCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */