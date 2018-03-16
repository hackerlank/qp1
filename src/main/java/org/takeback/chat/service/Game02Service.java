package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;



@Service("game02Service")
public class Game02Service
  extends LotteryService
{
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  private GameMonitor monitor;
  
  @Transactional
  public void returnMasterLoteryMoney(Lottery lottery, double deposit)
  {
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    StringBuffer hql = new StringBuffer("update PubUser a set a.money = COALESCE(a.money,0)+:money where a.id in(");
    StringBuilder sb = new StringBuilder();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      sb.append(uid).append(",");
      this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,desc1='抢庄' where a.lotteryid = :lotteryid and a.uid =:uid", 
        ImmutableMap.of("addback", Double.valueOf(deposit), "lotteryid", lottery.getId(), "uid", uid));
    }
    if (sb.length() > 0) {
      sb = sb.deleteCharAt(sb.length() - 1);
      hql = hql.append(sb).append(")");
      this.dao.executeUpdate(hql.toString(), ImmutableMap.of("money", Double.valueOf(deposit)));
    }
  }
  
  @Transactional
  public void open(Lottery lottery, Integer uid, Double money) {
    int effected = this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) - :money,a.exp=coalesce(exp,0)+:exp where a.id=:uid and a.money>=:money", 
      ImmutableMap.of("money", money, "exp", money, "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("金币不能少于" + money + ",请及时充值!");
    }
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    GcLotteryDetail gcLotteryDetail = new GcLotteryDetail();
    gcLotteryDetail.setLotteryid(lottery.getId());
    gcLotteryDetail.setGameType("G011");
    gcLotteryDetail.setRoomId(room.getId());
    gcLotteryDetail.setUid(uid);
    gcLotteryDetail.setDeposit(money.doubleValue());
    gcLotteryDetail.setAddback(0.0D);
    gcLotteryDetail.setInoutNum(0.0D);
    gcLotteryDetail.setCoin(new BigDecimal(0.0D));
    gcLotteryDetail.setCreateDate(new Date());
    gcLotteryDetail.setMasterId(lottery.getSender().intValue());
    this.dao.save(GcLotteryDetail.class, gcLotteryDetail);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int moneyDown(Integer uid, Double money) {
    return this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) - :money,a.exp=coalesce(exp,0)+:exp where a.id=:uid and a.money>=:money", ImmutableMap.of("money", money, "exp", money, "uid", uid));
  }
  





  @Transactional(rollbackFor={Throwable.class})
  public void saveDetail(Lottery lottery, LotteryDetail detail, double deposit, String gameType)
  {
    GcLotteryDetail gcLotteryDetail = (GcLotteryDetail)BeanUtils.map(detail, GcLotteryDetail.class);
    gcLotteryDetail.setLotteryid(lottery.getId());
    gcLotteryDetail.setGameType(gameType);
    gcLotteryDetail.setDeposit(deposit);
    gcLotteryDetail.setDesc1("牛" + NumberUtil.getPoint(detail.getCoin()));
    gcLotteryDetail.setRoomId(lottery.getRoomId());
    gcLotteryDetail.setMasterId(lottery.getSender().intValue());
    this.dao.save(GcLotteryDetail.class, gcLotteryDetail);
  }
  



























  @Transactional(rollbackFor={Throwable.class})
  public void gameStop(Lottery lottery)
  {
    this.dao.executeUpdate("update GcRoom a set a.status=0 where id=:id", ImmutableMap.of("id", lottery.getRoomId()));
    this.dao.executeUpdate("update GcLottery a set a.status=2 where id=:id", ImmutableMap.of("id", lottery.getId()));
  }
  




  public void dealMaster(Lottery lottery)
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
  
  @Transactional(rollbackFor={Throwable.class})
  public void dealGame(Lottery lottery) throws GameException {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStatus("0");
    Integer masterId = lottery.getSender();
    User master = (User)this.userStore.get(masterId);
    LotteryDetail masterDetail = getMasterDetail(lottery);
    Integer masterPoint = NumberUtil.getDecimalPartSum4G22(masterDetail.getCoin());
    BigDecimal masterInout = new BigDecimal(0.0D);
    Map<Integer, LotteryDetail> details = lottery.getDetail();
    StringBuilder msg = new StringBuilder("<table style='color:#0493b2'>");
    BigDecimal water = new BigDecimal(0.0D);
    BigDecimal rate = new BigDecimal(room.getFeeAdd().doubleValue());
    for (LotteryDetail ld : details.values()) {
      if (!ld.getUid().equals(masterId))
      {

        User player = (User)this.userStore.get(ld.getUid());
        Integer playerPoint = NumberUtil.getDecimalPartSum4G22(ld.getCoin());
        
        Integer losePoint = Integer.valueOf(getConifg(room.getId(), "conf_lose"));
        msg.append("<tr><td>〖闲〗</td><td class='g021-nick-name'>").append(player.getNickName()).append("</td><td>(").append(ld.getCoin()).append(")</td>");
        if ((masterPoint.intValue() > playerPoint.intValue()) || (playerPoint.intValue() <= losePoint.intValue())) {
          BigDecimal inout = getInout(room, masterPoint.intValue());
          this.monitor.setData(lottery.getRoomId(), player.getId(), Double.valueOf(-inout.doubleValue()));
          msg.append("<td style='color:green;'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[playerPoint.intValue()]).append(" -").append(NumberUtil.format(inout)).append("</td>");
          masterInout = masterInout.add(inout);
          
          Double addBack = Double.valueOf(getDeposit(room) - inout.doubleValue() + ld.getCoin().doubleValue());
          this.dao.executeUpdate("update PubUser a set a.money =a.money + :money,a.exp=coalesce(a.exp,0)+:exp where a.id = :uid ", ImmutableMap.of("money", addBack, "exp", Double.valueOf(Math.abs(inout.doubleValue())), "uid", player.getId()));
          this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
            ImmutableMap.of("addback", Double.valueOf(addBack.doubleValue()), "inoutNum", Double.valueOf(-inout.subtract(ld.getCoin()).doubleValue()), "lotteryid", lottery.getId(), "uid", player.getId()));
        }
        else if (masterPoint.intValue() < playerPoint.intValue()) {
          BigDecimal inout = getInout(room, playerPoint.intValue());
          masterInout = masterInout.subtract(inout);
          msg.append("<td style='color:red;'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[playerPoint.intValue()]).append("+").append(NumberUtil.format(inout)).append("</td>");
          if (false == "9".equals(player.getUserType())) {
            BigDecimal subWater = inout.multiply(rate);
            inout = inout.subtract(inout.multiply(rate));
            BigDecimal roomWater = new BigDecimal(setProxyWater(player, Double.valueOf(subWater.doubleValue()), room.getId(), "G022", lottery.getId()).doubleValue());
            water = water.add(roomWater);
          }
          this.monitor.setData(lottery.getRoomId(), player.getId(), Double.valueOf(inout.doubleValue()));
          
          Double addBack = Double.valueOf(getDeposit(room) + inout.add(ld.getCoin()).doubleValue());
          this.dao.executeUpdate("update PubUser a set a.money =a.money + :money,a.exp=coalesce(a.exp,0)+:exp where a.id = :uid ", ImmutableMap.of("money", addBack, "exp", Double.valueOf(Math.abs(inout.doubleValue())), "uid", player.getId()));
          this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
            ImmutableMap.of("addback", Double.valueOf(addBack.doubleValue()), "inoutNum", Double.valueOf(inout.add(ld.getCoin()).doubleValue()), "lotteryid", lottery.getId(), "uid", player.getId()));

        }
        else if (masterDetail.getCoin().compareTo(ld.getCoin()) >= 0) {
          BigDecimal inout = getInout(room, masterPoint.intValue());
          this.monitor.setData(lottery.getRoomId(), player.getId(), Double.valueOf(-inout.doubleValue()));
          msg.append("<td style='color:green;'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[playerPoint.intValue()]).append(" -").append(NumberUtil.format(inout)).append("</td>");
          masterInout = masterInout.add(inout);
          
          Double addBack = Double.valueOf(getDeposit(room) - inout.doubleValue() + ld.getCoin().doubleValue());
          this.dao.executeUpdate("update PubUser a set a.money =a.money + :money,a.exp=coalesce(a.exp,0)+:exp where a.id = :uid ", ImmutableMap.of("money", addBack, "exp", Double.valueOf(Math.abs(inout.doubleValue())), "uid", player.getId()));
          this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
            ImmutableMap.of("addback", Double.valueOf(addBack.doubleValue()), "inoutNum", Double.valueOf(-inout.subtract(ld.getCoin()).doubleValue()), "lotteryid", lottery.getId(), "uid", player.getId()));
        } else {
          BigDecimal inout = getInout(room, playerPoint.intValue());
          masterInout = masterInout.subtract(inout);
          msg.append("<td style='color:red;'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[playerPoint.intValue()]).append("+").append(NumberUtil.format(inout)).append("</td>");
          if (false == "9".equals(player.getUserType())) {
            BigDecimal subWater = inout.multiply(rate);
            inout = inout.subtract(inout.multiply(rate));
            BigDecimal roomWater = new BigDecimal(setProxyWater(player, Double.valueOf(subWater.doubleValue()), room.getId(), "G022", lottery.getId()).doubleValue());
            water = water.add(roomWater);
          }
          this.monitor.setData(lottery.getRoomId(), player.getId(), Double.valueOf(inout.doubleValue()));
          
          Double addBack = Double.valueOf(getDeposit(room) + inout.add(ld.getCoin()).doubleValue());
          this.dao.executeUpdate("update PubUser a set a.money =a.money + :money,a.exp=coalesce(a.exp,0)+:exp where a.id = :uid ", ImmutableMap.of("money", addBack, "exp", Double.valueOf(Math.abs(inout.doubleValue())), "uid", player.getId()));
          this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
            ImmutableMap.of("addback", Double.valueOf(addBack.doubleValue()), "inoutNum", Double.valueOf(inout.add(ld.getCoin()).doubleValue()), "lotteryid", lottery.getId(), "uid", player.getId()));
        }
        








        msg.append("</tr>");
      }
    }
    msg.append("<tr><td  style='color:#B22222'>【庄】</td><td class='g021-nick-name'>").append(master.getNickName()).append("</td><td>(").append(masterDetail.getCoin()).append(")</td>");
    if (masterInout.compareTo(new BigDecimal(0)) > 0) {
      msg.append("<td style='color:red'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[masterPoint.intValue()]).append("+").append(NumberUtil.format(masterInout)).append("</td>");
    } else if (masterInout.compareTo(new BigDecimal(0)) < 0) {
      msg.append("<td style='color:green'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[masterPoint.intValue()]).append(" -").append(NumberUtil.format(Math.abs(masterInout.doubleValue()))).append("</td></tr>");
    } else {
      msg.append("<td style='color:gray'>").append(org.takeback.chat.lottery.listeners.G02.NAMES[masterPoint.intValue()]).append("±平庄</td></tr>");
    }
    

    if ((masterInout.doubleValue() > 0.0D) && (false == "9".equals(master.getUserType()))) {
      BigDecimal subWater = masterInout.multiply(rate);
      masterInout = masterInout.subtract(subWater);
      BigDecimal roomWater = new BigDecimal(setProxyWater(master, Double.valueOf(subWater.doubleValue()), room.getId(), "G022", lottery.getId()).doubleValue());
      water = water.add(roomWater);
    }
    masterInout = masterInout.add(masterDetail.getCoin());
    msg.append("</table>");
    
    masterInout = masterInout.add(lottery.getRestMoney());
    this.monitor.setData(lottery.getRoomId(), master.getId(), Double.valueOf(masterInout.doubleValue()));
    
    Double masterAddBack = Double.valueOf(masterInout.add(new BigDecimal(getDeposit(room) * lottery.getNumber().intValue())).doubleValue());
    this.dao.executeUpdate("update PubUser a set a.money =a.money + :money,a.exp=coalesce(a.exp,0)+:exp where a.id = :uid ", ImmutableMap.of("money", masterAddBack, "exp", Double.valueOf(Math.abs(masterInout.doubleValue())), "uid", masterId));
    this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
      ImmutableMap.of("addback", Double.valueOf(masterAddBack.doubleValue()), "inoutNum", Double.valueOf(masterInout.doubleValue()), "lotteryid", lottery.getId(), "uid", masterId));
    this.dao.executeUpdate("update GcRoom a set a.sumFee =COALESCE(a.sumFee,0) + :water,sumPack = COALESCE(sumPack,0)+1 where a.id = :roomId ", ImmutableMap.of("water", Double.valueOf(water.doubleValue()), "roomId", lottery.getRoomId()));
    

    Message rmsg = new Message("TXT_SYS", Integer.valueOf(0), msg.toString());
    MessageUtils.broadcast(room, rmsg);
  }
  
  private LotteryDetail getMasterDetail(Lottery lottery) {
    LotteryDetail lastDetail = null;
    for (LotteryDetail ld : lottery.getDetail().values()) {
      if (ld.getUid().equals(lottery.getSender())) {
        return ld;
      }
    }
    return null;
  }
  
  public BigDecimal getInout(Room room, int nn) {
    Map<String, Object> map = room.getProperties();
    String key = "conf_n" + nn;
    Double types = Double.valueOf(1.0D);
    if (map.get(key) != null) {
      types = Double.valueOf(map.get(key).toString());
    }
    Double money = Double.valueOf(map.get("conf_money").toString());
    return new BigDecimal(money.doubleValue() * types.doubleValue());
  }
  
  public double getDeposit(Room room) throws GameException {
    Double conf_money = Double.valueOf(getConifg(room.getId(), "conf_money"));
    Double conf_n10 = Double.valueOf(getConifg(room.getId(), "conf_n15"));
    return conf_money.doubleValue() * conf_n10.doubleValue();
  }
  
  public String getConifg(String roomId, String key) throws GameException {
    Room room = (Room)this.roomStore.get(roomId);
    Map<String, Object> properties = room.getProperties();
    if (properties.containsKey(key)) {
      return properties.get(key).toString();
    }
    throw new GameException(500, "缺少配置项[" + key + "]");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\Game02Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */