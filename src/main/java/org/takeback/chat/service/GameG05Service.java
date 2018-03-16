package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcBetRecord;
import org.takeback.chat.entity.GcMasterRecord;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;
import org.takeback.util.exception.CodedBaseRuntimeException;

@Service("gameG05Service")
public class GameG05Service extends BaseService
{
  public static final String GET_MASTER_TEXT = "开始抢庄,谁大谁庄!";
  public static final String[] NAMES = { "牛牛", "牛①", "牛②", "牛③", "牛④", "牛⑤", "牛⑥", "牛⑦", "牛⑧", "牛⑨", "牛牛", "金牛", "对子", "顺子", "满牛", "豹子" };
  public static final String[] TUORA = { "0.12", "1.23", "2.34", "3.45", "4.56", "5.67", "6.78", "7.89" };
  public static final String[] GODEN = { "0.10", "0.20", "0.30", "0.40", "0.50", "0.60", "0.70", "0.80", "0.90" };
  @Autowired
  RoomStore roomStore;
  @Autowired
  UserStore userStore;
  
  public static String suggestNext(Integer currentStep)
  {
    if (Room.STEP_FREE.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'开始标桩'";
    if (Room.STEP_MASTER.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'标桩确认'";
    if (Room.STEP_CHECK1.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'标桩确认'";
    if (Room.STEP_CHECK2.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'标桩确认'";
    if (Room.STEP_CHECK3.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'开始下注'";
    if (Room.STEP_START_BET.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'结束下注'";
    if (Room.STEP_FINISH_BET.equals(currentStep))
      return "当前状态:" + currentStep + ",建议执行'发包'";
    if (Room.STEP_PLAY_FINISHED.equals(currentStep)) {
      return "当前状态:" + currentStep + ",建议执行'下庄或开始下注'";
    }
    return "当前命令:" + currentStep;
  }
  




  @Transactional
  public void restoreMasterMoney(Integer masterRecordId)
  {
    GcMasterRecord gmr = (GcMasterRecord)this.dao.get(GcMasterRecord.class, masterRecordId);
    int effected = this.dao.executeUpdate("update GcMasterRecord set status = '3',restBetable=0 where id =:id and status = '1' ", 
      ImmutableMap.of("id", masterRecordId));
    if (effected == 1) {
      this.dao.executeUpdate("update PubUser set money =coalesce(money,0)+:freeze where id=:id", 
        ImmutableMap.of("freeze", Double.valueOf(gmr.getFreeze()), "id", gmr.getUid()));
    }
  }
  
  @Transactional
  public void dealResult(Lottery lottery) {
    Room room = (Room)this.roomStore.get(lottery.getRoomId());
    room.setStep(Room.STEP_PLAY_FINISHED);
    
    Integer masterRecordId = room.getMasterRecordId();
    List<GcBetRecord> list = getBetRecords(masterRecordId);
    LotteryDetail masterDetail = (LotteryDetail)lottery.getDetail().get(room.getMaster());
    Integer masterPoint = NumberUtil.getDecimalPartSum4G22(masterDetail.getCoin());
    Double masterInout = Double.valueOf(0.0D);
    User master = (User)this.userStore.get(room.getMaster());
    String msg = "<table style='color:#0493b2'>";
    
    for (GcBetRecord r : list) {
      if (!r.getUid().equals(room.getMaster()))
      {

        Integer playerId = r.getUid();
        User player = (User)this.userStore.get(playerId);
        LotteryDetail playerDetail = (LotteryDetail)lottery.getDetail().get(r.getUid());
        msg = msg + "<tr><td>〖闲〗</td><td class='g021-nick-name'>" + player.getNickName() + "</td><td>(" + playerDetail.getCoin() + ")</td>";
        Integer playerPoint = NumberUtil.getDecimalPartSum4G22(playerDetail.getCoin());
        Double playerInout = Double.valueOf(0.0D);
        if ("2".equals(r.getBetType())) {
          playerInout = Double.valueOf(r.getMoney());
        } else {
          playerInout = getInout(room, masterPoint.intValue(), Double.valueOf(r.getMoney()));
        }
        
        Double playerAddBack = Double.valueOf(0.0D);
        if (masterPoint.intValue() > playerPoint.intValue()) {
          masterInout = Double.valueOf(masterInout.doubleValue() + playerInout.doubleValue());
          r.setUserInout(-playerInout.doubleValue());
          playerAddBack = Double.valueOf(r.getFreeze() - playerInout.doubleValue());
          msg = msg + "<td style='color:green;'>" + NAMES[playerPoint.intValue()] + " -" + NumberUtil.format(playerInout.doubleValue()) + "</td>";
        } else if (masterPoint.intValue() < playerPoint.intValue()) {
          masterInout = Double.valueOf(masterInout.doubleValue() - playerInout.doubleValue());
          r.setUserInout(playerInout.doubleValue());
          playerAddBack = Double.valueOf(r.getFreeze() + playerInout.doubleValue());
          msg = msg + "<td style='color:red;'>" + NAMES[playerPoint.intValue()] + "+" + NumberUtil.format(playerInout.doubleValue()) + "</td>";
        }
        else if (masterDetail.getCoin().compareTo(playerDetail.getCoin()) >= 0) {
          masterInout = Double.valueOf(masterInout.doubleValue() + playerInout.doubleValue());
          r.setUserInout(-playerInout.doubleValue());
          playerAddBack = Double.valueOf(r.getFreeze() - playerInout.doubleValue());
          msg = msg + "<td style='color:green;'>" + NAMES[playerPoint.intValue()] + " -" + NumberUtil.format(playerInout.doubleValue()) + "</td>";
        } else {
          masterInout = Double.valueOf(masterInout.doubleValue() - playerInout.doubleValue());
          r.setUserInout(playerInout.doubleValue());
          playerAddBack = Double.valueOf(r.getFreeze() + playerInout.doubleValue());
          msg = msg + "<td style='color:red;'>" + NAMES[playerPoint.intValue()] + "+" + NumberUtil.format(playerInout.doubleValue()) + "</td>";
        }
        
        msg = msg + "</tr>";
        r.setAddBack(playerAddBack.doubleValue());
        r.setStatus("1");
        this.dao.save(GcBetRecord.class, r);
        if (playerAddBack.doubleValue() > 0.0D) {
          this.dao.executeUpdate("update PubUser set money = coalesce(money,0) + :addBack where id = :uid", ImmutableMap.of("addBack", playerAddBack, "uid", playerId));
        }
        
        this.dao.executeUpdate("update GcLotteryDetail a set  a.addback =:addback,a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
          ImmutableMap.of("addback", playerAddBack, "inoutNum", playerInout, "lotteryid", lottery.getId(), "uid", playerId));
      }
    }
    msg = msg + "<tr><td  style='color:#B22222'>【庄】</td><td class='g021-nick-name'>" + master.getNickName() + "</td><td>(" + masterDetail.getCoin() + ")</td>";
    if (masterInout.doubleValue() > 0.0D) {
      msg = msg + "<td style='color:red'>" + NAMES[masterPoint.intValue()] + "+" + NumberUtil.format(masterInout.doubleValue()) + "</td></tr></table>";
    } else if (masterInout.doubleValue() < 0.0D) {
      msg = msg + "<td style='color:green'>" + NAMES[masterPoint.intValue()] + " -" + NumberUtil.format(Math.abs(masterInout.doubleValue())) + "</td></tr></table>";
    } else {
      msg = msg + "<td style='color:gray'>" + NAMES[masterPoint.intValue()] + "±平庄</td></tr></table>";
    }
    
    if (masterInout.doubleValue() != 0.0D) {
      this.dao.executeUpdate("update GcMasterRecord set freeze = coalesce(freeze,0) + :freeze , restBetable = coalesce(restBetable,0) + :freeze where id = :id", 
        ImmutableMap.of("freeze", masterInout, "id", masterRecordId));
    }
    

    this.dao.executeUpdate("update GcLotteryDetail a set  a.inoutNum = :inoutNum where a.lotteryid = :lotteryid and a.uid =:uid", 
      ImmutableMap.of("inoutNum", masterInout, "lotteryid", lottery.getId(), "uid", room.getMaster()));
    
    Message roundMsg = new Message("TXT_SYS", room.getManager(), msg);
    MessageUtils.broadcastDelay(room, roundMsg, 1L);
    
    String masterTxt = buildMasterMoney(room);
    PubUser manager = (PubUser)this.dao.get(PubUser.class, room.getOwner());
    Message masterMsg = new Message("TXT", manager.getId(), masterTxt);
    



    masterMsg.setHeadImg(manager.getHeadImg());
    masterMsg.setNickName(manager.getNickName());
    MessageUtils.broadcastDelay(room, masterMsg, 3L);
  }
  
  public String buildMasterMoney(Room room) {
    Integer masterRecordId = room.getMasterRecordId();
    Integer masterId = room.getMaster();
    GcMasterRecord gmr = (GcMasterRecord)this.dao.get(GcMasterRecord.class, masterRecordId);
    User master = (User)this.userStore.get(masterId);
    Double maxTypes = Double.valueOf(room.getProperties().get("conf_n15").toString());
    Double betable = Double.valueOf(NumberUtil.round(gmr.getRestBetable() / maxTypes.doubleValue()));
    

    String txt = "<table><tr><td colspan=2 align='center'><span style='color:red;font-weight:bold;font-size:22px;'>庄家金币</span></td></tr><tr><td style='color:#B22222;font-style:italic'>" + master.getNickName() + "</td><td style='color:orange;font-size:18px;font-weight:bold'>" + gmr.getFreeze() + " </td></tr><tr><td style='color:#B22222;'>最低下注</td><td style='color:green;'>" + 20 + "</td></tr><tr><td style='color:#B22222;'>最高下注</td><td style='color:yellow;'>" + 100 + "</td></tr><tr><td style='color:#B22222;'>可押注金额</td><td style='color:red;'>" + betable + "</td></tr></table>";
    



    return txt;
  }
  
  protected Double getInout(Room room, int nn, Double money) {
    Map<String, Object> map = room.getProperties();
    String key = "conf_n" + nn;
    Double types = Double.valueOf(1.0D);
    if (map.get(key) != null) {
      types = Double.valueOf(map.get(key).toString());
    }
    return Double.valueOf(money.doubleValue() * types.doubleValue());
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void bet(Room room, User user, Double money, Double freeze, Integer masterRecordId, String betType)
  {
    int effected = this.dao.executeUpdate("update GcMasterRecord  set restBetable = coalesce(restBetable,0) - :freeze where id=:id and restBetable>=:freeze", 
      ImmutableMap.of("freeze", freeze, "id", masterRecordId));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("可下金额不足!");
    }
    


    int userEffeted = this.dao.executeUpdate("update PubUser a set money = coalesce(money,0) - :money where id=:uid and money>=:money", 
      ImmutableMap.of("money", freeze, "uid", user.getId()));
    
    if (userEffeted == 0) {
      throw new CodedBaseRuntimeException("账户余额不足!");
    }
    
    GcBetRecord gbr = new GcBetRecord();
    gbr.setMoney(money.doubleValue());
    gbr.setFreeze(freeze.doubleValue());
    gbr.setBetType(betType);
    gbr.setMasterRecordId(masterRecordId);
    gbr.setUid(user.getId());
    gbr.setStatus("0");
    gbr.setUserId(user.getUserId());
    gbr.setBetTime(new Date());
    this.dao.save(GcBetRecord.class, gbr);
  }
  
  @Transactional
  public List<GcBetRecord> getBetRecords(Integer masterRecordId) {
    List l = this.dao.findByHql(" from GcBetRecord where masterRecordId = :masterRecordId and status='0' ", ImmutableMap.of("masterRecordId", masterRecordId));
    return l;
  }
  




  @Transactional
  public Integer getBetNumbers(Integer masterRecordId)
  {
    Map<String, Object> param = new HashedMap();
    param.put("masterRecordId", masterRecordId);
    List<GcBetRecord> l = this.dao.findByHql(" from GcBetRecord where masterRecordId = :masterRecordId and status = '0' group by uid", param);
    
    return Integer.valueOf(l.size());
  }
  




  @Transactional
  public boolean checkBet(Integer masterRecordId, Integer uid)
  {
    List<Long> l = this.dao.findByHql("select count(*) from GcBetRecord where masterRecordId = :masterRecordId and  uid = :uid ", ImmutableMap.of("masterRecordId", masterRecordId, "uid", uid));
    if (((Long)l.get(0)).longValue() > 0L) {
      return true;
    }
    return false;
  }
  
  @Transactional
  public GcMasterRecord checkMasterRecord(Room room)
  {
    List<GcMasterRecord> list = getMasterRecrods(room.getId());
    if (list.size() == 0) {
      throw new CodedBaseRuntimeException("无竞标记录!");
    }
    for (int i = 1; i < list.size(); i++) {
      GcMasterRecord r = (GcMasterRecord)list.get(i);
      this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) + :money where a.id=:uid and status = '0' ", 
        ImmutableMap.of("money", Double.valueOf(r.getFreeze()), "uid", r.getUid()));
      r.setStatus("3");
      r.setAddBack(r.getFreeze());
      r.setRestBetable(0.0D);
      this.dao.save(GcMasterRecord.class, r);
    }
    ((GcMasterRecord)list.get(0)).setStatus("1");
    this.dao.save(GcMasterRecord.class, list.get(0));
    return (GcMasterRecord)list.get(0);
  }
  
  @Transactional
  public void addMasterFreeze(User user, Integer masterRecordId, Double addedFreeze) {
    int userEffeted = this.dao.executeUpdate("update PubUser a set a.money = coalesce(a.money,0) - :money where a.id=:uid and a.money>=:money", 
      ImmutableMap.of("money", addedFreeze, "uid", user.getId()));
    if (userEffeted == 0) {
      throw new CodedBaseRuntimeException("账户余额不足!");
    }
    this.dao.executeUpdate("update GcMasterRecord  set freeze = coalesce(freeze,0) + :freeze, restBetable = coalesce(restBetable,0) + :freeze where id=:id", 
      ImmutableMap.of("freeze", addedFreeze, "id", masterRecordId));
  }
  
  @Transactional
  public GcMasterRecord newMasterRecord(User user, Room room, Double freeze) {
    int userEffeted = this.dao.executeUpdate("update PubUser  set money = coalesce(money,0) - :money,exp=coalesce(exp,0)+:exp where id=:uid and money>=:money", 
      ImmutableMap.of("money", freeze, "exp", freeze, "uid", user.getId()));
    if (userEffeted == 0) {
      throw new CodedBaseRuntimeException("账户余额不足!");
    }
    
    GcMasterRecord gmr = new GcMasterRecord();
    gmr.setUid(user.getId());
    gmr.setUserId(user.getUserId());
    gmr.setFreeze(freeze.doubleValue());
    gmr.setRoomId(room.getId());
    gmr.setRestBetable(freeze.doubleValue());
    gmr.setAddBack(0.0D);
    gmr.setUserInout(0.0D);
    gmr.setStatus("0");
    this.dao.save(GcMasterRecord.class, gmr);
    return gmr;
  }
  
  @Transactional
  public List<GcMasterRecord> getMasterRecrods(String roomId)
  {
    return this.dao.findByHql("from GcMasterRecord where roomId =:roomId and  status = '0' order by freeze desc", ImmutableMap.of("roomId", roomId));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\GameG05Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */