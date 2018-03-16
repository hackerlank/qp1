package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_master")
public class GcMasterRecord
{
  private Integer id;
  private Integer uid;
  private String userId;
  private double freeze;
  private double restBetable;
  private double userInout;
  private double addBack;
  private java.util.Date openTime;
  private String status;
  private String roomId;
  
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="addBack")
  public double getAddBack() {
    return this.addBack;
  }
  
  public void setAddBack(double addBack) {
    this.addBack = addBack;
  }
  
  @Basic
  @Column(name="freeze")
  public double getFreeze() {
    return this.freeze;
  }
  
  public void setFreeze(double freeze) {
    this.freeze = freeze;
  }
  
  @Basic
  @Column(name="openTime")
  public java.util.Date getOpenTime() {
    return this.openTime;
  }
  
  public void setOpenTime(java.util.Date openTime) {
    this.openTime = openTime;
  }
  
  @Basic
  @Column(name="uid")
  public Integer getUid() { return this.uid; }
  
  public void setUid(Integer uid)
  {
    this.uid = uid;
  }
  
  @Basic
  @Column(name="userId")
  public String getUserId() { return this.userId; }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Basic
  @Column(name="userInout")
  public double getUserInout() { return this.userInout; }
  
  public void setUserInout(double userInout)
  {
    this.userInout = userInout;
  }
  
  @Basic
  @Column(name="status")
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  @Basic
  @Column(name="restBetable")
  public double getRestBetable() {
    return this.restBetable;
  }
  
  public void setRestBetable(double restBetable) {
    this.restBetable = restBetable;
  }
  
  @Basic
  @Column(name="roomId")
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcMasterRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */