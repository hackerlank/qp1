package org.takeback.chat.store.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.lottery.DefaultLottery;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.Item;
import org.takeback.chat.store.user.AnonymousUser;
import org.takeback.chat.store.user.User;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;
import org.takeback.util.exception.CodedBaseRuntimeException;

public class Room implements Item
{
  public static final Integer STEP_FREE = Integer.valueOf(0);
  public static final Integer STEP_MASTER = Integer.valueOf(1);
  public static final Integer STEP_CHECK1 = Integer.valueOf(2);
  public static final Integer STEP_CHECK2 = Integer.valueOf(3);
  public static final Integer STEP_CHECK3 = Integer.valueOf(4);
  public static final Integer STEP_START_BET = Integer.valueOf(5);
  public static final Integer STEP_FINISH_BET = Integer.valueOf(6);
  public static final Integer STEP_SEND_RED = Integer.valueOf(7);
  public static final Integer STEP_PLAYING = Integer.valueOf(8);
  public static final Integer STEP_PLAY_FINISHED = Integer.valueOf(9);
  
  private String id;
  private String name;
  private String catalog;
  private String type;
  private Integer owner;
  private Integer limitNum;
  private Integer hot;
  private String roomimg;
  private String description;
  private String detail;
  private String rule;
  private String psw;
  private Date createdate;
  private String status = "0";
  
  private Integer unDead;
  
  private Double shareRate;
  
  private Double sumPool;
  
  private Double poolAdd;
  private Double feeAdd;
  private Double sumFee;
  private String statusText;
  private String ownerText;
  private Map<String, Object> properties;
  private Map<Integer, User> users;
  private Map<String, AnonymousUser> guests;
  private LoadingCache<String, Lottery> lotteries;
  private Integer master = Integer.valueOf(-1);
  private Long masterStamp = Long.valueOf(0L);
  private Integer masterTimes = Integer.valueOf(0);
  
  private Integer manager = Integer.valueOf(0);
  
  private Integer step = Integer.valueOf(0);
  
  private Integer masterRecordId;
  
  private Boolean canTalk = Boolean.valueOf(true);
  private Boolean candRed = Boolean.valueOf(true);
  private ArrayList<Integer> shutup = new ArrayList();
  private RoomAndLotteryListener roomAndLotteryListener;
  
  public Room()
  {
    this.properties = Maps.newConcurrentMap();
    this.users = Maps.newConcurrentMap();
    this.guests = Maps.newConcurrentMap();
    this.lotteries = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES).build(new CacheLoader()
    {
      public Lottery load(String s) throws Exception {
        LotteryService lotteryService = (LotteryService)ApplicationContextHolder.getBean("lotteryService");
        GcLottery gcLottery = (GcLottery)lotteryService.get(GcLottery.class, s);
        if (gcLottery == null) {
          throw new CodedBaseException(530, "lottery " + s + " not exists");
        }
        Lottery lottery = new DefaultLottery(gcLottery.getMoney(), gcLottery.getNumber());
        if (Room.this.roomAndLotteryListener != null) {
          lottery.setRoomAndLotteryListener(Room.this.roomAndLotteryListener);
        }
        BeanUtils.copy(gcLottery, lottery);
        java.util.List<GcLotteryDetail> ls = lotteryService.findByProperty(GcLotteryDetail.class, "lotteryid", s);
        for (GcLotteryDetail gcLotteryDetail : ls) {
          LotteryDetail lotteryDetail = new LotteryDetail(gcLotteryDetail.getUid(), gcLotteryDetail.getCoin());
          BeanUtils.copy(gcLotteryDetail, lotteryDetail);
          lottery.addDetail(lotteryDetail);
        }
        return lottery;
      }
    });
  }
  
  public void setRoomAndLotteryListener(RoomAndLotteryListener roomAndLotteryListener) {
    this.roomAndLotteryListener = roomAndLotteryListener;
  }
  
  public RoomAndLotteryListener getRoomAndLotteryListener() {
    return this.roomAndLotteryListener;
  }
  
  public synchronized void start() throws GameException {
    if ("0".equals(this.status))
    {
      if (this.roomAndLotteryListener != null)
        this.roomAndLotteryListener.onStart(this);
    }
  }
  
  public void addLottery(Lottery lottery) {
    this.lotteries.put(lottery.getId(), lottery);
  }
  
  public Lottery getLottery(String id) {
    try {
      return (Lottery)this.lotteries.get(id);
    } catch (ExecutionException e) {}
    return null;
  }
  
  public void showLotteries()
  {
    System.out.println(">>>>>>>>>>" + this.lotteries.asMap());
  }
  
  @JsonIgnore
  public Map<Integer, User> getUsers() {
    return this.users;
  }
  
  public void setUsers(Map<Integer, User> users) {
    this.users = users;
  }
  
  @JsonIgnore
  public Map<String, AnonymousUser> getGuests() {
    return this.guests;
  }
  
  public void setGuests(Map<String, AnonymousUser> guests) {
    this.guests = guests;
  }
  
  public synchronized void join(User user) {
    if (this.users.size() < this.limitNum.intValue()) {
      this.users.put(user.getId(), user);
    } else {
      throw new CodedBaseRuntimeException(530, "room is full");
    }
  }
  
  public void left(User user) {
    this.users.remove(user.getId());
  }
  
  public void guestJoin(AnonymousUser anonymousUserPojo) {
    this.guests.put(anonymousUserPojo.getWebSocketSession().getId(), anonymousUserPojo);
  }
  
  public void guestLeft(AnonymousUser anonymousUserPojo) {
    this.guests.remove(anonymousUserPojo.getWebSocketSession().getId());
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getCatalog() {
    return this.catalog;
  }
  
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
    this.roomAndLotteryListener = ((RoomAndLotteryListener)ApplicationContextHolder.getBean(type, RoomAndLotteryListener.class));
  }
  
  public Integer getOwner() {
    return this.owner;
  }
  
  public void setOwner(Integer owner) {
    this.owner = owner;
    if (0 == owner.intValue()) {
      this.ownerText = "系统房间";
    } else {
      this.ownerText = ((Dictionary)DictionaryController.instance().get("dic.pubuser")).getText(String.valueOf(owner));
    }
  }
  
  public Integer getLimitNum() {
    return Integer.valueOf(this.limitNum == null ? 50 : this.limitNum.intValue());
  }
  
  public void setLimitNum(Integer limit) {
    this.limitNum = limit;
  }
  
  public Integer getHot() {
    return this.hot;
  }
  
  public void setHot(Integer hot) {
    this.hot = hot;
  }
  
  public String getRoomimg() {
    return this.roomimg;
  }
  
  public void setRoomimg(String roomimg) {
    this.roomimg = roomimg;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Boolean isNeedPsw() {
    return Boolean.valueOf(StringUtils.isNotEmpty(this.psw));
  }
  
  @JsonIgnore
  public String getPsw() {
    return this.psw;
  }
  
  public void setPsw(String psw) {
    this.psw = psw;
  }
  
  public Date getCreatedate() {
    return this.createdate;
  }
  
  public void setCreatedate(Date createdate) {
    this.createdate = createdate;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
    this.statusText = ((Dictionary)DictionaryController.instance().get("dic.chat.roomStatus")).getText(status);
  }
  
  public Integer getPosition() {
    return Integer.valueOf(this.users.size());
  }
  
  public String getStatusText()
  {
    return this.statusText;
  }
  
  public String getOwnerText() {
    return this.ownerText;
  }
  
  public Map<String, Object> getProperties() {
    return this.properties;
  }
  
  public LoadingCache<String, Lottery> getLotteries() {
    return this.lotteries;
  }
  
  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }
  
  public Double getFeeAdd() {
    return this.feeAdd;
  }
  
  public void setFeeAdd(Double feeAdd) {
    this.feeAdd = feeAdd;
  }
  
  public Double getPoolAdd() {
    return this.poolAdd;
  }
  
  public void setPoolAdd(Double poolAdd) {
    this.poolAdd = poolAdd;
  }
  
  public Double getSumFee() {
    return this.sumFee;
  }
  
  public void setSumFee(Double sumFee) {
    this.sumFee = sumFee;
  }
  
  public Double getSumPool() {
    return this.sumPool;
  }
  
  public void setSumPool(Double sumPool) {
    this.sumPool = sumPool;
  }
  
  public Double getShareRate() {
    return this.shareRate;
  }
  
  public void setShareRate(Double shareRate) {
    this.shareRate = shareRate;
  }
  
  @javax.persistence.Basic
  @Column(name="rule", nullable=true)
  public String getRule() {
    return this.rule;
  }
  
  public void setRule(String rule) {
    this.rule = rule;
  }
  
  public ArrayList<Integer> getShutup() {
    return this.shutup;
  }
  
  public void setShutup(ArrayList<Integer> shutup) {
    this.shutup = shutup;
  }
  
  public Integer getMaster() {
    return this.master;
  }
  
  public void setMaster(Integer master) {
    this.master = master;
  }
  
  public Integer getMasterTimes() {
    return this.masterTimes;
  }
  
  public void setMasterTimes(Integer masterTimes) {
    this.masterTimes = masterTimes;
  }
  
  public Boolean getCanTalk() {
    return this.canTalk;
  }
  
  public void setCanTalk(Boolean canTalk) {
    this.canTalk = canTalk;
  }
  
  public Boolean getCandRed() {
    return this.candRed;
  }
  
  public void setCandRed(Boolean candRed) {
    this.candRed = candRed;
  }
  
  public String getDetail() {
    return this.detail;
  }
  
  public void setDetail(String detail) {
    this.detail = detail;
  }
  
  public Long getMasterStamp() {
    return this.masterStamp;
  }
  
  public void setMasterStamp(Long masterStamp) {
    this.masterStamp = masterStamp;
  }
  
  public Integer getUnDead() {
    return this.unDead;
  }
  
  public void setUnDead(Integer unDead) {
    this.unDead = unDead;
  }
  
  public Integer getManager() {
    return this.manager;
  }
  
  public void setManager(Integer manager) {
    this.manager = manager;
  }
  
  public Integer getStep() {
    return this.step;
  }
  
  public void setStep(Integer step) {
    this.step = step;
  }
  
  public Integer getMasterRecordId() {
    return this.masterRecordId;
  }
  
  public void setMasterRecordId(Integer masterRecordId) {
    this.masterRecordId = masterRecordId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\Room.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */