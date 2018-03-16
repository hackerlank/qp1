package org.takeback.chat.entity;

import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_room")
public class GcRoom
{
  private String id;
  private String name;
  private String catalog;
  private String type;
  private Integer owner;
  private Integer limitNum;
  private Integer hot;
  private String roomimg;
  private String description;
  private String detail;
  private String rule;
  private String psw;
  private Integer unDead;
  private java.util.Date createdate;
  private String status = "0";
  private Double shareRate;
  private Double sumPool;
  private Double poolAdd;
  private Double feeAdd;
  private Double sumFee;
  private Integer sumPack;
  
  @javax.persistence.Id
  @Column(name="id", nullable=false, length=50)
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @javax.persistence.Basic
  @Column(name="name", nullable=false, length=50)
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @javax.persistence.Basic
  @Column(name="catalog", nullable=false, length=10)
  public String getCatalog() {
    return this.catalog;
  }
  
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }
  
  @javax.persistence.Basic
  @Column(name="type", nullable=false, length=10)
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  @javax.persistence.Basic
  @Column(name="owner", nullable=false)
  public Integer getOwner() {
    return this.owner;
  }
  
  public void setOwner(Integer owner) {
    this.owner = owner;
  }
  
  @javax.persistence.Basic
  @Column(name="limitNum", nullable=false)
  public Integer getLimitNum() {
    return this.limitNum;
  }
  
  public void setLimitNum(Integer limitNum) {
    this.limitNum = limitNum;
  }
  
  @javax.persistence.Basic
  @Column(name="hot", nullable=true)
  public Integer getHot() {
    return this.hot;
  }
  
  public void setHot(Integer hot) {
    this.hot = hot;
  }
  
  @javax.persistence.Basic
  @Column(name="roomimg", nullable=true, length=100)
  public String getRoomimg() {
    return this.roomimg;
  }
  
  public void setRoomimg(String roomimg) {
    this.roomimg = roomimg;
  }
  
  @javax.persistence.Basic
  @Column(name="description", nullable=true, length=300)
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  @javax.persistence.Basic
  @Column(name="psw", nullable=true, length=30)
  public String getPsw() {
    return this.psw;
  }
  
  public void setPsw(String psw) {
    this.psw = psw;
  }
  
  @javax.persistence.Basic
  @Column(name="createdate", nullable=true)
  public java.util.Date getCreatedate() {
    return this.createdate;
  }
  
  public void setCreatedate(java.util.Date createdate) {
    this.createdate = createdate;
  }
  
  @javax.persistence.Basic
  @Column(name="status", nullable=true, length=10)
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  @javax.persistence.Basic
  @Column(name="feeAdd", nullable=true, length=10)
  public Double getFeeAdd() {
    return this.feeAdd;
  }
  
  public void setFeeAdd(Double feeAdd) {
    this.feeAdd = feeAdd;
  }
  
  @javax.persistence.Basic
  @Column(name="poolAdd", nullable=true, length=10)
  public Double getPoolAdd() {
    return this.poolAdd;
  }
  
  public void setPoolAdd(Double poolAdd) {
    this.poolAdd = poolAdd;
  }
  
  @javax.persistence.Basic
  @Column(name="sumFee", nullable=true, length=10)
  public Double getSumFee() {
    return this.sumFee;
  }
  
  public void setSumFee(Double sumFee) {
    this.sumFee = sumFee;
  }
  
  @javax.persistence.Basic
  @Column(name="sumPool", nullable=true, length=10)
  public Double getSumPool() {
    return this.sumPool;
  }
  
  public void setSumPool(Double sumPool) {
    this.sumPool = sumPool;
  }
  
  @javax.persistence.Basic
  @Column(name="shareRate", nullable=true)
  public Double getShareRate() {
    return this.shareRate;
  }
  
  public void setShareRate(Double shareRate) {
    this.shareRate = shareRate;
  }
  
  @javax.persistence.Basic
  @Column(name="rule", nullable=true)
  public String getRule() {
    return this.rule;
  }
  
  public void setRule(String rule) {
    this.rule = rule;
  }
  
  @javax.persistence.Basic
  @Column(name="detail", nullable=true)
  public String getDetail() {
    return this.detail;
  }
  
  public void setDetail(String detail) {
    this.detail = detail;
  }
  
  @javax.persistence.Basic
  @Column(name="sumPack", nullable=true)
  public Integer getSumPack() {
    return this.sumPack;
  }
  
  public void setSumPack(Integer sumPack) {
    this.sumPack = sumPack;
  }
  
  @javax.persistence.Basic
  @Column(name="unDead", nullable=true)
  public Integer getUnDead() {
    return this.unDead;
  }
  
  public void setUnDead(Integer unDead) {
    this.unDead = unDead;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    GcRoom gcGcRoom = (GcRoom)o;
    
    if (this.id != null ? !this.id.equals(gcGcRoom.id) : gcGcRoom.id != null) return false;
    if (this.name != null ? !this.name.equals(gcGcRoom.name) : gcGcRoom.name != null) return false;
    if (this.catalog != null ? !this.catalog.equals(gcGcRoom.catalog) : gcGcRoom.catalog != null) return false;
    if (this.type != null ? !this.type.equals(gcGcRoom.type) : gcGcRoom.type != null) return false;
    if (this.owner != null ? !this.owner.equals(gcGcRoom.owner) : gcGcRoom.owner != null) return false;
    if (this.limitNum != null ? !this.limitNum.equals(gcGcRoom.limitNum) : gcGcRoom.limitNum != null) return false;
    if (this.roomimg != null ? !this.roomimg.equals(gcGcRoom.roomimg) : gcGcRoom.roomimg != null) return false;
    if (this.description != null ? !this.description.equals(gcGcRoom.description) : gcGcRoom.description != null) return false;
    if (this.psw != null ? !this.psw.equals(gcGcRoom.psw) : gcGcRoom.psw != null) return false;
    if (this.createdate != null ? !this.createdate.equals(gcGcRoom.createdate) : gcGcRoom.createdate != null) return false;
    if (this.status != null ? !this.status.equals(gcGcRoom.status) : gcGcRoom.status != null) { return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
    result = 31 * result + (this.catalog != null ? this.catalog.hashCode() : 0);
    result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
    result = 31 * result + (this.owner != null ? this.owner.hashCode() : 0);
    result = 31 * result + (this.limitNum != null ? this.limitNum.hashCode() : 0);
    result = 31 * result + (this.roomimg != null ? this.roomimg.hashCode() : 0);
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.psw != null ? this.psw.hashCode() : 0);
    result = 31 * result + (this.createdate != null ? this.createdate.hashCode() : 0);
    result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */