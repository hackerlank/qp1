package org.takeback.chat.lottery.listeners;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
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
import org.takeback.util.BeanUtils;

@org.springframework.stereotype.Component("G02")
public class G02 extends DefaultGameListener
{
  public static final String GET_MASTER_TEXT = "开始抢庄,谁大谁庄!";
  public static final String[] NAMES = { "牛牛", "牛①", "牛②", "牛③", "牛④", "牛⑤", "牛⑥", "牛⑦", "牛⑧", "牛⑨", "牛牛", "金牛", "对子", "顺子", "满牛", "豹子" };
  public static final String[] TUORA = { "0.12", "1.23", "2.34", "3.45", "4.56", "5.67", "6.78", "7.89" };
  public static final String[] GODEN = { "0.10", "0.20", "0.30", "0.40", "0.50", "0.60", "0.70", "0.80", "0.90" };
  @Autowired
  Game02Service game02Service;
  
  public void processStartEvent(Room room) throws GameException
  {
    super.processStartEvent(room);
    sendNewMasterRed(room);
    room.setStatus("1");
    this.lotteryService.setRoomStatus(room.getId(), "1");
  }
  
  public boolean onBeforeStart(Room room) throws GameException
  {
    return true;
  }
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery) throws GameException
  {
    if ("1".equals(lottery.getType())) {
      return true;
    }
    

    if (uid.equals(lottery.getSender())) {
      return true;
    }
    
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    User user = (User)this.userStore.get(uid);
    
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      Double money = Double.valueOf(getMasterDeposit(room));
      if (this.lotteryService.moneyDown(uid, money) < 1) {
        throw new GameException(500, "余额必须大于" + money + "元餐能参与抢庄!");
      }
    } else {
      Double money = Double.valueOf(getDeposit(room));
      if (this.lotteryService.moneyDown(uid, money) < 1) {
        throw new GameException(500, "金额不能少于" + money + "元,请及时充值!");
      }
    }
    return true;
  }
  
  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail) throws GameException
  {
    User opener = (User)this.userStore.get(lotteryDetail.getUid());
    Message notice = null;
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      Double money = Double.valueOf(getMasterDeposit(room));
      this.game02Service.saveDetail(lottery, lotteryDetail, money.doubleValue(), "G02");
      String msg = opener.getNickName() + " 一脸严肃,参与了抢庄";
      notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
    } else {
      User sender = (User)this.userStore.get(lottery.getSender());
      Double money = Double.valueOf(getDeposit(room));
      if (lotteryDetail.getUid().equals(lottery.getSender())) {
        money = Double.valueOf(getMasterDeposit(room));
      }
      this.game02Service.saveDetail(lottery, lotteryDetail, money.doubleValue(), "G02");
      if (opener.getId().equals(lottery.getSender())) {
        return;
      }
      String msg = opener.getNickName() + " 抢走红包,与庄家兵戎相见!";
      notice = new Message("TXT_ALERT", Integer.valueOf(0), msg);
    }
    MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
  }
  
  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException
  {
    throw new GameException(500, "该房间不允许自由红包!");
  }
  
  public void onRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {}
  
  public void onFinished(Lottery lottery)
    throws GameException
  {
    this.lotteryService.setLotteryFinished(lottery.getId());
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      System.out.println(lottery.getId() + " finished");
      dealMaster(lottery);
      return;
    }
    this.game02Service.dealGame(lottery);
  }
  
  public void processExpireEvent(Lottery lottery) throws GameException
  {
    super.processExpireEvent(lottery);
    this.lotteryService.setLotteryExpired(lottery.getId());
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      System.out.println(lottery.getId() + " by " + lottery.getSender() + " expired");
      dealMaster(lottery);
      return;
    }
    this.game02Service.dealGame(lottery);
  }
  





  private void dealMaster(Lottery lottery)
    throws GameException
  {
    Map<Integer, LotteryDetail> details = lottery.getDetail();
    Integer maxMan = Integer.valueOf(0);
    BigDecimal maxMoney = null;
    for (LotteryDetail ld : details.values()) {
      if ((maxMan.intValue() == 0) || (maxMoney.compareTo(ld.getCoin()) < 0)) {
        maxMan = ld.getUid();
        maxMoney = ld.getCoin();
      }
    }
    if (maxMan.equals(Integer.valueOf(0)))
    {
      String str = "<span style='color:#B22222'>无人参与抢庄,游戏结束.";
      Message msg = new Message("TXT_SYS", Integer.valueOf(0), str);
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      room.setStatus("0");
      this.game02Service.gameStop(lottery);
      MessageUtils.broadcast(room, msg);
      return;
    }
    
    User master = (User)this.userStore.get(maxMan);
    String str = "<span style='color:#B22222'>" + master.getNickName() + "</span> 开始坐庄！";
    Message msg = new Message("TXT_SYS", Integer.valueOf(0), str);
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setMaster(maxMan);
    room.setMasterTimes(Integer.valueOf(1));
    MessageUtils.broadcast(room, msg);
    this.game02Service.returnMasterLoteryMoney(lottery, getMasterDeposit(room));
    
    sendNewGameRed(master, room);
  }
  
  private void sendNewGameRed(User master, Room room)
    throws GameException
  {
    Double deposit = Double.valueOf(getMasterDeposit(room));
    Double money = Double.valueOf(getConifg(room.getId(), "conf_money_game"));
    System.out.println("~~~~~~~~masterId:" + master.getId());
    

    if (this.lotteryService.moneyDown(master.getId(), deposit) == 0) {
      sendNewMasterRed(room);
      return;
    }
    Integer num = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    Integer expired = Integer.valueOf(getConifg(room.getId(), "conf_expired"));
    BigDecimal bd = new BigDecimal(money.doubleValue());
    




    Lottery lottery = LotteryFactory.getDefaultBuilder(bd, num).setExpiredSeconds(expired).setType("2").setSender(master.getId().intValue()).setDescription("恭喜发财,牛牛庄" + room.getMasterTimes()).setRoom(room).build();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery, GcLottery.class);
    this.lotteryService.save(GcLottery.class, gcLottery);
    room.addLottery(lottery);
    
    lottery.open(master.getId().intValue());
    Message redMessage = new Message("RED", master.getId(), lottery);
    redMessage.setHeadImg(master.getHeadImg());
    MessageUtils.broadcast(room, redMessage);
  }
  
  private void sendNewMasterRed(Room room) throws GameException
  {
    BigDecimal bd = new BigDecimal(1);
    Integer number = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    Integer expired = Integer.valueOf(getConifg(room.getId(), "conf_expired"));
    




    Lottery lottery = LotteryFactory.getDefaultBuilder(bd, number).setExpiredSeconds(expired).setType("2").setSender(0).setDescription("开始抢庄,谁大谁庄!").setRoom(room).build();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery, GcLottery.class);
    this.lotteryService.save(GcLottery.class, gcLottery);
    room.addLottery(lottery);
    Message redMessage = new Message("RED", Integer.valueOf(0), lottery);
    redMessage.setHeadImg("img/system.png");
    redMessage.setNickName("系统");
    MessageUtils.broadcast(room, redMessage);
  }
  






  protected BigDecimal getInout(Room room, int nn)
  {
    Map<String, Object> map = room.getProperties();
    String key = "conf_n" + nn;
    Double types = Double.valueOf(1.0D);
    if (map.get(key) != null) {
      types = Double.valueOf(map.get(key).toString());
    }
    Double money = Double.valueOf(map.get("conf_money").toString());
    return new BigDecimal(money.doubleValue() * types.doubleValue());
  }
  
  protected double getDeposit(Room room) throws GameException {
    Double conf_money = Double.valueOf(getConifg(room.getId(), "conf_money"));
    Double conf_n10 = Double.valueOf(getConifg(room.getId(), "conf_n10"));
    return conf_money.doubleValue() * conf_n10.doubleValue();
  }
  
  protected double getMasterDeposit(Room room) throws GameException {
    Double conf_money = Double.valueOf(getConifg(room.getId(), "conf_money"));
    Double conf_n10 = Double.valueOf(getConifg(room.getId(), "conf_n10"));
    Integer num = Integer.valueOf(getConifg(room.getId(), "conf_size"));
    return conf_money.doubleValue() * conf_n10.doubleValue() * num.intValue();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G02.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */