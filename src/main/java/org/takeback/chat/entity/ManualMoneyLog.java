package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_manualmoney")
public class ManualMoneyLog
{
  private Integer id;
  private Integer userId;
  private String userIdText;
  private Double money;
  private String des;
  private String operator;
  private Date createTime;
  
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
  @Column(name="userId", nullable=false)
  public Integer getUserId() {
    return this.userId;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  @Basic
  @Column(name="userIdText", nullable=false)
  public String getUserIdText() {
    return this.userIdText;
  }
  
  public void setUserIdText(String userIdText) {
    this.userIdText = userIdText;
  }
  
  @Basic
  @Column(name="money", nullable=false)
  public Double getMoney() {
    return this.money;
  }
  
  public void setMoney(Double money) {
    this.money = money;
  }
  
  @Basic
  @Column(name="des", nullable=false)
  public String getDes() {
    return this.des;
  }
  
  public void setDes(String des) {
    this.des = des;
  }
  
  @Basic
  @Column(name="operator", nullable=false)
  public String getOperator() {
    return this.operator;
  }
  
  public void setOperator(String operator) {
    this.operator = operator;
  }
  
  @Basic
  @Column(name="createTime", nullable=false)
  public Date getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\ManualMoneyLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */