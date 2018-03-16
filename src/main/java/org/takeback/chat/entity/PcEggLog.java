package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="pc_log")
public class PcEggLog
  implements Comparable<PcEggLog>
{
  private Integer id;
  private String exp;
  private String lucky;
  private String special;
  private Date dataTime;
  private Date openTime;
  private Date beginTime;
  private Date expireTime;
  
  public boolean isClosed(int closeSeconds)
  {
    return this.expireTime.getTime() - System.currentTimeMillis() < closeSeconds * 1000;
  }
  
  @Id
  @Column(name="id", nullable=false)
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="dataTime")
  public Date getDataTime() {
    return this.dataTime;
  }
  
  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }
  
  @Basic
  @Column(name="exp")
  public String getExp() { return this.exp; }
  
  public void setExp(String exp)
  {
    this.exp = exp;
  }
  
  @Basic
  @Column(name="lucky")
  public String getLucky() { return this.lucky; }
  
  public void setLucky(String lucky)
  {
    this.lucky = lucky;
  }
  
  @Basic
  @Column(name="openTime")
  public Date getOpenTime() { return this.openTime; }
  
  public void setOpenTime(Date openTime)
  {
    this.openTime = openTime;
  }
  
  @Basic
  @Column(name="special")
  public String getSpecial() { return this.special; }
  
  public void setSpecial(String special)
  {
    this.special = special;
  }
  
  public int compareTo(PcEggLog o)
  {
    Long v = Long.valueOf(Long.valueOf(o.getId().intValue()).longValue() - Long.valueOf(this.id.intValue()).longValue());
    return v.intValue();
  }
  
  public String toString()
  {
    return this.id + ":" + this.exp + "=" + this.lucky;
  }
  
  @Transient
  public Date getBeginTime() {
    return this.beginTime;
  }
  
  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
  
  @Transient
  public Date getExpireTime() { return this.expireTime; }
  
  public void setExpireTime(Date expireTime)
  {
    this.expireTime = expireTime;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PcEggLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */