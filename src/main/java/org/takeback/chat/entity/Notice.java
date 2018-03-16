package org.takeback.chat.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="pub_notice")
public class Notice
{
  private String id;
  private String content;
  private Date createDate;
  
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false)
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="content", nullable=false, precision=0)
  public String getContent() {
    return this.content;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  @Basic
  @Column(name="createDate", nullable=false, precision=0)
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\Notice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */