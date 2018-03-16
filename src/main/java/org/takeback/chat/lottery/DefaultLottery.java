package org.takeback.chat.lottery;

import com.google.common.collect.Maps;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.service.GameMonitor;
import org.takeback.util.JSONUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;

public class DefaultLottery implements Lottery
{
  private static final Logger log = LoggerFactory.getLogger(DefaultLottery.class);
  
  private static final String ID_PREFIX = "LT";
  private Lock lock = new ReentrantLock();
  
  private String id;
  private BigDecimal money;
  private Integer number;
  private AtomicInteger restNumber;
  private BigDecimal restMoney;
  private Integer sender;
  private Random random;
  private String roomId;
  private String description;
  private String title;
  private Date createTime;
  private String status = "0";
  private String type = "1";
  private Integer expiredSeconds = Integer.valueOf(60);
  private AtomicBoolean open = new AtomicBoolean(true);
  private GameMonitor monitor;
  private Map<Integer, LotteryDetail> detail;
  private RoomAndLotteryListener roomAndLotteryListener;
  
  public DefaultLottery(BigDecimal money, Integer number)
  {
    if (money.doubleValue() / number.intValue() < 0.01D) {
      throw new CodedBaseRuntimeException(500, "lottery create failed, each is must more than 0.01");
    }
    this.id = org.takeback.util.identity.SerialNumberGenerator.generateSequenceNo("LT");
    this.money = (this.restMoney = money);
    this.number = number;
    this.restNumber = new AtomicInteger(number.intValue());
    this.random = new Random();
    this.createTime = new Date();
    this.detail = Maps.newLinkedHashMap();
    this.monitor = GameMonitor.getInstance();
  }
  
  private BigDecimal allocate(int uid)
  {
    int temp = this.restNumber.getAndDecrement();
    if (temp < 1) {
      throw new CodedBaseRuntimeException(515, String.format("lottery %s is already finished", new Object[] { this.id }));
    }
    if (temp == 1) {
      BigDecimal last = this.restMoney;
      this.restMoney = BigDecimal.ZERO;
      return last;
    }
    BigDecimal expect = null;
    if (StringUtils.isNotEmpty(this.roomId)) {
      expect = this.monitor.getOne(this.roomId, Integer.valueOf(uid));
    }
    
    BigDecimal result = null;
    if ((expect != null) && (expect.doubleValue() > 0.0D) && (expect.doubleValue() < this.restMoney.doubleValue() / 2.0D)) {
      result = expect;
      this.monitor.deleteOne(this.roomId, Integer.valueOf(uid));
    } else {
      Integer restCoin = Integer.valueOf(this.restMoney.multiply(BigDecimal.valueOf(100L)).intValue());
      Integer maxCoin = Integer.valueOf(this.restMoney.multiply(BigDecimal.valueOf(200L)).divide(BigDecimal.valueOf(temp), 0, 3).intValue());
      Integer getCoin = Integer.valueOf(this.random.nextInt(maxCoin.intValue()) + 1);
      if (restCoin.intValue() - getCoin.intValue() < temp - 1) {
        getCoin = Integer.valueOf(restCoin.intValue() - (temp - 1));
      }
      result = new BigDecimal(getCoin.intValue()).divide(BigDecimal.valueOf(100L));
    }
    this.restMoney = this.restMoney.subtract(result);
    return result;
  }
  
  public BigDecimal open(int uid) throws GameException
  {
    this.lock.lock();
    try {
      if (!isOpen()) {
        throw new GameException(510, String.format("lottery %s is already closed", new Object[] { this.id }));
      }
      
      if (0 == this.restNumber.get()) {
        throw new GameException(514, String.format("lottery %s is already finished", new Object[] { this.id }));
      }
      if (isExpired()) {
        expired();
        throw new GameException(511, String.format("lottery %s is already expired", new Object[] { this.id }));
      }
      if (this.detail.containsKey(Integer.valueOf(uid))) {
        throw new GameException(512, String.format("user %s have opend this lottery %s", new Object[] { Integer.valueOf(uid), this.id }));
      }
      if ((this.roomAndLotteryListener != null) && 
        (!this.roomAndLotteryListener.onBeforeOpen(Integer.valueOf(uid), this))) {
        throw new GameException(513, String.format("lottery %s is not allow to open", new Object[] { this.id }));
      }
      
      BigDecimal result = allocate(uid);
      LotteryDetail lotteryDetail = new LotteryDetail(Integer.valueOf(uid), result);
      this.detail.put(Integer.valueOf(uid), lotteryDetail);
      if (this.roomAndLotteryListener != null) {
        this.roomAndLotteryListener.onOpen(this, lotteryDetail);
      }
      
      if (0 == this.restNumber.get()) {
        finished();
      }
      return result;
    } finally {
      this.lock.unlock();
    }
  }
  
  public BigDecimal fakeOpen(int uid) throws GameException
  {
    this.lock.lock();
    try {
      if (0 == this.restNumber.get()) {
        finished();
        throw new GameException(514, String.format("lottery %s is already finished", new Object[] { this.id }));
      }
      if (this.detail.containsKey(Integer.valueOf(uid))) {
        throw new GameException(512, String.format("user %s have opend this lottery %s", new Object[] { Integer.valueOf(uid), this.id }));
      }
      if ((this.roomAndLotteryListener != null) && 
        (!this.roomAndLotteryListener.onBeforeOpen(Integer.valueOf(uid), this))) {
        throw new GameException(513, String.format("lottery %s is not allow to open", new Object[] { this.id }));
      }
      
      BigDecimal result = allocate(uid);
      LotteryDetail lotteryDetail = new LotteryDetail(Integer.valueOf(uid), result);
      this.detail.put(Integer.valueOf(uid), lotteryDetail);
      if (this.roomAndLotteryListener != null) {
        this.roomAndLotteryListener.onOpen(this, lotteryDetail);
      }
      if (0 == this.restNumber.get()) {
        finished();
      }
      return result;
    } finally {
      this.lock.unlock();
    }
  }
  

  public Integer getRestNumber()
  {
    return Integer.valueOf(this.restNumber.get());
  }
  
  public void finished() throws GameException
  {
    if (this.open.compareAndSet(true, false)) {
      if (this.roomAndLotteryListener != null) {
        this.roomAndLotteryListener.onFinished(this);
      }
      setStatus("1");
    }
  }
  
  public void expired() throws GameException
  {
    if (this.expiredSeconds.intValue() == 0) {
      return;
    }
    if (this.open.compareAndSet(true, false)) {
      if (this.roomAndLotteryListener != null) {
        this.roomAndLotteryListener.onExpired(this);
      }
      setStatus("2");
    }
  }
  
  public boolean isExpired()
  {
    if (this.expiredSeconds.intValue() == 0) {
      return false;
    }
    if (this.status.equals("2")) {
      return true;
    }
    LocalDateTime now = LocalDateTime.now();
    return now.minusSeconds(this.expiredSeconds.intValue()).toDate().after(this.createTime);
  }
  
  public Integer getExpiredSeconds()
  {
    return this.expiredSeconds;
  }
  
  public void setExpiredSeconds(Integer expiredSeconds)
  {
    this.expiredSeconds = expiredSeconds;
  }
  
  public void setRoomAndLotteryListener(RoomAndLotteryListener roomAndLotteryListener)
  {
    this.roomAndLotteryListener = roomAndLotteryListener;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public BigDecimal getMoney()
  {
    return this.money;
  }
  
  public void setMoney(BigDecimal money)
  {
    this.money = money;
    this.restMoney = money;
  }
  
  public Integer getNumber()
  {
    return this.number;
  }
  
  public void setNumber(Integer number)
  {
    this.number = number;
  }
  
  public Integer getSender()
  {
    return this.sender;
  }
  
  public void setSender(Integer sender)
  {
    this.sender = sender;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getRoomId()
  {
    return this.roomId;
  }
  
  public void setRoomId(String roomId)
  {
    this.roomId = roomId;
  }
  
  public String getDescription()
  {
    return StringUtils.isEmpty(this.description) ? "恭喜发财,大吉大利!" : this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public Date getCreateTime()
  {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  public BigDecimal getRestMoney()
  {
    return this.restMoney;
  }
  
  public boolean isOpen()
  {
    return this.open.get();
  }
  
  public void addDetail(LotteryDetail lotteryDetail)
  {
    this.detail.put(lotteryDetail.getUid(), lotteryDetail);
  }
  
  public Map<Integer, LotteryDetail> getDetail()
  {
    return this.detail;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("id:").append(this.id).append("-").append(this.status).append("  ");
    Map<Integer, LotteryDetail> lts = getDetail();
    
    Iterator itr = lts.keySet().iterator();
    while (itr.hasNext()) {
      Integer key = Integer.valueOf(itr.next().toString());
      LotteryDetail detail = (LotteryDetail)lts.get(key);
      if (detail != null) {
        sb.append(detail.toString()).append("\r\n");
      }
    }
    
    return sb.toString();
  }
  
  public static void main(String[] args) {
    DefaultLottery lottery = new DefaultLottery(BigDecimal.valueOf(100L), Integer.valueOf(6));
    int i = 1;
    try {
      for (;;) {
        BigDecimal result = lottery.open(i);
        System.out.println(result);
        i++;
      }
      return;
    }
    catch (Exception e) {
      System.out.println(JSONUtils.toString(lottery.getDetail()));
    }
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\DefaultLottery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */