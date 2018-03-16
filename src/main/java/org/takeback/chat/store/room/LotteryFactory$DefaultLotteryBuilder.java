package org.takeback.chat.store.room;

import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.takeback.chat.lottery.DefaultLottery;
import org.takeback.chat.lottery.Lottery;






































public class LotteryFactory$DefaultLotteryBuilder
{
  private Lottery lottery;
  
  LotteryFactory$DefaultLotteryBuilder(BigDecimal money, Integer number)
  {
    this.lottery = new DefaultLottery(money, number);
  }
  
  public String getLotteryId() {
    return this.lottery.getId();
  }
  
  public BigDecimal getMoney() {
    return this.lottery.getMoney();
  }
  
  public int getNumber() {
    return this.lottery.getNumber().intValue();
  }
  
  public DefaultLotteryBuilder setExpiredSeconds(Integer seconds) {
    this.lottery.setExpiredSeconds(seconds);
    return this;
  }
  
  public DefaultLotteryBuilder setMoney(BigDecimal money) {
    this.lottery.setMoney(money);
    return this;
  }
  
  public int getExpiredSeconds() {
    return this.lottery.getExpiredSeconds().intValue();
  }
  
  public DefaultLotteryBuilder setType(String type) {
    this.lottery.setType(type);
    return this;
  }
  
  public String getType() {
    return this.lottery.getType();
  }
  
  public DefaultLotteryBuilder setSender(int sender) {
    this.lottery.setSender(Integer.valueOf(sender));
    return this;
  }
  
  public String getTitle() {
    return this.lottery.getTitle();
  }
  
  public DefaultLotteryBuilder setTitle(String title) {
    this.lottery.setTitle(title);
    return this;
  }
  
  public int getSender() {
    return this.lottery.getSender().intValue();
  }
  
  public DefaultLotteryBuilder setDescription(String description) {
    this.lottery.setDescription(description);
    return this;
  }
  
  public String getDescription() {
    return this.lottery.getDescription();
  }
  
  public DefaultLotteryBuilder setRoom(Room room) {
    this.lottery.setRoomId(room.getId());
    this.lottery.setRoomAndLotteryListener(room.getRoomAndLotteryListener());
    room.addLottery(this.lottery);
    return this;
  }
  
  public DefaultLotteryBuilder setRoomId(String roomId) {
    this.lottery.setRoomId(roomId);
    return this;
  }
  
  public Room getRoom() {
    if (StringUtils.isNotEmpty(this.lottery.getRoomId())) {
      return (Room)LotteryFactory.access$100().get(this.lottery.getRoomId());
    }
    return null;
  }
  
  public String getRoomId() {
    return this.lottery.getRoomId();
  }
  
  public Lottery build() {
    if ((this.lottery.getExpiredSeconds().intValue() == 0) || (StringUtils.isEmpty(this.lottery.getRoomId()))) {
      return this.lottery;
    }
    LotteryFactory.addLottery(this.lottery);
    return this.lottery;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\LotteryFactory$DefaultLotteryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */