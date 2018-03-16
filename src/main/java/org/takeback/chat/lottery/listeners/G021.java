package org.takeback.chat.lottery.listeners;

import java.math.BigDecimal;
import java.util.Map;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.Game02Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;

@org.springframework.stereotype.Component("G021")
public class G021 extends G02
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
    if (room.getMaster().intValue() < 0) {
      room.setMaster(Integer.valueOf(0));
      return true;
    }
    long sec = (System.currentTimeMillis() - room.getMasterStamp().longValue()) / 1000L;
    if (sec >= 120L) {
      if (room.getMaster().equals(Integer.valueOf(0))) {
        throw new GameException(500, "抢庄进行中，拆抢庄包争夺庄主.");
      }
      return true;
    }
    throw new GameException(500, "庄主停包<strong style='color:green'>120</strong>秒后可开始申请抢庄！<br>等待<strong style='color:red'>" + (120L - sec) + "</strong>秒后重新申请!");
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
      Double money = Double.valueOf(getDeposit(room) * lottery.getNumber().intValue());
      if (user.getMoney().doubleValue() < money.doubleValue()) {
        throw new GameException(500, "余额必须大于" + money + "元才能参与抢庄!");
      }
    }
    else {
      if (uid.equals(lottery.getSender())) {
        return true;
      }
      Double money = Double.valueOf(getDeposit(room));
      if (this.lotteryService.moneyDown(uid, money) < 1) {
        throw new GameException(500, "金额不能少于" + money + "元,请及时充值!");
      }
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
        this.game02Service.saveDetail(lottery, lotteryDetail, money.doubleValue(), "G02");
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
      
      double deposit = getDeposit(room);
      if (lotteryDetail.getUid().equals(lottery.getSender())) {
        deposit *= lottery.getNumber().intValue();
      }
      this.game02Service.saveDetail(lottery, lotteryDetail, deposit, "G021");
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
      
      if ((!lotteryDetail.getUid().equals(lottery.getSender())) && 
        (lottery.getRestNumber().intValue() == 1) && 
        (!lottery.getDetail().containsKey(lottery.getSender()))) {
        lottery.open(lottery.getSender().intValue());
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
    if (master == sender) {
      builder.setType("2");
      builder.setDescription("恭喜发财,害怕别来!");
      builder.setExpiredSeconds(Integer.valueOf(40));
      
      Double deposit = Double.valueOf(getDeposit(room));
      Integer num = Integer.valueOf(builder.getNumber());
      if (builder.getMoney().doubleValue() < 0.1D * builder.getNumber()) {
        throw new GameException(500, "红包金额必须大于" + 0.1D * builder.getNumber());
      }
      
      int affected = this.lotteryService.moneyDown(Integer.valueOf(sender), Double.valueOf(deposit.doubleValue() * num.intValue()));
      if (affected == 0) {
        throw new GameException(500, "金额不足!余额必须大于" + deposit.doubleValue() * num.intValue());
      }
      room.setStatus("1");
      room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
    } else {
      if (builder.getDescription().contains("牛牛")) {
        throw new GameException(500, "非法的关键字:牛牛");
      }
      builder.setType("1");
      
      int affected = this.lotteryService.moneyDown(Integer.valueOf(sender), Double.valueOf(builder.getMoney().doubleValue()));
      if (affected == 0) {
        throw new GameException(500, "余额不足!");
      }
    }
    builder.build();
    return true;
  }
  

  public void onRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {
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
      dealMaster(lottery);
      return;
    }
    this.lotteryService.setLotteryFinished(lottery.getId());
    dealGame(lottery);
  }
  
  public void processExpireEvent(Lottery lottery) throws GameException
  {
    if ("2".equals(lottery.getType()))
    {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      room.setStatus("0");
      MessageUtils.broadcast(room, new Message("gameOver", null));
      
      if (lottery.getSender().equals(Integer.valueOf(0))) {
        dealMaster(lottery);
        return;
      }
      
      if (!lottery.getDetail().containsKey(lottery.getSender())) {
        lottery.fakeOpen(lottery.getSender().intValue());
      }
      
      this.lotteryService.setLotteryExpired(lottery.getId());
      dealGame(lottery);
    }
  }
  



  private void openForMaster(Lottery lottery) {}
  



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
      String str = "<span style='color:#B22222'>无人参与抢庄,抢庄结束.";
      Message msg = new Message("TXT_SYS", Integer.valueOf(0), str);
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      room.setStatus("0");
      room.setMaster(Integer.valueOf(-1));
      

      MessageUtils.broadcast(room, msg);
      return;
    }
    
    User master = (User)this.userStore.get(maxMan);
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setMaster(maxMan);
    room.setMasterTimes(Integer.valueOf(1));
    room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
    
    String str = "<span style='color:#F89C4C'>" + master.getNickName() + "</span> 坐上庄主宝座,傲视群雄！";
    Message msg = new Message("TXT_SYS", Integer.valueOf(0), str);
    MessageUtils.broadcast(room, msg);
    
    String str1 = "<span style='color:#B22222'>你已成为庄主,发红包开始坐庄!</span>";
    Message msg1 = new Message("TXT_SYS", Integer.valueOf(0), str1);
    MessageUtils.send(master.getId(), room, msg1);
  }
  
  private void dealGame(Lottery lottery) throws GameException {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStatus("0");
    Integer masterId = lottery.getSender();
    User master = (User)this.userStore.get(masterId);
    LotteryDetail masterDetail = (LotteryDetail)lottery.getDetail().get(masterId);
    Integer masterPoint = Integer.valueOf(NumberUtil.getDecimalPartSum(masterDetail.getCoin()).intValue() % 10);
    BigDecimal masterInout = new BigDecimal(0.0D);
    
    if (masterPoint.intValue() == 0) {
      masterPoint = Integer.valueOf(10);
    }
    BigDecimal water = new BigDecimal(0.0D);
    Map<Integer, LotteryDetail> details = lottery.getDetail();
    StringBuilder msg = new StringBuilder("<table style='color:#0493b2'>");
    Map<Integer, Double> addMap = new java.util.HashMap();
    BigDecimal deposit = new BigDecimal(getDeposit(room));
    double feePercent = room.getFeeAdd() == null ? 0.0D : room.getFeeAdd().doubleValue();
    if ((feePercent >= 1.0D) || (feePercent < 0.0D)) {
      throw new GameException(500, "服务费设置错误!");
    }
    for (LotteryDetail ld : details.values())
      if (!ld.getUid().equals(masterId))
      {

        User player = (User)this.userStore.get(ld.getUid());
        Integer playerPoint = Integer.valueOf(NumberUtil.getDecimalPartSum(ld.getCoin()).intValue() % 10);
        if (playerPoint.equals(Integer.valueOf(0))) {
          playerPoint = Integer.valueOf(10);
        }
        msg.append("<tr><td>〖闲〗</td><td class='g021-nick-name'>").append(player.getNickName()).append("</td><td>(").append(ld.getCoin()).append(")</td>");
        if (masterPoint.intValue() > playerPoint.intValue()) {
          BigDecimal inout = getInout(room, masterPoint.intValue());
          if (masterPoint.intValue() == 10) {
            water = water.add(inout.multiply(new BigDecimal(feePercent)));
            masterInout = masterInout.add(inout.multiply(new BigDecimal(1.0D - feePercent)));
          } else {
            masterInout = masterInout.add(inout);
          }
          
          addMap.put(player.getId(), Double.valueOf(getDeposit(room) - inout.doubleValue()));
          
          msg.append("<td style='color:green;'>").append("牛").append(NAMES[playerPoint.intValue()]).append(" -").append(NumberUtil.format(inout)).append("</td>");
        } else if (masterPoint.intValue() < playerPoint.intValue()) {
          BigDecimal inout = getInout(room, playerPoint.intValue());
          masterInout = masterInout.subtract(inout);
          if (playerPoint.intValue() == 10) {
            water = water.add(inout.multiply(new BigDecimal(feePercent)));
            inout = inout.multiply(new BigDecimal(1.0D - feePercent));
          }
          
          addMap.put(player.getId(), Double.valueOf(getDeposit(room) + inout.doubleValue()));
          
          msg.append("<td style='color:red;'>").append("牛").append(NAMES[playerPoint.intValue()]).append(NumberUtil.format(inout)).append("</td>");

        }
        else if (masterDetail.getCoin().compareTo(ld.getCoin()) >= 0) {
          BigDecimal inout = getInout(room, masterPoint.intValue());
          if (masterPoint.intValue() == 10) {
            water = water.add(inout.multiply(new BigDecimal(feePercent)));
            masterInout = masterInout.add(inout.multiply(new BigDecimal(1.0D - feePercent)));
          } else {
            masterInout = masterInout.add(inout);
          }
          
          addMap.put(player.getId(), Double.valueOf(getDeposit(room) - inout.doubleValue()));
          
          msg.append("<td style='color:green;'>").append("牛").append(NAMES[playerPoint.intValue()]).append(" -").append(NumberUtil.format(inout)).append("</td>");
        } else {
          BigDecimal inout = getInout(room, playerPoint.intValue());
          masterInout = masterInout.subtract(inout);
          if (playerPoint.intValue() == 10) {
            water = water.add(inout.multiply(new BigDecimal(feePercent)));
            inout = inout.multiply(new BigDecimal(1.0D - feePercent));
          }
          
          addMap.put(player.getId(), Double.valueOf(getDeposit(room) + inout.doubleValue()));
          
          msg.append("<td style='color:red;'>").append("牛").append(NAMES[playerPoint.intValue()]).append("+").append(NumberUtil.format(inout)).append("</td>");
        }
        
        msg.append("</tr>");
      }
    msg.append("<tr><td  style='color:#B22222'>【庄】</td><td class='g021-nick-name'>").append(master.getNickName()).append("</td><td>(").append(masterDetail.getCoin()).append(")</td>");
    if (masterInout.compareTo(new BigDecimal(0)) > 0) {
      msg.append("<td style='color:red'>").append("牛").append(NAMES[masterPoint.intValue()]).append("+").append(NumberUtil.format(masterInout)).append("</td>");
    } else if (masterInout.compareTo(new BigDecimal(0)) < 0) {
      msg.append("<td style='color:green'>").append("牛").append(NAMES[masterPoint.intValue()]).append(" -").append(NumberUtil.format(Math.abs(masterInout.doubleValue()))).append("</td></tr>");
    } else {
      msg.append("<td style='color:gray'>").append("牛").append(NAMES[masterPoint.intValue()]).append("±平庄</td></tr>");
    }
    msg.append("</table>");
    

    masterInout = masterInout.add(new BigDecimal(getDeposit(room) * lottery.getNumber().intValue()));
    
    addMap.put(masterId, Double.valueOf(masterInout.doubleValue()));
    

    Message rmsg = new Message("TXT_SYS", Integer.valueOf(0), msg.toString());
    MessageUtils.broadcast(room, rmsg);
  }
  
  private void sendMasterRed(Room room) throws GameException
  {
    BigDecimal bd = new BigDecimal(1);
    

    Lottery lottery = org.takeback.chat.store.room.LotteryFactory.getDefaultBuilder(bd, Integer.valueOf(10)).setExpiredSeconds(Integer.valueOf(40)).setRoom(room).setType("2").setSender(0).setDescription("开始抢庄,谁大谁庄!").build();
    








    Message redMessage = new Message("RED", Integer.valueOf(0), lottery);
    redMessage.setHeadImg("img/system.png");
    redMessage.setNickName("系统");
    MessageUtils.broadcast(room, redMessage);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G021.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */