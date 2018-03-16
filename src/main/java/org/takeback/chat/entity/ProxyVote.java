package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@javax.persistence.Table(name="pub_proxyVote")
public class ProxyVote
{
  @Id
  @Column
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  Integer id;
  @Column
  Integer uid;
  @Column
  Double total;
  @Column
  Double vote;
  @Column
  String userId;
  @Column
  Date cacuDate;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public Double getVote()
  {
    return this.vote;
  }
  
  public void setVote(Double vote) {
    this.vote = vote;
  }
  
  public Date getCacuDate() {
    return this.cacuDate;
  }
  
  public void setCacuDate(Date cacuDate) {
    this.cacuDate = cacuDate;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public Double getTotal() {
    return this.total;
  }
  
  public void setTotal(Double total) {
    this.total = total;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\ProxyVote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */