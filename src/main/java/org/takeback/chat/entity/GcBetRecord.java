package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_bet")
public class GcBetRecord
{
  private Integer id;
  private String betType;
  private Integer uid;
  private String userId;
  private double money;
  private double freeze;
  private double bonus;
  private double userInout;
  private double addBack;
  private java.util.Date betTime;
  private java.util.Date openTime;
  private String status;
  private Integer masterRecordId;
  
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
  @Column(name="betType")
  public String getBetType() {
    return this.betType;
  }
  
  public void setBetType(String betType) {
    this.betType = betType;
  }
  
  @Basic
  @Column(name="bonus")
  public double getBonus() { return this.bonus; }
  
  public void setBonus(double bonus)
  {
    this.bonus = bonus;
  }
  
  @Basic
  @Column(name="betTime")
  public java.util.Date getBetTime() { return this.betTime; }
  
  public void setBetTime(java.util.Date betTime)
  {
    this.betTime = betTime;
  }
  
  @Basic
  @Column(name="freeze")
  public double getFreeze() { return this.freeze; }
  
  public void setFreeze(double freeze)
  {
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
  @Column(name="money")
  public double getMoney() {
    return this.money;
  }
  
  public void setMoney(double money) {
    this.money = money;
  }
  
  @Basic
  @Column(name="masterRecordId")
  public Integer getMasterRecordId() {
    return this.masterRecordId;
  }
  
  public void setMasterRecordId(Integer masterRecordId) {
    this.masterRecordId = masterRecordId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcBetRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */