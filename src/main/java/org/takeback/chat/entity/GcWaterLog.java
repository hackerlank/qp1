package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_waterLog")
public class GcWaterLog
{
  @javax.persistence.Id
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column
  private String id;
  @Column
  private Integer uid;
  @Column
  private String userId;
  @Column
  private String roomId;
  @Column
  private String lotteryId;
  @Column
  private String gameType;
  @Column
  private double fullWater;
  @Column
  private double water;
  @Column
  private Integer parentId;
  @Column
  private String parentUserId;
  @Column
  private Date createDate;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createdate) {
    this.createDate = createdate;
  }
  
  public String getRoomId()
  {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public Integer getUid()
  {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public String getLotteryId() {
    return this.lotteryId;
  }
  
  public void setLotteryId(String lotteryId) {
    this.lotteryId = lotteryId;
  }
  
  public String getGameType() {
    return this.gameType;
  }
  
  public void setGameType(String gameType) {
    this.gameType = gameType;
  }
  
  public double getFullWater() {
    return this.fullWater;
  }
  
  public void setFullWater(double fullWater) {
    this.fullWater = fullWater;
  }
  
  public double getWater() {
    return this.water;
  }
  
  public void setWater(double water) {
    this.water = water;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public Integer getParentId() {
    return this.parentId;
  }
  
  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }
  
  public String getParentUserId() {
    return this.parentUserId;
  }
  
  public void setParentUserId(String parentUserId) {
    this.parentUserId = parentUserId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcWaterLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */