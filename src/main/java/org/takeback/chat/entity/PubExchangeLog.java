package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;

@Entity
@javax.persistence.Table(name="pub_exchangeLog")
public class PubExchangeLog
{
  @javax.persistence.Id
  @Column
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Integer id;
  @Column
  String shopId;
  @Column
  String shopName;
  @Column
  Double money;
  @Column
  Integer uid;
  @Column
  String nickName;
  @Column
  String name;
  @Column
  String address;
  @Column
  String mobile;
  @Column
  Date exchangeTime;
  @Column
  String status;
  @Column
  String admin;
  @Column
  Date dealTime;
  @Column
  String dealInfo;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getShopId() {
    return this.shopId;
  }
  
  public void setShopId(String shopId) {
    this.shopId = shopId;
  }
  
  public String getShopName() {
    return this.shopName;
  }
  
  public void setShopName(String shopName) {
    this.shopName = shopName;
  }
  
  public Double getMoney() {
    return this.money;
  }
  
  public void setMoney(Double money) {
    this.money = money;
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
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getAddress() {
    return this.address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public Date getExchangeTime() {
    return this.exchangeTime;
  }
  
  public void setExchangeTime(Date exchangeTime) {
    this.exchangeTime = exchangeTime;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getAdmin() {
    return this.admin;
  }
  
  public void setAdmin(String admin) {
    this.admin = admin;
  }
  
  public Date getDealTime() {
    return this.dealTime;
  }
  
  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }
  
  public String getDealInfo() {
    return this.dealInfo;
  }
  
  public void setDealInfo(String dealInfo) {
    this.dealInfo = dealInfo;
  }
  
  @Transient
  public String getStatusText()
  {
    return ((Dictionary)DictionaryController.instance().get("dic.chat.dealStatus")).getText(this.status);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubExchangeLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */