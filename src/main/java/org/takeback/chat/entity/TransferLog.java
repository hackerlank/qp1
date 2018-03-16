package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.takeback.chat.utils.NumberUtil;







@Entity
@Table(name="pub_transfer")
public class TransferLog
{
  @Id
  @Column
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Integer id;
  @Column
  Integer fromUid;
  @Column
  String fromNickName;
  @Column
  Integer toUid;
  @Column
  String toNickName;
  @Column
  Double money;
  @Column
  Date transferDate;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public Integer getFromUid() {
    return this.fromUid;
  }
  
  public void setFromUid(Integer fromUid) {
    this.fromUid = fromUid;
  }
  
  public String getFromNickName() {
    return this.fromNickName;
  }
  
  public void setFromNickName(String fromNickName) {
    this.fromNickName = fromNickName;
  }
  
  public Integer getToUid() {
    return this.toUid;
  }
  
  public void setToUid(Integer toUid) {
    this.toUid = toUid;
  }
  
  public String getToNickName() {
    return this.toNickName;
  }
  
  public void setToNickName(String toNickName) {
    this.toNickName = toNickName;
  }
  
  public Double getMoney() {
    return Double.valueOf(NumberUtil.round(this.money.doubleValue()));
  }
  
  public void setMoney(Double money) {
    this.money = money;
  }
  
  public Date getTransferDate() {
    return this.transferDate;
  }
  
  public void setTransferDate(Date transferDate) {
    this.transferDate = transferDate;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\TransferLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */