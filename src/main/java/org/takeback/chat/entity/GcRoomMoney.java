package org.takeback.chat.entity;

import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_room_money")
public class GcRoomMoney
{
  @javax.persistence.Id
  @Column
  @javax.persistence.GeneratedValue
  private Integer id;
  @javax.persistence.Basic
  @Column
  private String roomId;
  @javax.persistence.Basic
  @Column
  private Double restMoney;
  @javax.persistence.Basic
  @Column
  private Double totalMoney;
  
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public Double getRestMoney() {
    return this.restMoney;
  }
  
  public void setRestMoney(Double restMoney) {
    this.restMoney = restMoney;
  }
  
  public Double getTotalMoney() {
    return this.totalMoney;
  }
  
  public void setTotalMoney(Double totalMoney) {
    this.totalMoney = totalMoney;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoomMoney.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */