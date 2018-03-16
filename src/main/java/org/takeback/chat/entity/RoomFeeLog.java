package org.takeback.chat.entity;

import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_roomFeeLog")
public class RoomFeeLog
{
  @Column
  @javax.persistence.Id
  @javax.persistence.GeneratedValue
  private String id;
  @Column
  private String roomId;
  @Column
  private String roomName;
  @Column
  private Double val;
  @Column
  private java.util.Date createDate;
  @Column
  private String admin;
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public String getRoomName() {
    return this.roomName;
  }
  
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
  
  public Double getVal() {
    return this.val;
  }
  
  public void setVal(Double val) {
    this.val = val;
  }
  
  public java.util.Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }
  
  public String getAdmin() {
    return this.admin;
  }
  
  public void setAdmin(String admin) {
    this.admin = admin;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\RoomFeeLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */