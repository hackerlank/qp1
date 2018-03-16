package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_valueControl")
public class ValueControlLog
{
  @Column
  @javax.persistence.Id
  @javax.persistence.GeneratedValue
  private String id;
  @Column
  private Integer uid;
  @Column
  private String nickName;
  @Column
  private String roomId;
  @Column
  private String roomName;
  @Column
  private Double val;
  @Column
  private Date createDate;
  @Column
  private String admin;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
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
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  
  public String getAdmin() {
    return this.admin;
  }
  
  public void setAdmin(String admin) {
    this.admin = admin;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\ValueControlLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */