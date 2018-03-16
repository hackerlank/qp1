package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_bank")
public class PubBank
{
  private Integer id;
  private Integer userId;
  private String userIdText;
  private String bankName;
  private String branch;
  private String name;
  private String account;
  private String mobile;
  private Date createTime;
  
  @javax.persistence.Id
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="userId", nullable=false)
  public Integer getUserId() {
    return this.userId;
  }
  
  public void setUserId(Integer userId) {
    setUserIdText(((Dictionary)DictionaryController.instance().get("dic.pubuser")).getText(userId.toString()));
    this.userId = userId;
  }
  
  @Basic
  @Column(name="bankName", nullable=true, length=100)
  public String getBankName() {
    return this.bankName;
  }
  
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
  
  @Basic
  @Column(name="branch", nullable=true, length=100)
  public String getBranch() {
    return this.branch;
  }
  
  public void setBranch(String branch) {
    this.branch = branch;
  }
  
  @Basic
  @Column(name="name", nullable=true, length=30)
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @Basic
  @Column(name="account", nullable=true, length=30)
  public String getAccount() {
    return this.account;
  }
  
  public void setAccount(String account) {
    this.account = account;
  }
  
  @Basic
  @Column(name="mobile", nullable=true, length=20)
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  @Basic
  @Column(name="createTime", nullable=true)
  public Date getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
  @Basic
  @Column(name="userIdText", nullable=true)
  public String getUserIdText() {
    return this.userIdText;
  }
  
  public void setUserIdText(String userIdText) {
    this.userIdText = userIdText;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    PubBank pubBank = (PubBank)o;
    
    if (this.id != null ? !this.id.equals(pubBank.id) : pubBank.id != null) return false;
    if (this.userId != null ? !this.userId.equals(pubBank.userId) : pubBank.userId != null) return false;
    if (this.bankName != null ? !this.bankName.equals(pubBank.bankName) : pubBank.bankName != null) return false;
    if (this.branch != null ? !this.branch.equals(pubBank.branch) : pubBank.branch != null) return false;
    if (this.name != null ? !this.name.equals(pubBank.name) : pubBank.name != null) return false;
    if (this.account != null ? !this.account.equals(pubBank.account) : pubBank.account != null) return false;
    if (this.mobile != null ? !this.mobile.equals(pubBank.mobile) : pubBank.mobile != null) return false;
    if (this.createTime != null ? !this.createTime.equals(pubBank.createTime) : pubBank.createTime != null) { return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.userId != null ? this.userId.hashCode() : 0);
    result = 31 * result + (this.bankName != null ? this.bankName.hashCode() : 0);
    result = 31 * result + (this.branch != null ? this.branch.hashCode() : 0);
    result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
    result = 31 * result + (this.account != null ? this.account.hashCode() : 0);
    result = 31 * result + (this.mobile != null ? this.mobile.hashCode() : 0);
    result = 31 * result + (this.createTime != null ? this.createTime.hashCode() : 0);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubBank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */