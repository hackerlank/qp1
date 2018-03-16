package org.takeback.chat.service.support;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.BeanUtils;
import org.takeback.util.converter.ConversionUtils;

@Component("redMessageProcessor")
public class RedMessageProcessor
  extends TxtMessageProcessor
{
  public static final Logger LOGGER = LoggerFactory.getLogger(RedMessageProcessor.class);
  
  @Autowired
  private LotteryService lotteryService;
  
  public void process(Message message, WebSocketSession session, Room room, User user)
  {
    LotteryFactory.DefaultLotteryBuilder builder = generateLottery(message, session, room, user);
    if (builder == null) {
      return;
    }
    RoomAndLotteryListener listener = room.getRoomAndLotteryListener();
    if (listener != null) {
      try {
        if (!listener.onBeforeRed(builder)) {
          return;
        }
      } catch (GameException e) {
        LOGGER.error(e.getMessage());
        MessageUtils.sendCMD(session, "alert", e.getMessage());
        return;
      }
    }
    builder.setRoom(room);
    BigDecimal money = (BigDecimal)ConversionUtils.convert(user.getMoney(), BigDecimal.class);
    





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
    this.lotteryService.save(GcLottery.class, gcLottery);
    Message redMessage = (Message)BeanUtils.map(message, Message.class);
    redMessage.setContent(gcLottery);
    MessageUtils.broadcast(room, redMessage);
    if (listener != null) {
      try {
        listener.onRed(builder);
      } catch (GameException e) {
        LOGGER.error(e.getMessage());
        MessageUtils.sendCMD(session, "alert", e.getMessage());
      }
    }
  }
  
  protected LotteryFactory.DefaultLotteryBuilder generateLottery(Message message, WebSocketSession session, Room room, User user)
  {
    try {
      Map<String, Object> body = (Map)message.getContent();
      BigDecimal money = (BigDecimal)ConversionUtils.convert(body.get("money"), BigDecimal.class);
      Integer number = (Integer)ConversionUtils.convert(body.get("number"), Integer.class);
      



      LotteryFactory.DefaultLotteryBuilder builder = LotteryFactory.getDefaultBuilder(money, number).setType("1").setExpiredSeconds(Integer.valueOf(40)).setSender(user.getId().intValue()).setRoomId(room.getId());
      
      String description = (String)body.get("description");
      if (!StringUtils.isEmpty(description)) {
        builder.setDescription(description);
      }
      



      return builder;
    } catch (Exception e) {
      e.printStackTrace();
      MessageUtils.sendCMD(session, "alert", "发送红包失败");
    }
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\RedMessageProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */