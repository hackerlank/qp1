package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_roomLog")
public class GcRoomLog
{
  private String id;
  private String roomId;
  private Integer uid;
  private Date createdate;
  
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false, length=50)
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @javax.persistence.Basic
  @Column(name="createdate", nullable=true)
  public Date getCreatedate() {
    return this.createdate;
  }
  
  public void setCreatedate(Date createdate) {
    this.createdate = createdate;
  }
  
  @javax.persistence.Basic
  @Column(name="roomId", nullable=true)
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  @javax.persistence.Basic
  @Column(name="uid", nullable=true)
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoomLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */