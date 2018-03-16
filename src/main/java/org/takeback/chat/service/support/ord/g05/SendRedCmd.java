package org.takeback.chat.service.support.ord.g05;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.support.ord.Command;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.BeanUtils;

@Component("sendRedCmd")
public class SendRedCmd implements Command
{
  @Autowired
  GameG05Service gameG05Service;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    if (!user.getId().equals(room.getOwner())) {
      MessageUtils.sendCMD(session, "alert", "非法操作!");
      return;
    }
    
    if (room.getStep() == Room.STEP_FINISH_BET) {
      Integer masterRecordId = room.getMasterRecordId();
      if (this.gameG05Service.getBetRecords(masterRecordId).size() == 0) {
        MessageUtils.sendCMD(session, "alert", "无下注记录");
        return;
      }
      

      Integer number = Integer.valueOf(this.gameG05Service.getBetNumbers(masterRecordId).intValue() + 1);
      BigDecimal money = new BigDecimal(number.intValue() + 0.5D);
      Integer expiredTime = Integer.valueOf(60);
      



      LotteryFactory.DefaultLotteryBuilder builder = LotteryFactory.getDefaultBuilder(money, number).setType("2").setExpiredSeconds(expiredTime).setSender(user.getId().intValue()).setRoomId(room.getId());
      
      RoomAndLotteryListener listener = room.getRoomAndLotteryListener();
      if (listener != null) {
        try {
          if (!listener.onBeforeRed(builder)) {
            return;
          }
        } catch (GameException e) {
          MessageUtils.sendCMD(session, "alert", e.getMessage());
          return;
        }
      }
      builder.setRoom(room);
      GcLottery gcLottery = new GcLottery();
      gcLottery.setId(builder.getLotteryId());
      gcLottery.setRoomId(builder.getRoomId());
      gcLottery.setCreateTime(new Date());
      gcLottery.setDescription(builder.getDescription());
      gcLottery.setMoney(builder.getMoney());
      gcLottery.setSender(Integer.valueOf(builder.getSender()));
      gcLottery.setNumber(Integer.valueOf(builder.getNumber()));
      gcLottery.setStatus("0");
      gcLottery.setType(builder.getType());
      gcLottery.setExpiredSeconds(Integer.valueOf(builder.getExpiredSeconds()));
      this.gameG05Service.save(GcLottery.class, gcLottery);
      

      Message redMessage = (Message)BeanUtils.map(message, Message.class);
      redMessage.setContent(gcLottery);
      redMessage.setSender(user.getId());
      redMessage.setNickName(user.getNickName());
      redMessage.setType("RED");
      redMessage.setHeadImg(user.getHeadImg());
      MessageUtils.broadcast(room, redMessage);
      
      if (listener != null) {
        try {
          listener.onRed(builder);
        } catch (GameException e) {
          MessageUtils.sendCMD(session, "alert", e.getMessage());
          return;
        }
      }
      
      room.setStep(Room.STEP_PLAYING);
    } else {
      MessageUtils.sendCMD(session, "alert", GameG05Service.suggestNext(room.getStep()));
    }
  }
  
  private String buildMessage(Double money) {
    return "<span style='color:#B22222'>[注] </span><span style='color:orange;font-style:italic;font-weight:bold;font-size:18px;'>" + money + "</span> ";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\g05\SendRedCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */