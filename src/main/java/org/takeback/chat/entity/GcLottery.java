package org.takeback.chat.entity;

import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_lottery")
public class GcLottery
{
  private String id;
  private java.math.BigDecimal money;
  private Integer number;
  private Integer sender;
  private String roomId;
  private String description;
  private String title;
  private java.util.Date createTime;
  private String type;
  private Integer expiredSeconds;
  private String status;
  
  @javax.persistence.Id
  @Column(name="id", nullable=false, length=50)
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @javax.persistence.Basic
  @Column(name="money", nullable=false, precision=0)
  public java.math.BigDecimal getMoney() {
    return this.money;
  }
  
  public void setMoney(java.math.BigDecimal money) {
    this.money = money;
  }
  
  @javax.persistence.Basic
  @Column(name="number", nullable=false)
  public Integer getNumber() {
    return this.number;
  }
  
  public void setNumber(Integer number) {
    this.number = number;
  }
  
  @javax.persistence.Basic
  @Column(name="sender", nullable=false)
  public Integer getSender() {
    return this.sender;
  }
  
  public void setSender(Integer sender) {
    this.sender = sender;
  }
  
  @javax.persistence.Basic
  @Column(name="roomid", nullable=false, length=50)
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomid) {
    this.roomId = roomid;
  }
  
  @javax.persistence.Basic
  @Column(name="description", nullable=true, length=200)
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  @javax.persistence.Basic
  @Column(name="createtime", nullable=false)
  public java.util.Date getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(java.util.Date createtime) {
    this.createTime = createtime;
  }
  
  @javax.persistence.Basic
  @Column(name="status", nullable=false, length=2)
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  @javax.persistence.Basic
  @Column(name="type")
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  @javax.persistence.Basic
  @Column(name="expiredSeconds")
  public Integer getExpiredSeconds() {
    return this.expiredSeconds;
  }
  
  public void setExpiredSeconds(Integer expiredSeconds) {
    this.expiredSeconds = expiredSeconds;
  }
  
  @javax.persistence.Basic
  @Column(name="title")
  public String getTitle() {
    return this.title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    GcLottery gcLottery = (GcLottery)o;
    
    if (this.id != null ? !this.id.equals(gcLottery.id) : gcLottery.id != null) return false;
    if (this.money != null ? !this.money.equals(gcLottery.money) : gcLottery.money != null) return false;
    if (this.number != null ? !this.number.equals(gcLottery.number) : gcLottery.number != null) return false;
    if (this.sender != null ? !this.sender.equals(gcLottery.sender) : gcLottery.sender != null) return false;
    if (this.roomId != null ? !this.roomId.equals(gcLottery.roomId) : gcLottery.roomId != null) return false;
    if (this.description != null ? !this.description.equals(gcLottery.description) : gcLottery.description != null)
      return false;
    if (this.createTime != null ? !this.createTime.equals(gcLottery.createTime) : gcLottery.createTime != null) return false;
    if (this.status != null ? !this.status.equals(gcLottery.status) : gcLottery.status != null) { return false;
    }
    return true;
  }
  


  public int hashCode()
  {
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.money != null ? this.money.hashCode() : 0);
    result = 31 * result + (this.number != null ? this.number.hashCode() : 0);
    result = 31 * result + (this.sender != null ? this.sender.hashCode() : 0);
    result = 31 * result + (this.roomId != null ? this.roomId.hashCode() : 0);
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.createTime != null ? this.createTime.hashCode() : 0);
    result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcLottery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */