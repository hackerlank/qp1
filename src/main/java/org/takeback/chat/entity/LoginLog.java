package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@javax.persistence.Table(name="pub_loginLog")
public class LoginLog
{
  private String id;
  private Integer userId;
  private String userName;
  private String country;
  private String province;
  private String city;
  private String area;
  private String ip;
  private String realIp;
  private Date loginTime;
  
  @javax.persistence.Id
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="ip")
  public String getIp() {
    return this.ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip;
  }
  
  @Basic
  @Column(name="loginTime")
  public Date getLoginTime() { return this.loginTime; }
  
  public void setLoginTime(Date loginTime)
  {
    this.loginTime = loginTime;
  }
  
  @Basic
  @Column(name="realIp")
  public String getRealIp() { return this.realIp; }
  
  public void setRealIp(String realIp)
  {
    this.realIp = realIp;
  }
  
  @Basic
  @Column(name="userId")
  public Integer getUserId() { return this.userId; }
  
  public void setUserId(Integer userId)
  {
    this.userId = userId;
  }
  
  @Basic
  @Column(name="area")
  public String getArea() {
    return this.area;
  }
  
  public void setArea(String area) {
    this.area = area;
  }
  
  @Basic
  @Column(name="city")
  public String getCity() {
    return this.city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  @Basic
  @Column(name="province")
  public String getProvince() {
    return this.province;
  }
  
  public void setProvince(String province) {
    this.province = province;
  }
  
  @Basic
  @Column(name="userName")
  public String getUserName() {
    return this.userName;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  @Basic
  @Column(name="country")
  public String getCountry() {
    return this.country;
  }
  
  public void setCountry(String country) {
    this.country = country;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\LoginLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */