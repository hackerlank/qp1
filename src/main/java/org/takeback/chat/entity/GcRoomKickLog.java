package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_room_kick")
public class GcRoomKickLog
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
  private Integer uid;
  @Basic
  @Column
  private String userId;
  @Basic
  @Column
  private java.util.Date kickTime;
  
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
  
  public java.util.Date getKickTime() {
    return this.kickTime;
  }
  
  public void setKickTime(java.util.Date kickTime) {
    this.kickTime = kickTime;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoomKickLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */