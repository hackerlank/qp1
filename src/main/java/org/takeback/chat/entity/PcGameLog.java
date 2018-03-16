package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@javax.persistence.Table(name="pc_gamelog")
public class PcGameLog
{
  private Integer id;
  private Integer num;
  private String betType;
  private String bet;
  private String luckyNumber;
  private Integer uid;
  private String userId;
  private Integer parentId;
  private double freeze;
  private double bonus;
  private double userInout;
  private double addBack;
  private double backMoney;
  private Date betTime;
  private Date openTime;
  private String status;
  
  @Id
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
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
  @Column(name="backMoney")
  public double getBackMoney() { return this.backMoney; }
  
  public void setBackMoney(double backMoney)
  {
    this.backMoney = backMoney;
  }
  
  @Basic
  @Column(name="bet")
  public String getBet() { return this.bet; }
  
  public void setBet(String bet)
  {
    this.bet = bet;
  }
  
  @Basic
  @Column(name="betType")
  public String getBetType() { return this.betType; }
  
  public void setBetType(String betType)
  {
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
  public Date getBetTime() { return this.betTime; }
  
  public void setBetTime(Date betTime)
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
  @Column(name="luckyNumber")
  public String getLuckyNumber() { return this.luckyNumber; }
  
  public void setLuckyNumber(String luckyNumber)
  {
    this.luckyNumber = luckyNumber;
  }
  
  @Basic
  @Column(name="num")
  public Integer getNum() { return this.num; }
  
  public void setNum(Integer num)
  {
    this.num = num;
  }
  
  @Basic
  @Column(name="openTime")
  public Date getOpenTime() { return this.openTime; }
  
  public void setOpenTime(Date openTime)
  {
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
  @Column(name="parentId")
  public Integer getParentId() { return this.parentId; }
  
  public void setParentId(Integer parentId)
  {
    this.parentId = parentId;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PcGameLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */