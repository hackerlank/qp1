package org.takeback.chat.service.support.ord.g05;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcMasterRecord;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;




@Component("checkMasterCmd")
public class CheckMasterCmd
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
    
    List<GcMasterRecord> list = this.gameG05Service.getMasterRecrods(room.getId());
    if (list.size() == 0) {
      MessageUtils.sendCMD(session, "alert", "无竞标记录!");
      return;
    }
    GcMasterRecord masterRecord = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, room.getMasterRecordId());
    PubUser master = (PubUser)this.gameG05Service.get(PubUser.class, room.getMaster());
    if (room.getStep() == Room.STEP_MASTER) {
      room.setStep(Room.STEP_CHECK1);
      Message msg = new Message("TXT", user.getId(), "<span style='color:green'>确认①次</span> <span style='color:#B22222'>" + master.getNickName() + " <span style='color:orange;font-size:20px;font-weight:bold;font-style:italic'>" + masterRecord.getFreeze() + "</span>竞标,有没有更高?</span>");
      msg.setHeadImg(user.getHeadImg());
      msg.setNickName(user.getNickName());
      MessageUtils.broadcast(room, msg);
      return; }
    if (room.getStep() == Room.STEP_CHECK1) {
      room.setStep(Room.STEP_CHECK2);
      Message msg = new Message("TXT", user.getId(), "<span style='color:green'>确认②次</span> <span style='color:#B22222'>" + master.getNickName() + " <span style='color:orange;font-size:20px;font-weight:bold;font-style:italic'>" + masterRecord.getFreeze() + "</span>竞标,有没有更高?</span>");
      msg.setHeadImg(user.getHeadImg());
      msg.setNickName(user.getNickName());
      MessageUtils.broadcast(room, msg);
      return; }
    if (room.getStep() == Room.STEP_CHECK2) {
      room.setStep(Room.STEP_CHECK3);
      Double maxTypes = Double.valueOf(room.getProperties().get("conf_n15").toString());
      GcMasterRecord gmr = this.gameG05Service.checkMasterRecord(room);
      Double betable = Double.valueOf(NumberUtil.round(gmr.getFreeze() / maxTypes.doubleValue()));
      room.setMasterRecordId(gmr.getId());
      room.setMaster(gmr.getUid());
      

      String txt = "<table style='color:#B22222'><tr><td colspan=2 align='center'><span style='color:red;font-weight:bold;font-size:20px;'>标庄结束!</span></td></tr><tr><td style='font-style:italic;font-weight:bold;font-size:18px;color:green' colspan=2>" + master.getNickName() + " <strong style='color:orange;font-style:italic;font-weight:bold;'>" + gmr.getFreeze() + " 夺标</strong></tr><tr><td >最低下注</td><td style='color:green'><strong>" + 10 + "</strong></td></tr><tr><td>最高下注</td><td style='color:red'><strong>" + 100 + "</strong></td></tr><tr><td>可押注金额</td><td style='color:orange'><strong>" + betable + "</strong></td></tr></table>";
      



      Message msg = new Message("TXT", user.getId(), txt);
      msg.setHeadImg(user.getHeadImg());
      msg.setNickName(user.getNickName());
      MessageUtils.broadcast(room, msg);
      MessageUtils.sendCMD(session, "roomStep", room.getStep());
      return;
    }
    MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\CheckMasterCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */