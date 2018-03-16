package org.takeback.chat.lottery.listeners;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.Game02Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;

@Component("G022")
public class G022
  extends G02
{
  public void onStart(Room room) throws GameException
  {
    room.setStatus("1");
    sendMasterRed(room);
    MessageUtils.broadcast(room, new Message("gameBegin", null));
  }
  

  public boolean onBeforeStart(Room room)
    throws GameException
  {
    GcRoom gcRoom = (GcRoom)this.game02Service.get(GcRoom.class, room.getId());
    if (gcRoom == null) {
      throw new GameException(500, "房间已经解散,停止游戏!");
    }
    
    if (room.getMaster().intValue() < 0) {
      room.setMaster(Integer.valueOf(0));
      return true;
    }
    
    long sec = (System.currentTimeMillis() - room.getMasterStamp().longValue()) / 1000L;
    if (sec >= 60L) {
      if (room.getMaster().equals(Integer.valueOf(0))) {
        throw new GameException(500, "抢庄进行中，拆抢庄包争夺庄主.");
      }
      return true;
    }
    throw new GameException(500, "庄主停包<strong style='color:green'>60</strong>秒后可开始申请抢庄！<br>等待<strong style='color:red'>" + (60L - sec) + "</strong>秒后重新申请!");
  }
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery)
    throws GameException
  {
    if ("1".equals(lottery.getType())) {
      return true;
    }
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    PubUser user = (PubUser)this.lotteryService.get(PubUser.class, uid);
    
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      Double money = Double.valueOf(this.game02Service.getDeposit(room) * lottery.getNumber().intValue());
      if (user.getMoney().doubleValue() < money.doubleValue()) {
        throw new GameException(500, "余额必须大于" + money + "金币才能参与抢庄!");
      }
    }
    else {
      if (uid.equals(lottery.getSender()))
      {





        return true;
      }
      Double money = Double.valueOf(this.game02Service.getDeposit(room));
      if (this.game02Service.moneyDown(uid, money) < 1) {
        throw new GameException(500, "余额不能少于" + money + "金币,请及时充值!");
      }
      System.out.println(user.getId() + " before open ..");
    }
    
    return true;
  }
  
  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail) throws GameException
  {
    if ("1".equals(lottery.getType())) {
      this.lotteryService.saveLotteryDetail(lottery, lotteryDetail);
      User opener = (User)this.userStore.get(lotteryDetail.getUid());
      User sender = (User)this.userStore.get(lottery.getSender());
      String msg = "<span style='color:#F89C4C'>" + opener.getNickName() + "</span> 领取了<span style='color:#F89C4C'>" + sender.getNickName() + "</span>发的福利红包";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
    } else {
      User opener = (User)this.userStore.get(lotteryDetail.getUid());
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      if (lottery.getSender().equals(Integer.valueOf(0))) {
        Double money = Double.valueOf(getMasterDeposit(room));
        this.game02Service.saveDetail(lottery, lotteryDetail, money.doubleValue(), "G022");
        String msg = "<span style='color:#F89C4C'>" + opener.getNickName() + "</span> 参与抢庄.";
        Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
        MessageUtils.broadcast(room, notice);
        return;
      }
      User sender = (User)this.userStore.get(lottery.getSender());
      String sendNickName = sender.getNickName();
      String msg = "<span style='color:#F89C4C'>" + opener.getNickName() + "</span> 抢走红包,与庄家兵戎相见!";
      if (lottery.getSender().equals(lotteryDetail.getUid())) {
        msg = "<span style='color:#F89C4C'>庄家</span>抢走红包,坐等挑战.";
      }
      
      double deposit = this.game02Service.getDeposit(room);
      if (lotteryDetail.getUid().equals(lottery.getSender())) {
        deposit *= lottery.getNumber().intValue();
      }
      
      if (!lotteryDetail.getUid().equals(lottery.getSender())) {
        this.game02Service.saveDetail(lottery, lotteryDetail, deposit, "G022");
      }
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      if (lottery.getSender().equals(lotteryDetail.getUid())) {
        return;
      }
      
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
      
      if ((!lotteryDetail.getUid().equals(lottery.getSender())) && 
        (lottery.getRestNumber().intValue() == 1) && 
        (!lottery.getDetail().containsKey(lottery.getSender()))) {
        lottery.fakeOpen(lottery.getSender().intValue());
        LotteryDetail masterLd = (LotteryDetail)lottery.getDetail().get(lottery.getSender());
        this.game02Service.saveDetail(lottery, masterLd, deposit, "G022");
      }
    }
  }
  

  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {
    Room room = builder.getRoom();
    int master = room.getMaster().intValue();
    int sender = builder.getSender();
    User user = (User)this.userStore.get(Integer.valueOf(sender));
    
    GcRoom gcRoom = (GcRoom)this.game02Service.get(GcRoom.class, room.getId());
    if (gcRoom == null) {
      throw new GameException(500, "房间已经解散,停止游戏!");
    }
    
    if (master == sender) {
      int maxSize = Integer.valueOf(getConifg(room.getId(), "conf_max_size")).intValue();
      if ((builder.getNumber() > maxSize) || (builder.getNumber() < 2)) {
        throw new GameException(500, "房间限制红包个数:2-" + maxSize + "个!");
      }
      builder.setType("2");
      builder.setDescription("恭喜发财,大吉大利!");
      int expired = Integer.valueOf(getConifg(room.getId(), "conf_expired")).intValue();
      builder.setExpiredSeconds(Integer.valueOf(expired));
      
      Double deposit = Double.valueOf(this.game02Service.getDeposit(room));
      Integer num = Integer.valueOf(builder.getNumber());
      double min = builder.getNumber() * 0.3D;
      
      if (builder.getMoney().doubleValue() < min) {
        throw new GameException(500, "金币不得低于:" + NumberUtil.round(min));
      }
      

      int affected = this.game02Service.moneyDown(Integer.valueOf(sender), Double.valueOf(deposit.doubleValue() * num.intValue() + builder.getMoney().doubleValue()));
      if (affected == 0) {
        throw new GameException(500, "金币必须大于" + deposit.doubleValue() * num.intValue());
      }
      

      room.setStatus("1");
      room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
    } else {
      throw new GameException(500, "只允许庄主发包!");
    }
    return true;
  }
  
  public void onRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {
    Lottery lottery = builder.build();
    if ("2".equals(builder.getType()))
    {
      Room room = (Room)this.roomStore.get(builder.getRoomId());
      room.setStatus("1");
      MessageUtils.broadcast(room, new Message("gameBegin", null));
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
    
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStatus("0");
    MessageUtils.broadcast(room, new Message("gameOver", null));
    
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      this.game02Service.dealMaster(lottery);
      return;
    }
    this.lotteryService.setLotteryFinished(lottery.getId());
    
    Map<Integer, LotteryDetail> details = lottery.getDetail();
    LotteryDetail md = (LotteryDetail)details.get(lottery.getSender());
    if (md != null) {
      md.setCreateDate(new Date());
      double deposit = this.game02Service.getDeposit(room);
      if (md.getUid().equals(lottery.getSender())) {
        deposit *= lottery.getNumber().intValue();
      }
      this.game02Service.saveDetail(lottery, md, deposit, "G022");
    }
    this.game02Service.dealGame(lottery);
  }
  
  public void processExpireEvent(Lottery lottery)
    throws GameException
  {
    if ("2".equals(lottery.getType()))
    {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      room.setStatus("0");
      MessageUtils.broadcast(room, new Message("gameOver", null));
      
      if (lottery.getSender().equals(Integer.valueOf(0))) {
        this.game02Service.dealMaster(lottery);
        return;
      }
      
      if (!lottery.getDetail().containsKey(lottery.getSender())) {
        lottery.fakeOpen(lottery.getSender().intValue());
        Map<Integer, LotteryDetail> details = lottery.getDetail();
        LotteryDetail md = (LotteryDetail)details.get(lottery.getSender());
        if (md != null) {
          md.setCreateDate(new Date());
          double deposit = this.game02Service.getDeposit(room);
          if (md.getUid().equals(lottery.getSender())) {
            deposit *= lottery.getNumber().intValue();
          }
          this.game02Service.saveDetail(lottery, md, deposit, "G022");
        }
      }
      
      this.lotteryService.setLotteryExpired(lottery.getId());
      this.game02Service.dealGame(lottery);
    }
  }
  
  private void sendMasterRed(Room room) throws GameException
  {
    BigDecimal bd = new BigDecimal(1);
    
    Integer size = Integer.valueOf(room.getProperties().get("conf_size").toString());
    

    Lottery lottery = LotteryFactory.getDefaultBuilder(bd, size).setExpiredSeconds(Integer.valueOf(40)).setRoom(room).setType("2").setSender(0).setDescription("抢庄专包").build();
    Message redMessage = new Message("RED", Integer.valueOf(0), lottery);
    redMessage.setHeadImg("img/system.png");
    redMessage.setNickName("系统");
    MessageUtils.broadcast(room, redMessage);
  }
  
  protected double getMasterDeposit(Room room) throws GameException {
    Double conf_money = Double.valueOf(getConifg(room.getId(), "conf_money"));
    Double conf_n10 = Double.valueOf(getConifg(room.getId(), "conf_n15"));
    Integer num = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    return conf_money.doubleValue() * conf_n10.doubleValue() * num.intValue();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G022.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */