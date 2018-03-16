package org.takeback.chat.lottery.listeners;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.BeanUtils;

@Component("G03")
public class G03
  extends DefaultGameListener
{
  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException
  {
    return true;
  }
  

  public void onRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {}
  

  public void onFinished(Lottery lottery)
    throws GameException
  {}
  
  public void processExpireEvent(Lottery lottery)
    throws GameException
  {
    super.processExpireEvent(lottery);
  }
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery)
    throws GameException
  {
    return true;
  }
  
  public void processStartEvent(Room room)
    throws GameException
  {
    super.processStartEvent(room);
    Integer number = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    



    Lottery lottery = LotteryFactory.getDefaultBuilder(BigDecimal.valueOf(1.0D), number).setDescription("游戏开始,祝你好运!").setSender(0).setType("2").setRoom(room).build();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery, GcLottery.class);
    this.lotteryService.save(GcLottery.class, gcLottery);
    Message message = new Message("RED", Integer.valueOf(0), lottery);
    message.setHeadImg("img/avatar.png");
    message.setNickName("系统");
    MessageUtils.broadcast(room, message);
  }
  
  public boolean onBeforeStart(Room room) throws GameException
  {
    return true;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G03.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */