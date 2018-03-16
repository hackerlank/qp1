package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_Apply")
public class PubRoomApply
{
  private Integer id;
  private Integer uid;
  private String userIdText;
  private String name;
  private String mobile;
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
  @Column(name="createTime", nullable=false)
  public Date getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
  @Basic
  @Column(name="mobile", nullable=false)
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  @Basic
  @Column(name="name", nullable=false)
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @Basic
  @Column(name="uid", nullable=false)
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  @Basic
  @Column(name="userIdText", nullable=false)
  public String getUserIdText() {
    return this.userIdText;
  }
  
  public void setUserIdText(String userIdText) {
    this.userIdText = userIdText;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubRoomApply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */