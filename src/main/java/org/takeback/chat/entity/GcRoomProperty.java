package org.takeback.chat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
@javax.persistence.Table(name="gc_room_props")
public class GcRoomProperty
{
  private Integer id;
  private String roomId;
  private String configKey;
  private String alias;
  private String configValue;
  private String info;
  
  public GcRoomProperty() {}
  
  public GcRoomProperty(String key, String alias, String value, String info)
  {
    this.configKey = key;
    this.alias = alias;
    this.configValue = value;
    this.info = info;
  }
  
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
  @Column(name="id", nullable=false, length=50)
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  @Basic
  @Column(name="alias", nullable=false, precision=0)
  public String getAlias() {
    return this.alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  @Basic
  @Column(name="info", nullable=false, precision=0)
  public String getInfo() {
    return this.info;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
  
  @Basic
  @Column(name="configKey", nullable=false, precision=0)
  public String getConfigKey() {
    return this.configKey;
  }
  
  public void setConfigKey(String key) {
    this.configKey = key;
  }
  
  @Basic
  @Column(name="roomId", nullable=false, precision=0)
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  @Basic
  @Column(name="configValue", nullable=false, precision=0)
  public String getConfigValue() {
    return this.configValue;
  }
  
  public void setConfigValue(String value) {
    this.configValue = value;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\GcRoomProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */