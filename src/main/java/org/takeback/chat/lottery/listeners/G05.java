package org.takeback.chat.lottery.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;







@Component("G05")
public class G05
  extends DefaultGameListener
{
  @Autowired
  GameG05Service gameG05Service;
  @Autowired
  RoomStore roomStore;
  @Autowired
  UserStore userStore;
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery)
    throws GameException
  {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    Integer masterRecordId = room.getMasterRecordId();
    if ((this.gameG05Service.checkBet(masterRecordId, uid)) || (room.getMaster().equals(uid))) {
      return true;
    }
    throw new GameException("本期您未下注,无法抢包!");
  }
  







  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail)
    throws GameException
  {
    String sendNickName = null;
    User opener = (User)this.userStore.get(lotteryDetail.getUid());
    
    User sender = (User)this.userStore.get(lottery.getSender());
    if (opener.getId().equals(sender.getId())) {
      sendNickName = "自己";
    } else {
      sendNickName = sender.getNickName();
    }
    
    if ("1".equals(lottery.getType())) {
      this.lotteryService.saveLotteryDetail(lottery, lotteryDetail);
      String msg = opener.getNickName() + " 领取了" + sendNickName + "发的红包";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
    } else {
      this.lotteryService.saveLotteryDetail(lottery, lotteryDetail);
      String msg = opener.getNickName() + " 领取了红包";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
    }
  }
  
  public void onFinished(Lottery lottery) throws GameException
  {
    if ("1".equals(lottery.getType())) {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      User sender = (User)this.userStore.get(lottery.getSender());
      String msg = "<span style='color:#F89C4C'>" + sender.getNickName() + "</span> 的红包已被领完.";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast(room, notice);
      return;
    }
    
    this.gameG05Service.dealResult(lottery);
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStep(Room.STEP_PLAY_FINISHED);
    this.lotteryService.setLotteryFinished(lottery.getId());
  }
  
  public void processExpireEvent(Lottery lottery) throws GameException
  {
    if ("2".equals(lottery.getType())) {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      room.setStatus("0");
      MessageUtils.broadcast(room, new Message("gameOver", null));
      this.gameG05Service.dealResult(lottery);
      room.setStep(Room.STEP_PLAY_FINISHED);
    }
    this.lotteryService.setLotteryExpired(lottery.getId());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G05.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */