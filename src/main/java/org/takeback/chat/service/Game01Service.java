package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;

@org.springframework.stereotype.Service("game01Service")
public class Game01Service extends LotteryService
{
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  private GameMonitor monitor;
  
  @Transactional(rollbackFor={Throwable.class})
  public void dealResult(Lottery lottery, Room room)
  {
    Integer looserId = who(lottery, room);
    User looser = (User)this.userStore.get(looserId);
    

    clear(lottery, room, looserId);
    
    clearRoom(lottery, room);
    
    Map<String, Object> p = room.getProperties();
    Integer delay = Integer.valueOf(0);
    try {
      delay = Integer.valueOf(p.get("conf_rest_time").toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    noticeResult(room, lottery, looserId, delay);
    
    sendNew(room, looser, delay);
    String msg = "<span style='color:#B22222'>你手气糟糕,下个红包由你发出!</span>";
    MessageUtils.send(looserId, room, new Message("TXT_SYS", looserId, msg));
  }
  
  private void noticeResult(Room room, Lottery lottery, Integer looserId, Integer delay)
  {
    Integer luckyId = whoLucky(lottery);
    LotteryDetail luckyDetail = (LotteryDetail)lottery.getDetail().get(luckyId);
    User looser = (User)this.userStore.get(looserId);
    User lucky = (User)this.userStore.get(luckyId);
    LotteryDetail badLuckDetail = (LotteryDetail)lottery.getDetail().get(looserId);
    
    String msg = "最佳手气 <span style='color:red'>" + lucky.getNickName() + " (" + luckyDetail.getCoin() + "金币)</span><br>" + "手气最差 <span style='color:green'>" + looser.getNickName() + "(" + badLuckDetail.getCoin() + "金币)</span>";
    MessageUtils.broadcast(room, new Message("TXT_SYS", Integer.valueOf(0), msg));
    
    String msg1 = "<span style='color:red'><strong>" + delay + "秒后发出红包,准备开抢!</strong></span>";
    MessageUtils.broadcast(room, new Message("TXT_SYS", Integer.valueOf(0), msg1));
    if (delay.intValue() > 5) {
      Integer half = Integer.valueOf(delay.intValue() / 2);
      String msg2 = "<span style='color:red'><strong>" + (delay.intValue() - half.intValue()) + "秒倒计时,袖子抡起!</strong></span>";
      MessageUtils.broadcastDelay(room, new Message("TXT_SYS", Integer.valueOf(0), msg2), half.intValue());
    }
  }
  
  @Transactional
  public void open(Lottery lottery, User user, Double money) {
    int effected = this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) - :money,a.exp=coalesce(exp,0)+:exp where a.id=:uid and a.money>=:money", 
      ImmutableMap.of("money", money, "exp", money, "uid", user.getId()));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("金币不能少于" + money + ",请及时充值!");
    }
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    GcLotteryDetail gcLotteryDetail = new GcLotteryDetail();
    gcLotteryDetail.setLotteryid(lottery.getId());
    gcLotteryDetail.setGameType("G011");
    gcLotteryDetail.setRoomId(room.getId());
    gcLotteryDetail.setUid(user.getId());
    gcLotteryDetail.setDeposit(money.doubleValue());
    gcLotteryDetail.setAddback(0.0D);
    gcLotteryDetail.setInoutNum(0.0D);
    gcLotteryDetail.setCoin(new BigDecimal(0.0D));
    gcLotteryDetail.setCreateDate(new Date());
    gcLotteryDetail.setMasterId(lottery.getSender().intValue());
    this.dao.save(GcLotteryDetail.class, gcLotteryDetail);
  }
  




  @Transactional(rollbackFor={Throwable.class})
  public void sendNew(Room room, User looser, Integer delay)
  {
    Message message = new Message();
    message.setType("RED");
    message.setHeadImg(looser.getHeadImg());
    message.setNickName(looser.getNickName());
    
    Map<String, Object> properties = room.getProperties();
    BigDecimal money = new BigDecimal(properties.get("conf_money").toString());
    

    if ((room.getPoolAdd() != null) && (room.getPoolAdd().doubleValue() > 0.0D)) {
      money = money.subtract(new BigDecimal(room.getPoolAdd().doubleValue()));
    }
    if ((room.getFeeAdd() != null) && (room.getFeeAdd().doubleValue() > 0.0D)) {
      money = money.subtract(new BigDecimal(room.getFeeAdd().doubleValue()));
    }
    
    Integer number = Integer.valueOf(properties.get("conf_size").toString());
    String description = org.apache.commons.lang3.StringUtils.isEmpty(properties.get("conf_title").toString()) ? "恭喜发财" : properties.get("conf_title").toString();
    Integer expired = Integer.valueOf(properties.get("conf_expired").toString());
    Map<String, Object> content = new HashMap();
    content.put("money", money);
    content.put("number", number);
    content.put("description", description);
    message.setContent(content);
    Map<String, Object> body = (Map)message.getContent();
    






    Lottery red = org.takeback.chat.store.room.LotteryFactory.getDefaultBuilder(money, number).setDescription("恭喜发财,大吉大利").setSender(looser.getId().intValue()).setType("2").setRoom(room).setExpiredSeconds(expired).build();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(red, GcLottery.class);
    this.dao.save(GcLottery.class, gcLottery);
    Message redMessage = (Message)BeanUtils.map(message, Message.class);
    redMessage.setContent(red);
    redMessage.setSender(looser.getId());
    

    MessageUtils.broadcastDelay(room, redMessage, delay.intValue());
  }
  



  @Transactional(rollbackFor={Throwable.class})
  public void gameLotteryExpired(Lottery lottery, Room room)
  {
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    Map<String, Object> romProps = room.getProperties();
    BigDecimal redMoney = new BigDecimal(Double.valueOf(romProps.get("conf_money").toString()).doubleValue());
    StringBuffer hql = new StringBuffer("update PubUser a set a.money = COALESCE(a.money,0)+:money where a.id in(");
    StringBuilder sb = new StringBuilder();
    
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      sb.append(uid).append(",");
    }
    if (sb.length() > 0) {
      sb = sb.deleteCharAt(sb.length() - 1);
      hql = hql.append(sb).append(")");
      this.dao.executeUpdate(hql.toString(), ImmutableMap.of("money", Double.valueOf(redMoney.doubleValue())));
    }
    
    if (!lottery.getSender().equals(Integer.valueOf(0))) {
      BigDecimal retn = redMoney.subtract(new BigDecimal(1));
      this.dao.executeUpdate("update PubUser a set a.money = COALESCE(a.money,0)+:money where a.id=:id", ImmutableMap.of("money", Double.valueOf(retn.doubleValue()), "id", lottery.getSender()));
    }
    this.dao.executeUpdate("update GcLottery a set a.status = '2' where a.id = :id and a.status = '0'", ImmutableMap.of("id", lottery.getId()));
    this.dao.executeUpdate("update GcRoom a set a.status=0 where id =:id ", ImmutableMap.of("id", lottery.getRoomId()));
  }
  



  @Transactional(rollbackFor={Throwable.class})
  public void clear(Lottery lottery, Room room, Integer looserId)
  {
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    Map<String, Object> romProps = room.getProperties();
    BigDecimal redMoney = new BigDecimal(Double.valueOf(romProps.get("conf_money").toString()).doubleValue());
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      User user = (User)this.userStore.get(uid);
      LotteryDetail d = (LotteryDetail)detail.get(uid);
      BigDecimal money = d.getCoin();
      if (!user.getId().equals(looserId)) {
        money = money.add(redMoney);
      }
      
      String key = "b_" + money.toString();
      if (romProps.containsKey(key)) {
        Double value = Double.valueOf(romProps.get(key).toString());
        
        this.dao.executeUpdate("update GcRoom set sumPool = COALESCE(sumPool,0)-:bonus where id =:roomId ", ImmutableMap.of("bonus", value, "roomId", room.getId()));
        money = money.add(new BigDecimal(value.doubleValue()));
        String msg = "<span style='color:#B22222'>" + user.getNickName() + " 手气超好,获得奖金</span><span style='font-size:16;color:red'>￥" + value + "</span>";
        MessageUtils.broadcast(room, new Message("TXT_SYS", uid, msg));
      }
      String desc = "幸运";
      Double inout = Double.valueOf(NumberUtil.round(money.subtract(redMoney).doubleValue()));
      if (user.getId().equals(looserId)) {
        inout = Double.valueOf(NumberUtil.round(money.subtract(redMoney).doubleValue()));
        desc = "最差手气";
      }
      Map param = new HashMap();
      param.put("coin", d.getCoin());
      param.put("desc1", desc);
      param.put("addBack", Double.valueOf(NumberUtil.round(money.doubleValue())));
      param.put("inoutNum", inout);
      param.put("lotteryId", lottery.getId());
      param.put("uid", user.getId());
      
      int effected = this.dao.executeUpdate("update GcLotteryDetail set coin =:coin , desc1 =:desc1,addBack =:addBack,inoutNum=:inoutNum where lotteryId=:lotteryId and uid =:uid", param);
      if (effected == 1) {
        this.dao.executeUpdate("update PubUser set money = COALESCE(money,0)+:money ,exp = coalesce(exp,0)+:exp where id=:uid ", 
          ImmutableMap.of("money", Double.valueOf(money.doubleValue()), "exp", Double.valueOf(redMoney.doubleValue()), "uid", uid));
        this.monitor.setData(room.getId(), user.getId(), inout);
      }
    }
  }
  




  @Transactional(rollbackFor={Throwable.class})
  public void clearRoom(Lottery lottery, Room room)
  {
    if (lottery.getSender().equals(Integer.valueOf(0))) {
      return;
    }
    
    User sender = (User)this.userStore.get(lottery.getSender());
    if ("9".equals(sender.getUserType())) {
      return;
    }
    Double poolAdd = Double.valueOf(0.0D);
    Double feeAdd = Double.valueOf(0.0D);
    if ((room.getPoolAdd() != null) && (room.getPoolAdd().doubleValue() > 0.0D)) {
      poolAdd = room.getPoolAdd();
    }
    if ((room.getFeeAdd() != null) && (room.getFeeAdd().doubleValue() > 0.0D)) {
      feeAdd = room.getFeeAdd();
    }
    
    if ((poolAdd.doubleValue() == 0.0D) && (feeAdd.doubleValue() == 0.0D)) {
      return;
    }
    
    feeAdd = setProxyWater(sender, feeAdd, room.getId(), "G011", lottery.getId());
    String hql = "update GcRoom set sumPool = COALESCE(sumPool,0) + :poolAdd , sumFee = COALESCE(sumFee,0) + :feeAdd ,sumPack = COALESCE(sumPack,0)+1 where id =:roomId";
    this.dao.executeUpdate(hql, ImmutableMap.of("poolAdd", poolAdd, "feeAdd", feeAdd, "roomId", room.getId()));
  }
  
  private Integer whoLucky(Lottery lottery) {
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    Integer luckyId = null;
    BigDecimal num = null;
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      LotteryDetail d = (LotteryDetail)detail.get(uid);
      if (num == null) {
        num = d.getCoin();
        luckyId = uid;

      }
      else if (num.compareTo(d.getCoin()) < 0) {
        num = d.getCoin();
        luckyId = uid;
      }
    }
    
    return luckyId;
  }
  






  private Integer who(Lottery lottery, Room room)
  {
    Map<String, Object> romProps = room.getProperties();
    if (romProps.get("conf_looser") == null) {
      throw new CodedBaseRuntimeException("配置丢失!");
    }
    
    Integer unDead = room.getUnDead();
    String rule = romProps.get("conf_looser").toString();
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    Integer looserId = null;
    BigDecimal mem = null;
    if ("max".equals(rule))
      while (itr.hasNext()) {
        Integer uid = (Integer)itr.next();
        if (!uid.equals(unDead))
        {

          LotteryDetail d = (LotteryDetail)detail.get(uid);
          if (mem == null) {
            mem = d.getCoin();
            looserId = uid;

          }
          else if (mem.compareTo(d.getCoin()) < 0) {
            mem = d.getCoin();
            looserId = uid;
          }
        } }
    if ("min".equals(rule))
      while (itr.hasNext()) {
        Integer uid = (Integer)itr.next();
        if (!uid.equals(unDead))
        {


          LotteryDetail d = (LotteryDetail)detail.get(uid);
          if (mem == null) {
            mem = d.getCoin();
            looserId = uid;

          }
          else if (mem.compareTo(d.getCoin()) > 0) {
            mem = d.getCoin();
            looserId = uid;
          }
        } }
    if ("tail_max".equals(rule))
      while (itr.hasNext()) {
        Integer uid = (Integer)itr.next();
        if (!uid.equals(unDead))
        {

          LotteryDetail d = (LotteryDetail)detail.get(uid);
          if (mem == null) {
            mem = NumberUtil.getDecimalPart(d.getCoin());
            looserId = uid;

          }
          else if (mem.compareTo(d.getCoin()) > 0) {
            mem = NumberUtil.getDecimalPart(d.getCoin());
            looserId = uid;
          }
        } }
    if ("tail_min".equals(rule))
      while (itr.hasNext()) {
        Integer uid = (Integer)itr.next();
        if (!uid.equals(unDead))
        {

          LotteryDetail d = (LotteryDetail)detail.get(uid);
          if (mem == null) {
            mem = NumberUtil.getDecimalPart(d.getCoin());
            looserId = uid;

          }
          else if (mem.compareTo(d.getCoin()) < 0) {
            mem = mem = NumberUtil.getDecimalPart(d.getCoin());
            looserId = uid;
          }
        } }
    if (!"tail_sum_max".equals(rule))
    {
      if (!"tail_sum_min".equals(rule))
      {
        if (!"sum_max".equals(rule))
        {
          if (!"sum_min".equals(rule)) {}
        }
      }
    }
    return looserId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\Game01Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */