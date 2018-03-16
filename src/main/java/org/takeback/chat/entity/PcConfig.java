package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="pc_config")
public class PcConfig
{
  private Integer id;
  private String param;
  private String val;
  private String alias;
  private String info;
  
  public PcConfig() {}
  
  public PcConfig(String name, String value)
  {
    this.param = name;
    this.val = value;
  }
  
  public PcConfig(String name, String value, String description) {
    this.param = name;
    this.val = value;
    this.info = description;
  }
  
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
  @Column(name="alias", nullable=false)
  public String getAlias() {
    return this.alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  @Basic
  @Column(name="info", nullable=false)
  public String getInfo()
  {
    return this.info;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
  
  @Basic
  @Column(name="param", nullable=false)
  public String getParam() {
    return this.param;
  }
  
  public void setParam(String param) {
    this.param = param;
  }
  
  @Basic
  @Column(name="val", nullable=false)
  public String getVal() {
    return this.val;
  }
  
  public void setVal(String val) {
    this.val = val;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\PcConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */