package org.takeback.chat.lottery.listeners;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.Game04Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;



@Component("G04")
public class G04
  extends DefaultGameListener
{
  @Autowired
  Game04Service game04Service;
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery)
    throws GameException
  {
    Room r = (Room)this.roomStore.get(lottery.getRoomId());
    Double money = getDeposit(r, lottery);
    if (lottery.getSender().equals(uid)) {
      return true;
    }
    
    if (this.lotteryService.moneyDown(uid, money) < 1) {
      throw new GameException(500, "金币不能少于" + money + ",请及时充值!");
    }
    return true;
  }
  
  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail) throws GameException
  {
    User opener = (User)this.userStore.get(lotteryDetail.getUid());
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    User sender = (User)this.userStore.get(lottery.getSender());
    Integer tailPoint = NumberUtil.getTailPoint(lotteryDetail.getCoin());
    String raidStr = lottery.getDescription().charAt(lottery.getDescription().indexOf("雷") + 1) + "";
    Integer raidPoint = Integer.valueOf(raidStr);
    
    String msg = opener.getNickName() + " 抢了你的红包,幸运躲过了地雷!";
    
    if (lottery.getSender().equals(lotteryDetail.getUid())) {
      this.lotteryService.moneyUp(opener.getId(), Double.valueOf(lotteryDetail.getCoin().doubleValue()));
      this.game04Service.saveDetail(lottery, lotteryDetail, Double.valueOf(lotteryDetail.getCoin().doubleValue()));
    } else if (lotteryDetail.getUid().equals(room.getUnDead())) {
      this.lotteryService.moneyUp(opener.getId(), Double.valueOf(getDeposit(room, lottery).doubleValue() + lotteryDetail.getCoin().doubleValue()));
      this.game04Service.saveDetail(lottery, lotteryDetail, Double.valueOf(lotteryDetail.getCoin().doubleValue()));
    }
    else if (tailPoint.equals(raidPoint))
    {
      msg = "<span style='color:#F89C4C'>" + opener.getNickName() + "</span>不幸踩中你埋下的地雷!</span>";
      this.lotteryService.moneyUp(sender.getId(), getDeposit(room, lottery));
      this.lotteryService.moneyUp(opener.getId(), Double.valueOf(lotteryDetail.getCoin().doubleValue()));
      this.game04Service.saveDetail(lottery, lotteryDetail, Double.valueOf(lotteryDetail.getCoin().doubleValue() - getDeposit(room, lottery).doubleValue()));
    } else {
      this.lotteryService.moneyUp(opener.getId(), Double.valueOf(lotteryDetail.getCoin().doubleValue() + getDeposit(room, lottery).doubleValue()));
      this.game04Service.saveDetail(lottery, lotteryDetail, Double.valueOf(lotteryDetail.getCoin().doubleValue()));
    }
    
    Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
    MessageUtils.send(lottery.getSender(), room, notice);
  }
  
  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder)
    throws GameException
  {
    Room room = builder.getRoom();
    int master = room.getMaster().intValue();
    int sender = builder.getSender();
    User user = (User)this.userStore.get(Integer.valueOf(sender));
    
    int maxSize = Integer.valueOf(getConifg(room.getId(), "conf_max_size")).intValue();
    int minSize = Integer.valueOf(getConifg(room.getId(), "conf_min_size")).intValue();
    
    if ((builder.getNumber() > maxSize) || (builder.getNumber() < minSize)) {
      throw new GameException(500, "房间限制红包个数:" + minSize + "-" + maxSize + "个");
    }
    
    Double maxMoney = Double.valueOf(getConifg(room.getId(), "conf_max_money"));
    Double minMoney = Double.valueOf(getConifg(room.getId(), "conf_min_money"));
    
    if ((builder.getMoney().doubleValue() > maxMoney.doubleValue()) || (builder.getMoney().doubleValue() < minMoney.doubleValue())) {
      throw new GameException(500, "红包金币限制:" + minMoney + "-" + maxMoney);
    }
    
    builder.setType("2");
    
    int expired = Integer.valueOf(getConifg(room.getId(), "conf_expired")).intValue();
    
    String raid = builder.getDescription();
    try
    {
      raidPoint = Integer.valueOf(raid);
    } catch (Exception e) { Integer raidPoint;
      throw new GameException(500, "雷点数为0-9的个位数字");
    }
    Integer raidPoint;
    if ((raidPoint.intValue() > 9) || (raidPoint.intValue() < 0)) {
      throw new GameException(500, "雷点数为0-9的个位数字");
    }
    double perRate = Double.valueOf(getConifg(room.getId(), "conf_rate")).doubleValue();
    if (room.getProperties().containsKey("p_" + builder.getNumber())) {
      perRate = Double.valueOf(room.getProperties().get("p_" + builder.getNumber()).toString()).doubleValue();
    }
    

    double water = getWater(room, Double.valueOf(builder.getMoney().doubleValue())).doubleValue();
    double cutMoney = builder.getMoney().doubleValue();
    builder.setMoney(NumberUtil.round(builder.getMoney().subtract(new BigDecimal(water))));
    builder.setDescription(builder.getMoney() + "金/雷" + raidPoint + "/" + perRate + "倍");
    builder.setExpiredSeconds(Integer.valueOf(expired));
    int affected = this.lotteryService.moneyDown(Integer.valueOf(sender), Double.valueOf(cutMoney));
    if (affected == 0) {
      throw new GameException(500, "金币不足!余额必须大于" + cutMoney);
    }
    return true;
  }
  
  public void onRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException
  {
    Lottery l = builder.build();
    Room room = (Room)this.roomStore.get(builder.getRoomId());
    room.setStatus("1");
    MessageUtils.broadcast(room, new Message("gameBegin", null));
    
    if ((room.getUnDead() != null) && (room.getUnDead().intValue() > 0)) {
      System.out.println("免死號：" + room.getUnDead());
      BigDecimal b = l.fakeOpen(room.getUnDead().intValue());
      this.lotteryService.moneyUp(room.getUnDead(), Double.valueOf(b.doubleValue()));
    }
  }
  
  public void onFinished(Lottery lottery) throws GameException
  {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStatus("0");
    MessageUtils.broadcast(room, new Message("gameOver", null));
    this.lotteryService.setLotteryFinished(lottery.getId());
    double water = caculateWater(room, Double.valueOf(lottery.getMoney().doubleValue())).doubleValue();
    User sender = (User)this.userStore.get(lottery.getSender());
    if (false == "9".equals(sender.getUserType())) {
      this.game04Service.setWater(room.getId(), sender, Double.valueOf(water), lottery.getId());
    }
    Message rmsg = new Message("TXT_SYS", Integer.valueOf(0), sender.getUserId() + "游戏包已被抢光!");
    MessageUtils.broadcast(room, rmsg);
    this.game04Service.setMasterMonitorData(lottery);
    


    showLotteryResult(lottery);
  }
  
  private void showLotteryResult(Lottery lottery) {
    if (lottery.getDetail().size() == 0) {
      return;
    }
    StringBuilder msg = new StringBuilder("<table>");
    
    Iterator itr = lottery.getDetail().keySet().iterator();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      LotteryDetail ld = (LotteryDetail)lottery.getDetail().get(uid);
      User player = (User)this.userStore.get(ld.getUid());
      if (uid.equals(lottery.getSender())) {
        msg.append("<tr style='color:#0493b2'><td>〖发〗</td><td class='g021-nick-name'>").append(player.getNickName()).append("</td><td>免死</td></tr>");
      } else {
        String raidStr = lottery.getDescription().charAt(lottery.getDescription().indexOf("雷") + 1) + "";
        Integer tailPoint = NumberUtil.getTailPoint(ld.getCoin());
        if (tailPoint.toString().equals(raidStr)) {
          msg.append("<tr  style='color:#B22222'><td>〖抢〗</td><td class='g021-nick-name'>").append(player.getNickName()).append("</td><td>中雷</td></tr>");
        } else {
          msg.append("<tr><td>〖抢〗</td><td class='g021-nick-name'>").append(player.getNickName()).append("</td><td>无雷</td></tr>");
        }
      }
    }
    msg.append("</table>");
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    Message rmsg = new Message("TXT_SYS", Integer.valueOf(0), msg);
    MessageUtils.broadcast(room, rmsg);
  }
  
  public void processExpireEvent(Lottery lottery) throws GameException
  {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    User sender = (User)this.userStore.get(lottery.getSender());
    
    if (lottery.getRestMoney().doubleValue() > 0.0D) {
      double water = caculateWater(room, Double.valueOf(lottery.getMoney().doubleValue())).doubleValue();
      double money = lottery.getRestMoney().doubleValue();
      if (lottery.getRestNumber() != lottery.getNumber())
      {
        if (false == "9".equals(sender.getUserType())) {
          this.game04Service.setWater(room.getId(), sender, Double.valueOf(water), lottery.getId());
        }
      }
      
      this.lotteryService.moneyUp(lottery.getSender(), Double.valueOf(money));
    }
    Message rmsg = new Message("TXT_SYS", Integer.valueOf(0), sender.getUserId() + " 的雷包已过期!");
    MessageUtils.broadcast(room, rmsg);
    this.lotteryService.setLotteryExpired(lottery.getId());
    this.game04Service.setMasterMonitorData(lottery);
    if (lottery.getRestMoney().doubleValue() > 0.0D) {
      Message notcie = new Message("TXT_SYS", Integer.valueOf(0), "你的雷包已过期!" + NumberUtil.round(lottery.getRestMoney().doubleValue()) + "金币已经退入账户!");
      MessageUtils.send(lottery.getSender(), room, notcie);
    }
    
    showLotteryResult(lottery);
  }
  
  private Double getDeposit(Room r, Lottery lottery) {
    Double rate = r.getFeeAdd();
    Double types = Double.valueOf(1.0D);
    
    if (r.getProperties().containsKey("conf_rate")) {
      types = Double.valueOf(r.getProperties().get("conf_rate").toString());
    }
    

    String rateKey = "p_" + lottery.getNumber();
    if (r.getProperties().containsKey(rateKey)) {
      types = Double.valueOf(r.getProperties().get(rateKey).toString());
    }
    return Double.valueOf(lottery.getMoney().doubleValue() / (1.0D - rate.doubleValue()) * types.doubleValue());
  }
  
  private Double getWater(Room r, Double money) {
    Double rate = Double.valueOf(r.getFeeAdd() != null ? r.getFeeAdd().doubleValue() : 0.0D);
    if (rate.equals(Integer.valueOf(0))) {
      return Double.valueOf(0.0D);
    }
    return Double.valueOf(money.doubleValue() * rate.doubleValue());
  }
  
  private Double caculateWater(Room r, Double money) {
    Double rate = Double.valueOf(r.getFeeAdd() != null ? r.getFeeAdd().doubleValue() : 0.0D);
    if (rate.equals(Integer.valueOf(0))) {
      return Double.valueOf(0.0D);
    }
    
    return Double.valueOf(money.doubleValue() / (1.0D - rate.doubleValue()) * rate.doubleValue());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\G04.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */