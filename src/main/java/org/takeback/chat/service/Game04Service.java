package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;


@Service("game04Service")
public class Game04Service
  extends LotteryService
{
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  GameMonitor monitor;
  
  @Transactional(rollbackFor={Throwable.class})
  public void saveDetail(Lottery lottery, LotteryDetail detail, Double userInout)
  {
    GcLotteryDetail gcLotteryDetail = (GcLotteryDetail)BeanUtils.map(detail, GcLotteryDetail.class);
    gcLotteryDetail.setLotteryid(lottery.getId());
    gcLotteryDetail.setGameType("G04");
    
    Room r = (Room)this.roomStore.get(lottery.getRoomId());
    Double rate = r.getFeeAdd();
    Double deposit = Double.valueOf(lottery.getMoney().doubleValue() / (1.0D - rate.doubleValue()));
    
    gcLotteryDetail.setDeposit(deposit.doubleValue());
    if (userInout.doubleValue() > 0.0D) {
      gcLotteryDetail.setDesc1("无雷");
      gcLotteryDetail.setAddback(NumberUtil.round(deposit.doubleValue() + userInout.doubleValue()));
      gcLotteryDetail.setInoutNum(NumberUtil.round(userInout.doubleValue()));
    } else if (userInout.doubleValue() < 0.0D) {
      gcLotteryDetail.setDesc1("中雷");
      gcLotteryDetail.setAddback(detail.getCoin().doubleValue());
      gcLotteryDetail.setInoutNum(NumberUtil.round(userInout.doubleValue()));
    }
    


    this.monitor.setData(lottery.getRoomId(), detail.getUid(), userInout);
    
    gcLotteryDetail.setRoomId(lottery.getRoomId());
    gcLotteryDetail.setMasterId(lottery.getSender().intValue());
    this.dao.save(GcLotteryDetail.class, gcLotteryDetail);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void gameStop(Lottery lottery) {
    this.dao.executeUpdate("update GcRoom a set a.status=0 where id=:id", ImmutableMap.of("id", lottery.getRoomId()));
    this.dao.executeUpdate("update GcLottery a set a.status=2 where id=:id", ImmutableMap.of("id", lottery.getId()));
  }
  




  @Transactional
  public void setWater(String roomId, User child, Double water, String lotteryId)
  {
    water = setProxyWater(child, water, roomId, "G04", lotteryId);
    waterUp(roomId, water);
  }
  
  public void setMasterMonitorData(Lottery lottery) {
    Map<Integer, LotteryDetail> detail = lottery.getDetail();
    Iterator itr = detail.keySet().iterator();
    
    Room r = (Room)this.roomStore.get(lottery.getRoomId());
    Double rate = r.getFeeAdd();
    Double deposit = Double.valueOf(lottery.getMoney().doubleValue() / (1.0D - rate.doubleValue()));
    
    String raidStr = lottery.getDescription().charAt(lottery.getDescription().indexOf("雷") + 1) + "";
    
    Integer raidPoint = Integer.valueOf(raidStr);
    Double masterInout = Double.valueOf(-(deposit.doubleValue() - lottery.getRestMoney().doubleValue()));
    if (lottery.getRestNumber() == lottery.getNumber()) {
      masterInout = Double.valueOf(0.0D);
    }
    


    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      LotteryDetail d = (LotteryDetail)detail.get(uid);
      Integer tailPoint = NumberUtil.getTailPoint(d.getCoin());
      if (!d.getUid().equals(lottery.getSender()))
      {

        if (tailPoint.equals(raidPoint)) {
          masterInout = Double.valueOf(masterInout.doubleValue() + deposit.doubleValue());
        }
      }
    }
    this.monitor.setData(lottery.getRoomId(), lottery.getSender(), masterInout);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\Game04Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */