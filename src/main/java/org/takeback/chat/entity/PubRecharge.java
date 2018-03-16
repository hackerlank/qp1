package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_recharge")
public class PubRecharge
{
  private Long id;
  private Integer uid;
  private String tradeno;
  private Double fee;
  private Double gift;
  private Double realfee;
  private Date tradetime;
  private Date finishtime;
  private String payno;
  private String goodsname;
  private String descpt;
  private String status;
  private Integer operator;
  private String rechargeType;
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
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public Long getId() {
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
  }
  
  @Basic
  @Column(name="tradeno", nullable=false, length=50)
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
  @Column(name="realfee", precision=0)
  public Double getRealfee() {
    return this.realfee;
  }
  
  public void setRealfee(Double realfee) {
    this.realfee = realfee;
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
  @Column(name="descpt", nullable=true, length=200)
  public String getDescpt() {
    return this.descpt;
  }
  
  public void setDescpt(String descpt) {
    this.descpt = descpt;
  }
  
  @Basic
  @Column(name="gift")
  public Double getGift() {
    return this.gift;
  }
  
  public void setGift(Double gift) {
    this.gift = gift;
  }
  
  @Basic
  @Column(name="operator")
  public Integer getOperator() {
    return this.operator;
  }
  
  public void setOperator(Integer operator) {
    this.operator = operator;
  }
  
  @Basic
  @Column(name="status", nullable=false, length=1)
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getRechargeType() {
    return this.rechargeType;
  }
  
  public void setRechargeType(String rechargeType) {
    this.rechargeType = rechargeType;
  }
  
  @javax.persistence.Transient
  public String getStatusText() {
    if (!org.apache.commons.lang3.StringUtils.isEmpty(this.status)) {
      return ((Dictionary)DictionaryController.instance().get("dic.chat.rechargeStatus")).getText(this.status);
    }
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PubRecharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */