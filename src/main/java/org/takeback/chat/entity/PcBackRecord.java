package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="pc_back")
public class PcBackRecord
{
  private Integer id;
  private double money;
  private Date backDate;
  private double userInout;
  private Integer uid;
  private String userId;
  
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
  @Column(name="money")
  public double getMoney() {
    return this.money;
  }
  
  public void setMoney(double money) {
    this.money = money;
  }
  
  @Basic
  @Column(name="backDate")
  public Date getBackDate() { return this.backDate; }
  
  public void setBackDate(Date backDate)
  {
    this.backDate = backDate;
  }
  
  @Basic
  @Column(name="userInout")
  public double getUserInout() { return this.userInout; }
  
  public void setUserInout(double userInout)
  {
    this.userInout = userInout;
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
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PcBackRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */