package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name="ct_log")
public class LotteryLog implements Comparable<LotteryLog>
{
  private String id;
  private String luckyNumber;
  private String dateline;
  private Date catchTime;
  private String game100;
  private String game300;
  private String groupNum;
  private String special;
  private String result300;
  
  @Basic
  @Column(name="game100")
  public String getGame100()
  {
    return this.game100;
  }
  
  public void setGame100(String game100) {
    this.game100 = game100;
  }
  
  @Basic
  @Column(name="game300")
  public String getGame300() {
    return this.game300;
  }
  
  public void setGame300(String game300) {
    this.game300 = game300;
  }
  
  @Basic
  @Column(name="result300")
  public String getResult300() { return this.result300; }
  
  public void setResult300(String result300)
  {
    this.result300 = result300;
  }
  
  @Basic
  @Column(name="special")
  public String getSpecial() {
    return this.special;
  }
  
  public void setSpecial(String special) {
    this.special = special;
  }
  
  public String toString()
  {
    return this.id;
  }
  
  public int compareTo(LotteryLog o)
  {
    Long v = Long.valueOf(Long.valueOf(o.getId()).longValue() - Long.valueOf(this.id).longValue());
    return v.intValue();
  }
  
  @javax.persistence.Id
  @Column(name="id", nullable=false)
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="luckyNumber", nullable=false, length=10)
  public String getLuckyNumber() {
    return this.luckyNumber;
  }
  
  public void setLuckyNumber(String luckyNumber) {
    this.luckyNumber = luckyNumber;
  }
  
  @Basic
  @Column(name="Dateline", nullable=false, length=30)
  public String getDateline() {
    return this.dateline;
  }
  
  public void setDateline(String dateline) {
    this.dateline = dateline;
  }
  
  @Basic
  @Column(name="catchTime", nullable=false)
  public Date getCatchTime() {
    return this.catchTime;
  }
  
  public void setCatchTime(Date catchTime) {
    this.catchTime = catchTime;
  }
  
  @Basic
  @Column(name="groupNum")
  public String getGroupNum() {
    return this.groupNum;
  }
  
  public void setGroupNum(String groupNum) {
    this.groupNum = groupNum;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\LotteryLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */