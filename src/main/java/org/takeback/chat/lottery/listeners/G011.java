package org.takeback.chat.lottery.listeners;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.Game01Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;

@Component("G011")
public class G011 extends DefaultGameListener
{
  @Autowired
  private Game01Service game01Service;
  
  public void onFinished(Lottery lottery) throws GameException
  {
    super.onFinished(lottery);
    
    if ("2".equals(lottery.getType())) {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      this.game01Service.dealResult(lottery, room);
      this.lotteryService.setLotteryFinished(lottery.getId());
    }
  }
  
  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException {
    throw new GameException(500, "不允许发包!");
  }
  


  public void processExpireEvent(Lottery lottery)
    throws GameException
  {
    super.processExpireEvent(lottery);
    
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStatus("0");
    
    this.game01Service.gameLotteryExpired(lottery, room);
    Message msg = new Message("TXT_SYS", Integer.valueOf(0), "<span style='color:#B22222'>游戏包过期,游戏停止</span>");
    MessageUtils.broadcast(room, msg);
  }
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery) throws GameException
  {
    if ("1".equals(lottery.getType())) {
      return true;
    }
    Double money = Double.valueOf(getConifg(lottery.getRoomId(), "conf_money"));
    try {
      User user = (User)this.userStore.get(uid);
      this.game01Service.open(lottery, user, money);
    } catch (CodedBaseRuntimeException e) {
      throw new GameException(500, e.getMessage());
    }
    return true;
  }
  
  public void processStartEvent(Room room) throws GameException
  {
    super.processStartEvent(room);
    Integer number = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    Double money = Double.valueOf(getConifg(room.getId(), "conf_money_start"));
    Integer expreid = Integer.valueOf(getConifg(room.getId(), "conf_expired"));
    

    Lottery lottery = LotteryFactory.getDefaultBuilder(BigDecimal.valueOf(money.doubleValue()), number).setDescription("游戏开始,祝你好运!").setSender(0).setType("2").setExpiredSeconds(expreid).setRoom(room).build();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery, GcLottery.class);
    this.lotteryService.save(GcLottery.class, gcLottery);
    this.lotteryService.setRoomStatus(room.getId(), "1");
    room.setStatus("1");
    Message message = new Message("RED", Integer.valueOf(0), lottery);
    message.setHeadImg("img/system.png");
    message.setNickName("系统");
    MessageUtils.broadcast(room, message);
  }
  
  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail) throws GameException
  {
    super.onOpen(lottery, lotteryDetail);
    if ("2".equals(lottery.getType())) {
      String sendNickName = null;
      User opener = (User)this.userStore.get(lotteryDetail.getUid());
      if (0 == lottery.getSender().intValue()) {
        sendNickName = "系统";
      } else {
        User sender = (User)this.userStore.get(lottery.getSender());
        if (opener.getId().equals(sender.getId())) {
          sendNickName = "自己";
        } else {
          sendNickName = sender.getNickName();
        }
      }
      String msg = "<span style='color:#F89C4C'>" + opener.getNickName() + "</span> 领取了<span style='color:#F89C4C'>" + sendNickName + "</span>发的红包";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
    }
  }
  
  public boolean onBeforeStart(Room room) throws GameException
  {
    return true;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G011.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */