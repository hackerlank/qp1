package org.takeback.chat.entity;

import java.util.Date;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.utils.NumberUtil;





public class ControlModel
  implements Comparable
{
  static Long IDX = Long.valueOf(1L);
  
  Long id;
  
  Date startDate;
  
  Date lastModifyDate;
  
  String roomId;
  String roomName;
  Integer uid;
  String userId;
  String nickName;
  Double targetRate = Double.valueOf(0.0D);
  String targetRateText = "";
  
  Double currentRate = Double.valueOf(0.0D);
  String currentRateText = "";
  
  Double inoutNum = Double.valueOf(0.0D);
  Double win = Double.valueOf(0.0D);
  Double lose = Double.valueOf(0.0D);
  Integer playTimes = Integer.valueOf(0);
  String suggests;
  
  public ControlModel(String roomId, String roomName, Integer uid, String userId, String nickName, Double targetRate)
  {
    Long localLong1 = IDX;Long localLong2 = IDX = Long.valueOf(IDX.longValue() + 1L);this.id = localLong1;
    this.roomId = roomId;
    this.uid = uid;
    this.roomName = roomName;
    this.userId = userId;
    this.nickName = nickName;
    this.targetRate = targetRate;
    this.targetRateText = (NumberUtil.round(targetRate.doubleValue() * 100.0D) + "%");
    this.startDate = new Date();
    this.lastModifyDate = new Date();
  }
  
  public Date getStartDate(Room room) {
    return this.startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public Date getLastModifyDate() {
    return this.lastModifyDate;
  }
  
  public void setLastModifyDate(Date lastModifyDate) {
    this.lastModifyDate = lastModifyDate;
  }
  
  public Double getTargetRate() {
    return Double.valueOf(NumberUtil.round(this.targetRate.doubleValue()));
  }
  
  public void setTargetRate(Double targetRate) {
    this.targetRate = targetRate;
    this.targetRateText = (NumberUtil.round(targetRate.doubleValue() * 100.0D) + "%");
  }
  
  public Double getCurrentRate() {
    return Double.valueOf(NumberUtil.round(this.currentRate.doubleValue()));
  }
  
  public void setCurrentRate(Double currentRate) {
    this.currentRate = currentRate;
  }
  
  public Double getInoutNum() {
    return Double.valueOf(NumberUtil.round(this.inoutNum.doubleValue()));
  }
  
  public void setInoutNum(Double inoutNum) {
    ControlModel localControlModel = this;(localControlModel.inoutNum = Double.valueOf(localControlModel.inoutNum.doubleValue() + inoutNum.doubleValue()));
    if (inoutNum.doubleValue() >= 0.0D) {
      localControlModel = this;(localControlModel.win = Double.valueOf(localControlModel.win.doubleValue() + inoutNum.doubleValue()));
    } else {
      localControlModel = this;(localControlModel.lose = Double.valueOf(localControlModel.lose.doubleValue() + inoutNum.doubleValue()));
    }
    localControlModel = this;Integer localInteger1 = localControlModel.playTimes;Integer localInteger2 = localControlModel.playTimes = Integer.valueOf(localControlModel.playTimes.intValue() + 1);
    this.lastModifyDate = new Date();
    this.currentRate = Double.valueOf(this.win.doubleValue() / (this.win.doubleValue() - this.lose.doubleValue()));
    this.currentRateText = (NumberUtil.round(this.currentRate.doubleValue() * 100.0D) + "%");
  }
  
  public Double getWin() {
    return Double.valueOf(NumberUtil.round(this.win.doubleValue()));
  }
  
  public void setWin(Double win) {
    this.win = win;
  }
  
  public Double getLose() {
    return Double.valueOf(NumberUtil.round(this.lose.doubleValue()));
  }
  
  public void setLose(Double lose) {
    this.lose = lose;
  }
  
  public Integer getPlayTimes() {
    return this.playTimes;
  }
  
  public void setPlayTimes(Integer playTimes) {
    this.playTimes = playTimes;
  }
  
  public String getSuggests() {
    return this.suggests;
  }
  
  public void setSuggests(String suggests) {
    this.suggests = suggests;
  }
  
  public Date getStartDate() {
    return this.startDate;
  }
  
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public int compareTo(Object o)
  {
    ControlModel cm = (ControlModel)o;
    return (int)(cm.getLastModifyDate().getTime() - this.lastModifyDate.getTime());
  }
  
  public String getRoomName() {
    return this.roomName;
  }
  
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getTargetRateText() {
    return this.targetRateText;
  }
  
  public void setTargetRateText(String targetRateText) {
    this.targetRateText = targetRateText;
  }
  
  public String getCurrentRateText() {
    return this.currentRateText;
  }
  
  public void setCurrentRateText(String currentRateText) {
    this.currentRateText = currentRateText;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\ControlModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */