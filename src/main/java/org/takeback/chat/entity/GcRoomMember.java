package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_room_member")
public class GcRoomMember
{
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column
  private Integer id;
  @Basic
  @Column
  private String roomId;
  @Basic
  @Column
  private Double rate;
  @Basic
  @Column
  private Integer uid;
  @Basic
  @Column
  private String userId;
  @Basic
  @Column
  private String nickName;
  @Basic
  @Column
  private String isPartner;
  @Basic
  @Column
  private java.util.Date joinDate;
  
  public Integer getId()
  {
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
  
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  
  public String getIsPartner() {
    return this.isPartner;
  }
  
  public void setIsPartner(String isPartner) {
    this.isPartner = isPartner;
  }
  
  public java.util.Date getJoinDate() {
    return this.joinDate;
  }
  
  public void setJoinDate(java.util.Date joinDate) {
    this.joinDate = joinDate;
  }
  
  public Double getRate() {
    return this.rate;
  }
  
  public void setRate(Double rate) {
    this.rate = rate;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoomMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */