package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.entity.GcRoomMember;
import org.takeback.chat.entity.GcWaterLog;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;

@org.springframework.stereotype.Service("lotteryService")
public class LotteryService extends org.takeback.service.BaseService
{
  @org.springframework.beans.factory.annotation.Autowired
  org.takeback.chat.store.room.RoomStore roomStore;
  
  @Transactional(rollbackFor={Throwable.class})
  public int setLotteryExpired(String lotteryId)
  {
    return this.dao.executeUpdate("update GcLottery a set a.status = '2' where a.id = :id and a.status = '0'", ImmutableMap.of("id", lotteryId));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int setLotteryFinished(String lotteryId) {
    return this.dao.executeUpdate("update GcLottery a set a.status = '1' where a.id = :id and a.status = '0'", ImmutableMap.of("id", lotteryId));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int setRoomStatus(String roomId, String status) {
    return this.dao.executeUpdate("update GcRoom a set a.status =:status where a.id =:roomId", ImmutableMap.of("status", status, "roomId", roomId));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int moneyDown(Integer uid, Double money) {
    System.out.println("uid:" + uid + "  money:" + money);
    return this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) - :money,a.exp=coalesce(exp,0)+:exp where a.id=:uid and a.money>=:money", ImmutableMap.of("money", money, "exp", money, "uid", uid));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int moneyUp(Integer uid, Double money) {
    return this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) + :money where a.id=:uid ", ImmutableMap.of("money", money, "uid", uid));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int waterDown(String roomId, Double money) {
    return this.dao.executeUpdate("update GcRoom a set a.sumFee = coalesce(a.sumFee,0) - :money where a.id=:roomId ", ImmutableMap.of("money", money, "roomId", roomId));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int waterUp(String roomId, Double money) {
    return this.dao.executeUpdate("update GcRoom a set a.sumFee = coalesce(a.sumFee,0) + :money,a.sumPack = coalesce(a.sumPack,0)+1 where a.id=:roomId ", ImmutableMap.of("money", money, "roomId", roomId));
  }
  




  @Transactional(rollbackFor={Throwable.class})
  public void saveLotteryDetail(Lottery lottery, LotteryDetail detail)
  {
    GcLotteryDetail gcLotteryDetail = (GcLotteryDetail)BeanUtils.map(detail, GcLotteryDetail.class);
    gcLotteryDetail.setLotteryid(lottery.getId());
    gcLotteryDetail.setLotteryid(lottery.getId());
    this.dao.save(GcLotteryDetail.class, gcLotteryDetail);
    BigDecimal money = detail.getCoin();
    this.dao.executeUpdate("update PubUser set money=money+:money where id = :uid", ImmutableMap.of("money", Double.valueOf(money.doubleValue()), "uid", detail.getUid()));
  }
  




  @Transactional(rollbackFor={Throwable.class})
  public BigDecimal giftLotteryExpired(Lottery lottery)
  {
    Integer sender = lottery.getSender();
    if (sender.intValue() <= 0) {
      return null;
    }
    Map<Integer, LotteryDetail> dts = lottery.getDetail();
    java.util.Collection<LotteryDetail> c = dts.values();
    BigDecimal bd = lottery.getMoney();
    Iterator<LotteryDetail> itr = c.iterator();
    while (itr.hasNext()) {
      LotteryDetail ld = (LotteryDetail)itr.next();
      bd = bd.subtract(ld.getCoin());
    }
    this.dao.executeUpdate("update PubUser a set a.money =coalesce(a.money,0) + :rest where a.id = :uid ", ImmutableMap.of("rest", Double.valueOf(bd.doubleValue()), "uid", sender));
    this.dao.executeUpdate("update GcLottery a set a.status = '2' where a.id = :id and a.status = '0'", ImmutableMap.of("id", lottery.getId()));
    return bd;
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void changeMoney(Map<Integer, Double> data) {
    Iterator itr = data.keySet().iterator();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      Double v = Double.valueOf(((Double)data.get(uid)).doubleValue());
      this.dao.executeUpdate("update PubUser a set a.money =coalesce(a.money,0) + :money where a.id = :uid ", ImmutableMap.of("money", v, "uid", uid));
    }
  }
  



  @Transactional(rollbackFor={Throwable.class})
  public void createLottery(Lottery lottery, double deposit)
  {
    Integer uid = lottery.getSender();
    GcLottery gcLottery = (GcLottery)BeanUtils.map(lottery, GcLottery.class);
    this.dao.save(GcLottery.class, gcLottery);
    moneyDown(uid, Double.valueOf(deposit));
  }
  


  @Transactional(rollbackFor={Throwable.class})
  public Lottery loadLottery(String lotteryId)
  {
    GcLottery gcLottery = (GcLottery)this.dao.get(GcLottery.class, lotteryId);
    if (gcLottery == null) {
      return null;
    }
    Lottery lottery = (Lottery)BeanUtils.map(gcLottery, Lottery.class);
    List<GcLotteryDetail> detailList = this.dao.findByProperty(GcLotteryDetail.class, "lotteryId", lotteryId);
    for (GcLotteryDetail gld : detailList) {
      LotteryDetail detail = (LotteryDetail)BeanUtils.map(gld, LotteryDetail.class);
      lottery.addDetail(detail);
    }
    lottery.setStatus(gcLottery.getStatus());
    if ("1".equals(lottery.getStatus())) {
      try {
        lottery.finished();
      } catch (GameException e) {
        e.printStackTrace();
      }
    } else if ("2".equals(lottery.getStatus())) {
      try {
        lottery.expired();
      } catch (GameException e) {
        e.printStackTrace();
      }
    }
    

    return lottery;
  }
  


  @Transactional(rollbackFor={Throwable.class})
  public List<GcLottery> loadRecentLottery(String roomId)
  {
    List<GcLottery> lotteries = this.dao.findByHqlPaging("from GcLottery where  roomId =:roomId and status<>0 order by id desc", ImmutableMap.of("roomId", roomId), 10, 1);
    return lotteries;
  }
  
  @Transactional
  public void test() {
    List<Double> l = this.dao.findByHql("select sum(money)+(select sum(sumFee) from GcRoom ) from PubUser");
    System.out.println("站内总金额:" + l.get(0));
  }
  




  @Transactional
  public Double setProxyWater(User child, Double water, String roomId, String gameType, String lotteryId)
  {
    if (water.doubleValue() <= 0.0D) {
      return water;
    }
    Room r = (Room)this.roomStore.get(roomId);
    if (!"G022".equals(r.getType())) {
      return water;
    }
    

    Double rate = Double.valueOf(SystemConfigService.getInstance().getValue("water"));
    Double restWater = Double.valueOf(water.doubleValue() * rate.doubleValue());
    List<GcRoomMember> ls = this.dao.findByProperties(GcRoomMember.class, ImmutableMap.of("roomId", roomId, "isPartner", "1"));
    for (GcRoomMember grm : ls) {
      Double partnerRate = grm.getRate();
      if (partnerRate.doubleValue() > 0.0D) {
        Double partnerWater = Double.valueOf(water.doubleValue() * rate.doubleValue() * (partnerRate.doubleValue() / 100.0D));
        restWater = Double.valueOf(restWater.doubleValue() - partnerWater.doubleValue());
        this.dao.executeUpdate("update PubUser a set a.money =coalesce(a.money,0) + :money where a.id = :uid ", ImmutableMap.of("money", partnerWater, "uid", grm.getUid()));
        
        GcWaterLog gwl = new GcWaterLog();
        gwl.setUid(child.getId());
        gwl.setUserId(child.getUserId());
        gwl.setFullWater(water.doubleValue());
        gwl.setWater(partnerWater.doubleValue());
        gwl.setCreateDate(new java.util.Date());
        gwl.setGameType(gameType);
        gwl.setRoomId(roomId);
        gwl.setLotteryId(lotteryId);
        gwl.setParentId(grm.getUid());
        this.dao.save(GcWaterLog.class, gwl);
      }
    }
    
    if (restWater.doubleValue() > 0.0D) {
      GcWaterLog gwl = new GcWaterLog();
      gwl.setUid(child.getId());
      gwl.setUserId(child.getUserId());
      gwl.setFullWater(water.doubleValue());
      gwl.setWater(restWater.doubleValue());
      gwl.setCreateDate(new java.util.Date());
      gwl.setGameType(gameType);
      gwl.setRoomId(roomId);
      gwl.setLotteryId(lotteryId);
      gwl.setParentId(r.getOwner());
      this.dao.save(GcWaterLog.class, gwl);
      this.dao.executeUpdate("update PubUser a set a.money =coalesce(a.money,0) + :money where a.id = :uid ", ImmutableMap.of("money", restWater, "uid", r.getOwner()));
    }
    
















    return Double.valueOf(water.doubleValue() * (1.0D - rate.doubleValue()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\LotteryService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */