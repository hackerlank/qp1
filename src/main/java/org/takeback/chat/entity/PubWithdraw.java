package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_withdraw")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
public class PubWithdraw
{
  private Long id;
  private Integer uid;
  private String tradeno;
  private Double fee;
  private Date tradetime;
  private Date finishtime;
  private String payno;
  private String goodsname;
  private String descpt;
  private String status;
  private String bankName;
  private String account;
  private String branch;
  private String ownerName;
  private String mobile;
  private String userIdText;
  
  @Basic
  @Column(name="userIdText", nullable=false, length=50)
  public String getUserIdText()
  {
    return this.userIdText;
  }
  
  public void setUserIdText(String userIdText) {
    this.userIdText = userIdText;
  }
  

  @javax.persistence.Id
  @GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  @Column(name="uid", nullable=false)
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
    setUserIdText(((Dictionary)DictionaryController.instance().get("dic.pubuser")).getText(uid.toString()));
  }
  
  @Basic
  @Column(name="tradeno", length=50)
  public String getTradeno() {
    return this.tradeno;
  }
  
  public void setTradeno(String tradeno) {
    this.tradeno = tradeno;
  }
  
  @Basic
  @Column(name="fee", nullable=false, precision=0)
  public Double getFee() {
    return this.fee;
  }
  
  public void setFee(Double fee) {
    this.fee = fee;
  }
  
  @Basic
  @Column(name="tradetime", nullable=false)
  public Date getTradetime() {
    return this.tradetime;
  }
  
  public void setTradetime(Date tradetime) {
    this.tradetime = tradetime;
  }
  
  @Basic
  @Column(name="finishtime", nullable=true)
  public Date getFinishtime() {
    return this.finishtime;
  }
  
  public void setFinishtime(Date finishtime) {
    this.finishtime = finishtime;
  }
  
  @Basic
  @Column(name="payno", nullable=true, length=50)
  public String getPayno() {
    return this.payno;
  }
  
  public void setPayno(String payno) {
    this.payno = payno;
  }
  
  @Basic
  @Column(name="goodsname", nullable=true, length=50)
  public String getGoodsname() {
    return this.goodsname;
  }
  
  public void setGoodsname(String goodsname) {
    this.goodsname = goodsname;
  }
  
  @Basic
  @Column(name="account", nullable=true, length=50)
  public String getAccount() {
    return this.account;
  }
  
  public void setAccount(String account) {
    this.account = account;
  }
  
  @Basic
  @Column(name="bankName", nullable=true, length=50)
  public String getBankName() {
    return this.bankName;
  }
  
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
  
  @Basic
  @Column(name="branch", nullable=true, length=50)
  public String getBranch() {
    return this.branch;
  }
  
  public void setBranch(String branch) {
    this.branch = branch;
  }
  
  @Basic
  @Column(name="mobile", nullable=true, length=50)
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  @Basic
  @Column(name="ownerName", nullable=true, length=50)
  public String getOwnerName() { return this.ownerName; }
  
  public void setOwnerName(String name)
  {
    this.ownerName = name;
  }
  
  @Basic
  @Column(name="descpt", nullable=true, length=200)
  public String getDescpt()
  {
    return this.descpt;
  }
  
  public void setDescpt(String descpt) {
    this.descpt = descpt;
  }
  
  @Basic
  @Column(name="status", nullable=false, length=1)
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  

  @Transient
  public String getStatusText()
  {
    if (!org.apache.commons.lang3.StringUtils.isEmpty(this.status)) {
      return ((Dictionary)DictionaryController.instance().get("dic.chat.withdrawStatus")).getText(this.status);
    }
    return null;
  }
  


  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    PubWithdraw that = (PubWithdraw)o;
    
    if (this.id != null ? !this.id.equals(that.id) : that.id != null) return false;
    if (this.tradeno != null ? !this.tradeno.equals(that.tradeno) : that.tradeno != null) return false;
    if (this.fee != null ? !this.fee.equals(that.fee) : that.fee != null) return false;
    if (this.tradetime != null ? !this.tradetime.equals(that.tradetime) : that.tradetime != null) return false;
    if (this.finishtime != null ? !this.finishtime.equals(that.finishtime) : that.finishtime != null) return false;
    if (this.payno != null ? !this.payno.equals(that.payno) : that.payno != null) return false;
    if (this.goodsname != null ? !this.goodsname.equals(that.goodsname) : that.goodsname != null) return false;
    if (this.descpt != null ? !this.descpt.equals(that.descpt) : that.descpt != null) return false;
    if (this.status != null ? !this.status.equals(that.status) : that.status != null) { return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.tradeno != null ? this.tradeno.hashCode() : 0);
    result = 31 * result + (this.fee != null ? this.fee.hashCode() : 0);
    result = 31 * result + (this.tradetime != null ? this.tradetime.hashCode() : 0);
    result = 31 * result + (this.finishtime != null ? this.finishtime.hashCode() : 0);
    result = 31 * result + (this.payno != null ? this.payno.hashCode() : 0);
    result = 31 * result + (this.goodsname != null ? this.goodsname.hashCode() : 0);
    result = 31 * result + (this.descpt != null ? this.descpt.hashCode() : 0);
    result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubWithdraw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */