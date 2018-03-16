package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@javax.persistence.Table(name="pub_shop")
public class PubShop
{
  @Id
  @Column
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  Integer id;
  @Column
  String name;
  @Column
  String summary;
  @Column
  String detail;
  @Column
  String listImg;
  @Column
  String img1;
  @Column
  String img2;
  @Column
  String img3;
  @Column
  Double money;
  @Column
  Integer exchanged;
  @Column
  Integer sortNum;
  @Column
  Integer storage;
  @Column
  String createUser;
  @Column
  Date createDate;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getSummary() {
    return this.summary;
  }
  
  public void setSummary(String summary) {
    this.summary = summary;
  }
  
  public String getDetail() {
    return this.detail;
  }
  
  public void setDetail(String detail) {
    this.detail = detail;
  }
  
  public String getListImg() {
    return this.listImg;
  }
  
  public void setListImg(String listImg) {
    this.listImg = listImg;
  }
  
  public String getImg1() {
    return this.img1;
  }
  
  public void setImg1(String img1) {
    this.img1 = img1;
  }
  
  public String getImg2() {
    return this.img2;
  }
  
  public void setImg2(String img2) {
    this.img2 = img2;
  }
  
  public String getImg3() {
    return this.img3;
  }
  
  public void setImg3(String img3) {
    this.img3 = img3;
  }
  
  public Double getMoney() {
    return this.money;
  }
  
  public void setMoney(Double money) {
    this.money = money;
  }
  
  public Integer getStorage() {
    return this.storage;
  }
  
  public void setStorage(Integer storage) {
    this.storage = storage;
  }
  
  public String getCreateUser() {
    return this.createUser;
  }
  
  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  
  public Integer getExchanged() {
    return this.exchanged;
  }
  
  public void setExchanged(Integer exchanged) {
    this.exchanged = exchanged;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubShop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */