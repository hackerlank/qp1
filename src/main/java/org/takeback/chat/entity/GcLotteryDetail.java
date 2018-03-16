package org.takeback.chat.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_lottery_detail")
public class GcLotteryDetail
{
  private Integer id;
  private String lotteryid;
  private Integer uid = Integer.valueOf(0);
  
  private BigDecimal coin;
  private Date createDate;
  private String roomId = "";
  
  private String gameType = "";
  private double deposit = 0.0D;
  private double addback = 0.0D;
  private double inoutNum = 0.0D;
  
  private String desc1 = "";
  private int masterId = 0;
  
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="lotteryid", nullable=false, length=50)
  public String getLotteryid() {
    return this.lotteryid;
  }
  
  public void setLotteryid(String lotteryid) {
    this.lotteryid = lotteryid;
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
  @Column(name="coin", nullable=false, precision=0)
  public BigDecimal getCoin() {
    return this.coin;
  }
  
  public void setCoin(BigDecimal coin) {
    this.coin = coin;
  }
  
  @Basic
  @Column(name="createdate", nullable=false)
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createdate) {
    this.createDate = createdate;
  }
  
  @Basic
  @Column(name="addback", nullable=false)
  public double getAddback() {
    return this.addback;
  }
  
  public void setAddback(double addback) {
    this.addback = addback;
  }
  
  @Basic
  @Column(name="deposit", nullable=false)
  public double getDeposit() { return this.deposit; }
  
  public void setDeposit(double deposit)
  {
    this.deposit = deposit;
  }
  
  @Basic
  @Column(name="gameType", nullable=false)
  public String getGameType() { return this.gameType; }
  
  public void setGameType(String gameType)
  {
    this.gameType = gameType;
  }
  
  @Basic
  @Column(name="inoutNum", nullable=false)
  public double getInoutNum() { return this.inoutNum; }
  
  public void setInoutNum(double inout)
  {
    this.inoutNum = inout;
  }
  
  @Basic
  @Column(name="masterId", nullable=false)
  public int getMasterId() { return this.masterId; }
  
  public void setMasterId(int masterId)
  {
    this.masterId = masterId;
  }
  
  @Basic
  @Column(name="roomId", nullable=false)
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  @Basic
  @Column(name="desc1", nullable=false)
  public String getDesc1() {
    return this.desc1;
  }
  
  public void setDesc1(String desc1) {
    this.desc1 = desc1;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    GcLotteryDetail that = (GcLotteryDetail)o;
    
    if (this.id != null ? !this.id.equals(that.id) : that.id != null) return false;
    if (this.lotteryid != null ? !this.lotteryid.equals(that.lotteryid) : that.lotteryid != null) return false;
    if (this.uid != null ? !this.uid.equals(that.uid) : that.uid != null) return false;
    if (this.coin != null ? !this.coin.equals(that.coin) : that.coin != null) return false;
    if (this.createDate != null ? !this.createDate.equals(that.createDate) : that.createDate != null) { return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.lotteryid != null ? this.lotteryid.hashCode() : 0);
    result = 31 * result + (this.uid != null ? this.uid.hashCode() : 0);
    result = 31 * result + (this.coin != null ? this.coin.hashCode() : 0);
    result = 31 * result + (this.createDate != null ? this.createDate.hashCode() : 0);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcLotteryDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */