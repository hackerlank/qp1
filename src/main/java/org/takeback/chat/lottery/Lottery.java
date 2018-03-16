package org.takeback.chat.lottery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.lottery.listeners.RoomAndLotteryListener;
import org.takeback.chat.store.Item;

public abstract interface Lottery
  extends Item
{
  public abstract BigDecimal open(int paramInt)
    throws GameException;
  
  public abstract BigDecimal getMoney();
  
  public abstract void setMoney(BigDecimal paramBigDecimal);
  
  public abstract Integer getNumber();
  
  public abstract void setNumber(Integer paramInteger);
  
  public abstract BigDecimal fakeOpen(int paramInt)
    throws GameException;
  
  public abstract Integer getRestNumber();
  
  public abstract Integer getSender();
  
  public abstract void setSender(Integer paramInteger);
  
  public abstract BigDecimal getRestMoney();
  
  public abstract String getId();
  
  public abstract void setId(String paramString);
  
  public abstract String getRoomId();
  
  public abstract void setRoomId(String paramString);
  
  public abstract String getDescription();
  
  public abstract String getTitle();
  
  public abstract void setTitle(String paramString);
  
  public abstract void setDescription(String paramString);
  
  public abstract Date getCreateTime();
  
  public abstract void setCreateTime(Date paramDate);
  
  public abstract String getStatus();
  
  public abstract void setStatus(String paramString);
  
  public abstract boolean isOpen();
  
  public abstract boolean isExpired();
  
  public abstract void setExpiredSeconds(Integer paramInteger);
  
  public abstract Integer getExpiredSeconds();
  
  public abstract void addDetail(LotteryDetail paramLotteryDetail);
  
  public abstract Map<Integer, LotteryDetail> getDetail();
  
  public abstract void setRoomAndLotteryListener(RoomAndLotteryListener paramRoomAndLotteryListener);
  
  public abstract void finished()
    throws GameException;
  
  public abstract void expired()
    throws GameException;
  
  public abstract String getType();
  
  public abstract void setType(String paramString);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\Lottery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */