package org.takeback.chat.store.room;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.math.RandomUtils;
import org.takeback.chat.entity.GcBetRecord;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcMasterRecord;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PcEggLog;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.service.GameG05Service;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.user.RobotUser;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.util.ApplicationContextHolder;

public class RoomThread implements Runnable
{
  LotteryService lotteryService;
  GameG05Service gameG05Service;
  org.takeback.chat.service.support.ord.HandsUpCmd handsUpCmd;
  UserStore userStore;
  public static final Integer level = Integer.valueOf(0);
  
  static Map<String, String[]> eggGroups = new HashedMap();
  static String[] group1 = { "大", "单", "大单", "极大", "红", "黄", "蓝" };
  static String[] group2 = { "大", "双", "大双", "极大", "红", "黄", "蓝" };
  static String[] group3 = { "小", "单", "小单", "极小", "红", "黄", "蓝" };
  static String[] group4 = { "小", "双", "小双", "极小", "红", "黄", "蓝" };
  
  static {
    eggGroups.put("group1", group1);
    eggGroups.put("group2", group2);
    eggGroups.put("group3", group3);
    eggGroups.put("group4", group4);
  }
  

  private Integer currentEggNumber = Integer.valueOf(0);
  private Map<Integer, Map<String, Integer>> currentBetMap = new HashedMap();
  
  public static String[] talkList = { "来个豹子...", "卧槽,今天手气好像差了点", "给我回点血吧!!!!!", "我打算收手了,草 再抢剁手了", "呵呵 我看着你剁了", "我今天看到个妹子,大街上就嘘嘘了起来,城里人真会玩", "感觉有点恶搞", "妈的老是我,,", "谁允许我唱首歌啊  这个软件啥都好,就是不能语音", "瓦里格气 我能说的只有圈圈和叉叉了", "我是一个小毛驴我从来都不齐", "好无聊啊........." };
  







  private Room room;
  






  private int masterTimes = 0;
  
  public RoomThread() {
    this.lotteryService = ((LotteryService)ApplicationContextHolder.getBean("lotteryService"));
    this.handsUpCmd = ((org.takeback.chat.service.support.ord.HandsUpCmd)ApplicationContextHolder.getBean("handsUpCmd"));
    this.gameG05Service = ((GameG05Service)ApplicationContextHolder.getBean("gameG05Service"));
    this.userStore = ((UserStore)ApplicationContextHolder.getBean("userStore"));
  }
  
  public void setRoom(Room rm) {
    this.room = rm;
  }
  
  public void run() {
    Integer random = Integer.valueOf(0);
    while (!Thread.interrupted()) {
      try {
        random = Integer.valueOf(RandomUtils.nextInt(4) + 2);
        TimeUnit.SECONDS.sleep(random.intValue() + level.intValue());
        if (random.intValue() % 2 == 0) {
          random = Integer.valueOf(random.intValue() + RandomUtils.nextInt(3));
        }
        
        if (this.room.getType().startsWith("G01")) {
          playG01();
        } else if (this.room.getType().startsWith("G02")) {
          random = Integer.valueOf(random.intValue() + RandomUtils.nextInt(2));
          playG02();
        } else if (this.room.getType().startsWith("G03")) {
          random = Integer.valueOf(random.intValue() + RandomUtils.nextInt(15) + 3);
          playG03();
        } else if (this.room.getType().startsWith("G04")) {
          random = Integer.valueOf(random.intValue() + RandomUtils.nextInt(4) + 1);
          playG04();
        } else if (this.room.getType().startsWith("G05")) {
          random = Integer.valueOf(random.intValue() + RandomUtils.nextInt(4));
          playG05();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  



  private void playG05()
  {
    User manager = (User)this.userStore.get(this.room.getOwner());
    User master = (User)this.userStore.get(this.room.getMaster());
    if (this.room.getStep() == Room.STEP_FREE) {
      this.room.setStep(Room.STEP_MASTER);
      Message msg = new Message("TXT_SYS", manager.getId(), "<span style='color:red'>=====开始标庄=====</span>");
      MessageUtils.broadcast(this.room, msg);
      return; }
    if (this.room.getStep() == Room.STEP_MASTER) {
      List<GcMasterRecord> l = this.gameG05Service.getMasterRecrods(this.room.getId());
      if (l.size() > RandomUtils.nextInt(3) + 1) {
        this.room.setStep(Room.STEP_CHECK1);
        return;
      }
      RobotUser u = pickRobotInRoom();
      Double freeze = Double.valueOf(0.0D);
      if (l.size() == 0) {
        freeze = Double.valueOf(RandomUtils.nextInt(9) * 1000 + 5000.0D);
      } else {
        freeze = Double.valueOf(((GcMasterRecord)l.get(0)).getFreeze() + RandomUtils.nextInt(9) * 100);
      }
      doGetMaster(u, freeze);
    } else { if (this.room.getStep() == Room.STEP_CHECK1) {
        this.room.setStep(Room.STEP_CHECK2);
        GcMasterRecord masterRecord = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, this.room.getMasterRecordId());
        Message msg = new Message("TXT", manager.getId(), "<span style='color:green'>确认①次</span> <span style='color:#B22222'>" + master.getNickName() + " <span style='color:orange;font-size:20px;font-weight:bold;font-style:italic'>" + masterRecord.getFreeze() + "</span>竞标,有没有更高?</span>");
        msg.setHeadImg(manager.getHeadImg());
        msg.setNickName(manager.getNickName());
        MessageUtils.broadcast(this.room, msg);
        return; }
      if (this.room.getStep() == Room.STEP_CHECK2) {
        this.room.setStep(Room.STEP_CHECK3);
        GcMasterRecord masterRecord = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, this.room.getMasterRecordId());
        Message msg = new Message("TXT", manager.getId(), "<span style='color:green'>确认②次</span> <span style='color:#B22222'>" + master.getNickName() + " <span style='color:orange;font-size:20px;font-weight:bold;font-style:italic'>" + masterRecord.getFreeze() + "</span>竞标,有没有更高?</span>");
        msg.setHeadImg(manager.getHeadImg());
        msg.setNickName(manager.getNickName());
        MessageUtils.broadcast(this.room, msg);
      } else if (this.room.getStep() == Room.STEP_CHECK3) {
        this.room.setStep(Room.STEP_START_BET);
        Double maxTypes = Double.valueOf(this.room.getProperties().get("conf_n15").toString());
        GcMasterRecord gmr = this.gameG05Service.checkMasterRecord(this.room);
        Double betable = Double.valueOf(org.takeback.chat.utils.NumberUtil.round(gmr.getFreeze() / maxTypes.doubleValue()));
        this.room.setMasterRecordId(gmr.getId());
        this.room.setMaster(gmr.getUid());
        

        String txt = "<table style='color:#B22222'><tr><td colspan=2 align='center'><span style='color:red;font-weight:bold;font-size:20px;'>标庄结束!</span></td></tr><tr><td style='font-style:italic;font-weight:bold;font-size:18px;color:green' colspan=2>" + master.getNickName() + " <strong style='color:orange;font-style:italic;font-weight:bold;'>" + gmr.getFreeze() + " 夺标</strong></tr><tr><td >最低下注</td><td style='color:green'><strong>" + 10 + "</strong></td></tr><tr><td>最高下注</td><td style='color:red'><strong>" + 100 + "</strong></td></tr><tr><td>可押注金额</td><td style='color:orange'><strong>" + betable + "</strong></td></tr></table>";
        



        Message msg = new Message("TXT", manager.getId(), txt);
        msg.setHeadImg(manager.getHeadImg());
        msg.setNickName(manager.getNickName());
        MessageUtils.broadcast(this.room, msg);
        try {
          TimeUnit.SECONDS.sleep(RandomUtils.nextInt(4) + 2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        Message msg1 = new Message("TXT_SYS", manager.getId(), "<span style='color:red'>开始下注!</span>");
        MessageUtils.broadcast(this.room, msg1);
      } else if (this.room.getStep() == Room.STEP_START_BET) {
        try {
          TimeUnit.SECONDS.sleep(RandomUtils.nextInt(4));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        List<GcBetRecord> l = this.gameG05Service.getBetRecords(this.room.getMasterRecordId());
        if (l.size() >= RandomUtils.nextInt(5) + 3) {
          this.room.setStep(Room.STEP_FINISH_BET);
          return;
        }
        RobotUser user = pickRobotInRoom();
        if (this.room.getMaster().equals(user.getId())) {
          return;
        }
        Double money = Double.valueOf(RandomUtils.nextInt(40) + 5.0D);
        Double maxTypes = Double.valueOf(this.room.getProperties().get("conf_n15").toString());
        Double deposit = Double.valueOf(money.doubleValue() * maxTypes.doubleValue());
        Integer masterRecordId = this.room.getMasterRecordId();
        this.gameG05Service.bet(this.room, user, money, deposit, masterRecordId, "1");
        String txt = "<span style='color:#B22222'>[注] </span><span style='color:orange;font-style:italic;font-weight:bold;font-size:18px;'>" + money + "</span> ";
        Message msg = new Message("TXT", user.getId(), txt);
        msg.setHeadImg(user.getHeadImg());
        msg.setNickName(user.getNickName());
        MessageUtils.broadcast(this.room, msg);
      } else if (this.room.getStep() == Room.STEP_FINISH_BET) {
        if (this.gameG05Service.getBetRecords(this.room.getMasterRecordId()).size() == 0) {
          GcMasterRecord gmr = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, this.room.getMasterRecordId());
          this.gameG05Service.restoreMasterMoney(this.room.getMasterRecordId());
          Message msg = new Message("TXT_SYS", manager.getId(), "<span style='color:#B22222'>无人下注," + gmr.getFreeze() + " 已退还庄主账户!</span>");
          MessageUtils.broadcast(this.room, msg);
          this.room.setStep(Room.STEP_FREE);
          this.room.setMaster(Integer.valueOf(0));
          this.room.setStep(Room.STEP_FREE);
          this.room.setMasterRecordId(Integer.valueOf(0));
          return;
        }
        this.room.setStep(Room.STEP_SEND_RED);
        Message msg = new Message("TXT_SYS", manager.getId(), "<span style='color:red'>截止下注!</span>");
        MessageUtils.broadcast(this.room, msg);
      } else if (this.room.getStep() == Room.STEP_SEND_RED) {
        sendRed(manager);
        this.room.setStep(Room.STEP_PLAYING);
      } else if (this.room.getStep() == Room.STEP_PLAYING) {
        Lottery lottery = getOpenableLottery();
        List<GcBetRecord> l = this.gameG05Service.getBetRecords(this.room.getMasterRecordId());
        Integer masterPlace = Integer.valueOf(RandomUtils.nextInt(l.size() - 1));
        if (masterPlace.intValue() == 0) {
          masterPlace = Integer.valueOf(1);
        }
        for (int i = 0; i < l.size(); i++) {
          GcBetRecord rec = (GcBetRecord)l.get(i);
          try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(4) + 2);
            lottery.open(rec.getUid().intValue());
            if (i == masterPlace.intValue()) {
              TimeUnit.SECONDS.sleep(RandomUtils.nextInt(4) + 2);
              lottery.open(this.room.getMaster().intValue());
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        this.room.setStep(Room.STEP_PLAY_FINISHED);
      }
      else if (this.room.getStep() == Room.STEP_PLAY_FINISHED) {
        GcMasterRecord gmr = (GcMasterRecord)this.gameG05Service.get(GcMasterRecord.class, this.room.getMasterRecordId());
        if (gmr.getFreeze() < 8000.0D) {
          this.gameG05Service.restoreMasterMoney(this.room.getMasterRecordId());
          
          Message msg = new Message("TXT_SYS", master.getId(), "<span style='color:#B22222'>" + master.getNickName() + "已下庄,剩余金币已退还账户!</span>");
          MessageUtils.broadcast(this.room, msg);
          this.room.setMaster(Integer.valueOf(0));
          this.room.setStep(Room.STEP_FREE);
          this.room.setMasterRecordId(Integer.valueOf(0));
          return;
        }
        if (RandomUtils.nextInt(10) % 3 == 0) {
          this.gameG05Service.restoreMasterMoney(this.room.getMasterRecordId());
          Message msg = new Message("TXT_SYS", master.getId(), "<span style='color:#B22222'>" + master.getNickName() + "已下庄,剩余金币已退还账户!</span>");
          MessageUtils.broadcast(this.room, msg);
          this.room.setMaster(Integer.valueOf(0));
          this.room.setStep(Room.STEP_FREE);
          this.room.setMasterRecordId(Integer.valueOf(0));
          return;
        }
        Message msg = new Message("TXT_SYS", manager.getId(), "<span style='color:red'>开始下注!</span>");
        MessageUtils.broadcast(this.room, msg);
        this.room.setStep(Room.STEP_START_BET);
        try {
          TimeUnit.SECONDS.sleep(RandomUtils.nextInt(4) + 2);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private void sendRed(User user)
  {
    Integer masterRecordId = this.room.getMasterRecordId();
    Integer number = Integer.valueOf(this.gameG05Service.getBetNumbers(masterRecordId).intValue() + 1);
    BigDecimal money = new BigDecimal(number.intValue() + 0.5D);
    Integer expiredTime = Integer.valueOf(60);
    



    LotteryFactory.DefaultLotteryBuilder builder = LotteryFactory.getDefaultBuilder(money, number).setType("2").setExpiredSeconds(expiredTime).setSender(user.getId().intValue()).setRoomId(this.room.getId());
    
    RoomAndLotteryListener listener = this.room.getRoomAndLotteryListener();
    if (listener != null) {
      try {
        if (!listener.onBeforeRed(builder)) {
          return;
        }
      } catch (GameException e) {
        return;
      }
    }
    builder.setRoom(this.room);
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
    
    Message redMessage = new Message();
    redMessage.setContent(gcLottery);
    redMessage.setSender(user.getId());
    redMessage.setType("RED");
    redMessage.setNickName(user.getNickName());
    redMessage.setHeadImg(user.getHeadImg());
    MessageUtils.broadcast(this.room, redMessage);
    
    if (listener != null) {
      try {
        listener.onRed(builder);
      } catch (GameException e) {
        return;
      }
    }
    
    this.room.setStep(Room.STEP_PLAYING);
  }
  
  private void doGetMaster(User user, Double freeze)
  {
    List<GcMasterRecord> masterRecords = this.gameG05Service.getMasterRecrods(this.room.getId());
    if (masterRecords.size() > 0) {
      GcMasterRecord maxRecord = (GcMasterRecord)masterRecords.get(0);
      Double maxFreeze = Double.valueOf(maxRecord.getFreeze());
      if (freeze.doubleValue() <= maxFreeze.doubleValue()) {
        return;
      }
      
      for (GcMasterRecord rec : masterRecords) {
        if (rec.getUid().equals(user.getId())) {
          this.gameG05Service.addMasterFreeze(user, rec.getId(), Double.valueOf(freeze.doubleValue() - rec.getFreeze()));
          String txt = "<span style='color:orange;font-style:italic;font-weight:bold;font-size:26px;'>" + freeze + "</span> <span style='color:#B22222;'>参与竞标</span>";
          Message msg = new Message("TXT", user.getId(), txt);
          msg.setHeadImg(user.getHeadImg());
          msg.setNickName(user.getNickName());
          MessageUtils.broadcast(this.room, msg);
          this.room.setMaster(user.getId());
          this.room.setMasterRecordId(rec.getId());
          return;
        }
      }
    }
    GcMasterRecord gmr = this.gameG05Service.newMasterRecord(user, this.room, freeze);
    this.room.setMaster(user.getId());
    this.room.setMasterRecordId(gmr.getId());
    String txt = "<span style='color:orange;font-style:italic;font-weight:bold;font-size:26px;'>" + freeze + "</span> <span style='color:#B22222;'>参与竞标</span>";
    Message msg = new Message("TXT", user.getId(), txt);
    msg.setHeadImg(user.getHeadImg());
    msg.setNickName(user.getNickName());
    MessageUtils.broadcast(this.room, msg);
  }
  


  private void playG01()
  {
    Map<String, Object> p = this.room.getProperties();
    if (this.room.getStatus().equals("0")) {
      Integer nums = Integer.valueOf(p.get("conf_size").toString());
      if (this.room.getUsers().size() >= nums.intValue())
      {
        startGame();
      }
    }
    else {
      Lottery lottery = getOpenableLottery();
      Integer rest = Integer.valueOf(2);
      try {
        rest = Integer.valueOf(p.get("conf_rest_time").toString());
        rest = Integer.valueOf(rest.intValue() + 1);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (lottery != null) {
        if ((new Date().getTime() - lottery.getCreateTime().getTime()) / 1000L <= rest.intValue()) {
          return;
        }
        try {
          RobotUser u = pickRobot(lottery);
          if (u == null) {
            return;
          }
          lottery.open(u.getId().intValue());
        }
        catch (GameException localGameException) {}
      }
    }
  }
  





  private RobotUser pickRobot(Lottery lottery)
  {
    Map<Integer, User> users = this.room.getUsers();
    Iterator<Integer> itr = users.keySet().iterator();
    List<RobotUser> robotsList = new java.util.ArrayList();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      User u = (User)users.get(uid);
      if (((u instanceof RobotUser)) && 
        (!lottery.getDetail().containsKey(u.getId()))) {
        robotsList.add((RobotUser)u);
      }
    }
    
    if (robotsList.size() == 0) {
      return null;
    }
    if (robotsList.size() == 1) {
      return (RobotUser)robotsList.get(0);
    }
    
    Integer r = Integer.valueOf(RandomUtils.nextInt(robotsList.size() - 1));
    return (RobotUser)robotsList.get(r.intValue());
  }
  



  public RobotUser pickRobotInRoom()
  {
    Map<Integer, User> users = this.room.getUsers();
    Iterator<Integer> itr = users.keySet().iterator();
    List<RobotUser> robotsList = new java.util.ArrayList();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      User u = (User)users.get(uid);
      if ((u instanceof RobotUser)) {
        robotsList.add((RobotUser)u);
      }
    }
    if (robotsList.size() == 0) {
      return null;
    }
    if (robotsList.size() == 1) {
      return (RobotUser)robotsList.get(0);
    }
    
    Integer r = Integer.valueOf(RandomUtils.nextInt(robotsList.size() - 1));
    return (RobotUser)robotsList.get(r.intValue());
  }
  




  private boolean startGame()
  {
    RoomAndLotteryListener listener = this.room.getRoomAndLotteryListener();
    try {
      if (listener != null) {
        if (listener.onBeforeStart(this.room)) {
          this.room.start();
          return true;
        }
      } else {
        this.room.start();
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  


  private void playG02()
  {
    Map<String, Object> p = this.room.getProperties();
    Integer masterId = this.room.getMaster();
    User master = (User)this.room.getUsers().get(masterId);
    
    Integer nums = Integer.valueOf(p.get("conf_size").toString());
    if (master == null) {
      if (this.room.getUsers().size() >= nums.intValue()) {
        startGame();
      }
    } else if ((master instanceof RobotUser)) {
      Integer r = Integer.valueOf(RandomUtils.nextInt(7) + 8);
      if (this.masterTimes < r.intValue()) {
        Lottery lottery = getOpenableLottery();
        if (lottery == null) {
          try {
            Integer sleep = Integer.valueOf(RandomUtils.nextInt(3) + 2);
            if (this.masterTimes == 0) {
              sleep = Integer.valueOf(sleep.intValue() + RandomUtils.nextInt(5));
            }
            TimeUnit.SECONDS.sleep(sleep.intValue());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Integer num = Integer.valueOf(this.room.getProperties().get("conf_size").toString());
          BigDecimal money = new BigDecimal(1 * num.intValue() + 0.5D);
          




          Lottery lottery2 = LotteryFactory.getDefaultBuilder(money, num).setType("2").setSender(master.getId().intValue()).setDescription("恭喜发财,大吉大利!").setRoom(this.room).build();
          this.room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
          GcLottery gcLottery = (GcLottery)org.takeback.util.BeanUtils.map(lottery2, GcLottery.class);
          
          Double conf_money = Double.valueOf(this.room.getProperties().get("conf_money").toString());
          Double conf_n10 = Double.valueOf(this.room.getProperties().get("conf_n15").toString());
          Double deposit = Double.valueOf(conf_money.doubleValue() * conf_n10.doubleValue());
          this.lotteryService.moneyDown(master.getId(), Double.valueOf(deposit.doubleValue() * num.intValue() + money.doubleValue()));
          
          this.lotteryService.save(GcLottery.class, gcLottery);
          try {
            lottery2.fakeOpen(master.getId().intValue());
          } catch (GameException e) {
            e.printStackTrace();
            return;
          }
          Message redMessage = new Message("RED", master.getId(), lottery2);
          redMessage.setHeadImg(master.getHeadImg());
          redMessage.setNickName(master.getNickName());
          MessageUtils.broadcast(this.room, redMessage);
          this.masterTimes += 1;
        }
      }
    }
    Long minus = Long.valueOf(System.currentTimeMillis() - this.room.getMasterStamp().longValue());
    if ((minus.longValue() / 1000L > 65L) && 
      (this.room.getUsers().size() >= nums.intValue()) && 
      (startGame())) {
      this.masterTimes = 0;
    }
    



    Lottery lottery = getOpenableLottery();
    if (lottery != null) {
      RobotUser u = pickRobot(lottery);
      if (u == null) {
        return;
      }
      if (lottery.getSender().equals(Integer.valueOf(0))) {
        try {
          TimeUnit.SECONDS.sleep(3L);
        }
        catch (Exception localException) {}
      }
      
      try
      {
        lottery.open(u.getId().intValue());
      }
      catch (GameException localGameException1) {}
    }
  }
  
  private void playG03()
  {
    PcEggLog log = org.takeback.chat.store.pcegg.PcEggStore.getStore().getLastest();
    Calendar c = Calendar.getInstance();
    c.setTime(log.getExpireTime());
    c.add(13, -30);
    if (c.getTime().compareTo(new Date()) < 0) {
      return;
    }
    
    RobotUser robot = pickRobotInRoom();
    if (robot == null) {
      return;
    }
    
    if (!this.currentEggNumber.equals(log.getId())) {
      this.currentEggNumber = log.getId();
      this.currentBetMap = new HashedMap();
    }
    
    Map<String, Integer> betRecord = (Map)this.currentBetMap.get(robot.getId());
    String key = "";
    if (betRecord != null)
    {
      String groupId = (String)betRecord.keySet().iterator().next();
      if (((Integer)betRecord.get(groupId)).intValue() >= 3) {
        return;
      }
      String[] keySet = (String[])eggGroups.get(groupId);
      key = keySet[RandomUtils.nextInt(keySet.length - 1)];
      betRecord.put(groupId, Integer.valueOf(((Integer)betRecord.get(groupId)).intValue() + 1));
    }
    else
    {
      Integer rd = Integer.valueOf(RandomUtils.nextInt(3));
      if (rd.intValue() == 0) {
        rd = Integer.valueOf(1);
      }
      String groupId = "group" + rd;
      String[] keySet = (String[])eggGroups.get(groupId);
      key = keySet[RandomUtils.nextInt(keySet.length - 1)];
      Map<String, Integer> amp = new java.util.HashMap();
      amp.put(groupId, Integer.valueOf(1));
      this.currentBetMap.put(robot.getId(), amp);
    }
    int d = (int)Math.round(Math.random() * 10.0D) + 1;
    Double money = Double.valueOf(Math.random() * 100.0D);
    




    Lottery lottery = LotteryFactory.getDefaultBuilder(new BigDecimal(money.doubleValue()), Integer.valueOf(1)).setExpiredSeconds(Integer.valueOf(1)).setType("2").setTitle(key + " " + org.takeback.chat.utils.NumberUtil.round(d * 5) + "金币").setSender(robot.getId().intValue()).setDescription(log.getId() + "期").build();
    try
    {
      lottery.open(0);
    } catch (GameException e) {
      e.printStackTrace();
    }
    Message redMessage = new Message("RED", robot.getId(), lottery);
    redMessage.setHeadImg(robot.getHeadImg());
    redMessage.setNickName(robot.getNickName());
    MessageUtils.broadcast(this.room, redMessage);
  }
  
  private void playG04() {
    Map<String, Object> p = this.room.getProperties();
    Integer size = Integer.valueOf(p.get("conf_max_size").toString());
    Double money = Double.valueOf(p.get("conf_max_money").toString());
    Double rate = this.room.getFeeAdd();
    
    Lottery lottery = getOpenableLottery();
    if (lottery != null) {
      try {
        RobotUser u = pickRobot(lottery);
        if (u != null) {
          lottery.open(u.getId().intValue());
        }
      }
      catch (GameException localGameException) {}
    }
    else {
      RobotUser u = pickRobotInRoom();
      if (u == null) {
        return;
      }
      double perRate = Double.valueOf(this.room.getProperties().get("conf_rate").toString()).doubleValue();
      if (this.room.getProperties().containsKey("p_" + size)) {
        perRate = Double.valueOf(this.room.getProperties().get("p_" + size.toString()).toString()).doubleValue();
      }
      Integer expired = Integer.valueOf(this.room.getProperties().get("conf_expired").toString());
      Integer raidPoint = Integer.valueOf(RandomUtils.nextInt(9));
      DecimalFormat df = new DecimalFormat("0.00");
      




      Lottery lottery2 = LotteryFactory.getDefaultBuilder(new BigDecimal(money.doubleValue() * (1.0D - rate.doubleValue())), size).setType("2").setSender(u.getId().intValue()).setExpiredSeconds(expired).setDescription(df.format(money.doubleValue() * (1.0D - rate.doubleValue())) + "金/雷" + raidPoint + "/" + perRate + "倍").setRoom(this.room).build();
      this.room.setMasterStamp(Long.valueOf(System.currentTimeMillis()));
      this.room.getProperties().put("raid", raidPoint);
      GcLottery gcLottery = (GcLottery)org.takeback.util.BeanUtils.map(lottery2, GcLottery.class);
      this.lotteryService.moneyUp(u.getId(), money);
      this.lotteryService.save(GcLottery.class, gcLottery);
      Message redMessage = new Message("RED", u.getId(), lottery2);
      redMessage.setHeadImg(u.getHeadImg());
      redMessage.setNickName(u.getNickName());
      MessageUtils.broadcast(this.room, redMessage);
    }
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


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\RoomThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */