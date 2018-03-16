package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="gc_pk10")
public class PK10
{
  @Id
  @Column
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Integer id;
  @Column
  String number;
  @Column
  String lucky;
  @Column
  Date openTime;
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getNumber() {
    return this.number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public String getLucky() {
    return this.lucky;
  }
  
  public void setLucky(String lucky) {
    this.lucky = lucky;
  }
  
  public Date getOpenTime() {
    return this.openTime;
  }
  
  public void setOpenTime(Date openTime) {
    this.openTime = openTime;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PK10.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */