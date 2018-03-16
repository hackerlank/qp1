package org.takeback.chat.store.user;

import com.google.common.cache.LoadingCache;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.math.RandomUtils;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PcEggLog;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.pcegg.PcEggStore;
import org.takeback.chat.store.room.LotteryFactory;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.BeanUtils;

public class RobotUser extends User implements Runnable
{
  LotteryService lotteryService;
  public static final Integer level = Integer.valueOf(1);
  
  public static String[] talkList = { "来个豹子...", "卧槽,今天手气好像差了点", "给我回点血吧!!!!!", "我打算收手了,草 再抢剁手了", "呵呵 我看着你剁了", "我今天看到个妹子,大街上就嘘嘘了起来,城里人真会玩", "感觉有点恶搞", "妈的老是我,,", "谁允许我唱首歌啊  这个软件啥都好,就是不能语音", "瓦里格气 我能说的只有圈圈和叉叉了", "我是一个小毛驴我从来都不齐", "好无聊啊........." };
  







  private Room room;
  







  private int masterTimes = 0;
  
  private Integer eggNumber = Integer.valueOf(0);
  
  private java.util.List<String> eggBets = new ArrayList();
  
  public RobotUser() {
    super.setUserType("0");
    this.lotteryService = ((LotteryService)org.takeback.util.ApplicationContextHolder.getBean("lotteryService"));
  }
  
  public void setRoom(Room rm) {
    this.room = rm;
  }
  
  public void run() {
    try {
      while (!Thread.interrupted()) {
        Long random = Long.valueOf(Math.round(10.0D * Math.random()));
        TimeUnit.SECONDS.sleep(random.longValue() + level.intValue());
        if (this.room.getType().startsWith("G01")) {
          playG01();
        } else if (this.room.getType().startsWith("G02")) {
          playG02();
        } else if (this.room.getType().startsWith("G03")) {
          playG03();
        } else if (this.room.getType().startsWith("G04")) {
          playG04();
        }
        long r1 = Math.round(10.0D * Math.random());
        TimeUnit.SECONDS.sleep(r1 + level.intValue());
      }
      
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      Thread.currentThread().interrupt();
      return;
    }
  }
  
  private void playG01()
  {
    Lottery lottery = getOpenableLottery();
    Map<String, Object> p = this.room.getProperties();
    Integer rest = Integer.valueOf(2);
    try {
      rest = Integer.valueOf(p.get("conf_rest_time").toString());
      rest = Integer.valueOf(rest.intValue() + 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    

    if (lottery != null)
    {
      if ((new Date().getTime() - lottery.getCreateTime().getTime()) / 1000L <= rest.intValue()) {
        return;
      }
      try
      {
        lottery.open(getId().intValue());
      }
      catch (GameException localGameException) {}
    }
  }
  

  private void playG02()
  {
    Lottery lottery = getOpenableLottery();
    if (lottery != null) {
      try {
        lottery.open(getId().intValue());

      }
      catch (GameException localGameException1) {}
    }
    else if (this.room.getMaster().equals(getId())) {
      Integer r = Integer.valueOf(RandomUtils.nextInt(5) + 2);
      if (this.masterTimes == r.intValue()) {
        this.masterTimes = 0;
        this.room.setMaster(Integer.valueOf(0));
      }
      
      Integer num = Integer.valueOf(this.room.getProperties().get("conf_size").toString());
      



      Lottery lottery2 = LotteryFactory.getDefaultBuilder(new BigDecimal(0.5D * num.intValue()), num).setType("2").setSender(getId().intValue()).setDescription("恭喜发财,大吉大利!").setRoom(this.room).build();
      this.room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
      GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery2, GcLottery.class);
      this.lotteryService.save(GcLottery.class, gcLottery);
      try {
        lottery2.fakeOpen(getId().intValue());
      } catch (GameException e) {
        e.printStackTrace();
        return;
      }
      Message redMessage = new Message("RED", getId(), lottery2);
      redMessage.setHeadImg(getHeadImg());
      redMessage.setNickName(getNickName());
      MessageUtils.broadcast(this.room, redMessage);
    }
  }
  
  private void playG03()
  {
    PcEggLog log = PcEggStore.getStore().getLastest();
    Calendar c = Calendar.getInstance();
    c.setTime(log.getExpireTime());
    c.add(13, -30);
    if (c.getTime().compareTo(new Date()) < 0) {
      return;
    }
    
    String[] games = { "大", "小", "单", "双", "大单", "大双", "小双", "小单", "极大", "极小", "红", "黄", "蓝" };
    int d = (int)Math.round(Math.random() * 10.0D);
    if ((d == 0) || (d % 4 == 0)) {
      return;
    }
    
    long game = Math.round(d) % games.length;
    if (game == games.length) {
      game -= 1L;
    }
    
    Double money = Double.valueOf(Math.random() * 100.0D);
    




    Lottery lottery = LotteryFactory.getDefaultBuilder(new BigDecimal(money.doubleValue()), Integer.valueOf(1)).setExpiredSeconds(Integer.valueOf(1)).setType("2").setTitle(games[((int)game)] + " " + org.takeback.chat.utils.NumberUtil.round(d * 5) + "金币").setSender(getId().intValue()).setDescription(log.getId() + "期").build();
    try
    {
      lottery.open(0);
    } catch (GameException e) {
      e.printStackTrace();
    }
    Message redMessage = new Message("RED", getId(), lottery);
    redMessage.setHeadImg(getHeadImg());
    redMessage.setNickName(getNickName());
    MessageUtils.broadcast(this.room, redMessage);
  }
  
  private void playG04() {
    Map<String, Object> p = this.room.getProperties();
    Integer size = Integer.valueOf(p.get("conf_max_size").toString());
    Integer money = Integer.valueOf(p.get("conf_max_money").toString());
    Double rate = this.room.getFeeAdd();
    

    Lottery lottery = getOpenableLottery();
    if (lottery != null) {
      try {
        lottery.open(getId().intValue());
      }
      catch (GameException localGameException) {}
    }
    else {
      double perRate = Double.valueOf(this.room.getProperties().get("conf_rate").toString()).doubleValue();
      if (this.room.getProperties().containsKey("p_" + size)) {
        perRate = Double.valueOf(this.room.getProperties().get("p_" + size.toString()).toString()).doubleValue();
      }
      Integer expired = Integer.valueOf(this.room.getProperties().get("conf_expired").toString());
      Integer raidPoint = Integer.valueOf(RandomUtils.nextInt(9));
      DecimalFormat df = new DecimalFormat("0.00");
      




      Lottery lottery2 = LotteryFactory.getDefaultBuilder(new BigDecimal(money.intValue() * (1.0D - rate.doubleValue())), size).setType("2").setSender(getId().intValue()).setExpiredSeconds(expired).setDescription(df.format(money.intValue() * (1.0D - rate.doubleValue())) + "金/雷" + raidPoint + "/" + perRate + "倍").setRoom(this.room).build();
      this.room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
      this.room.getProperties().put("raid", raidPoint);
      GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery2, GcLottery.class);
      this.lotteryService.save(GcLottery.class, gcLottery);
      Message redMessage = new Message("RED", getId(), lottery2);
      redMessage.setHeadImg(getHeadImg());
      redMessage.setNickName(getNickName());
      MessageUtils.broadcast(this.room, redMessage);
    }
  }
  


  public void talk()
  {
    long r = Math.round(10.0D * Math.random());
    if (r > 0L) {
      r = r % talkList.length - 1L;
    } else if (r < 0L) {
      r = 0L;
    }
    Message msg = new Message();
    msg.setContent(talkList[((int)r)]);
    msg.setHeadImg(getHeadImg());
    msg.setSender(getId());
    msg.setNickName(getUserId());
    msg.setType("TXT");
    MessageUtils.broadcast(this.room, msg);
  }
  
  private Lottery getOpenableLottery() {
    Map<String, Lottery> lts = this.room.getLotteries().asMap();
    
    Iterator itr = lts.keySet().iterator();
    while (itr.hasNext()) {
      String key = (String)itr.next();
      Lottery lottery = (Lottery)lts.get(key);
      if (("0".equals(lottery.getStatus())) && (lottery.isOpen()) && (!lottery.isExpired())) {
        return lottery;
      }
    }
    return null;
  }
  
  public static void main(String... args)
  {
    for (;;)
    {
      System.out.println(RandomUtils.nextInt(10));
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\RobotUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */