package org.takeback.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.takeback.core.organ.OrganController;
import org.takeback.core.organ.Organization;
import org.takeback.core.role.Role;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.converter.ConversionUtils;



public class UserRoleToken
  implements Serializable
{
  private static final long serialVersionUID = 258173847713519333L;
  private Map<String, Object> properties;
  private Long id;
  private String userid;
  private String roleid;
  private String organid;
  
  public Long getId()
  {
    return this.id;
  }
  
  public String getDisplayName() {
    StringBuilder sb = new StringBuilder(String.valueOf(this.id));
    sb.append("-").append(getUsername()).append("-").append(getRolename()).append("-").append(getOrganname());
    return sb.toString();
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getUserid() {
    return this.userid;
  }
  
  public void setUserid(String userid) {
    this.userid = userid;
  }
  
  public String getRoleid() {
    return this.roleid;
  }
  
  public void setRoleid(String roleid) {
    this.roleid = roleid;
  }
  
  public String getOrganid() {
    return this.organid;
  }
  
  public void setOrganid(String organid) {
    this.organid = organid;
  }
  
  public String getUsername() {
    User user = getUser();
    if (user != null) {
      return user.getName();
    }
    return null;
  }
  
  public Timestamp getLastLoginTime() {
    User user = getUser();
    if (user != null) {
      return user.getLastsignintime();
    }
    return null;
  }
  
  public String getLastLoginIP() {
    User user = getUser();
    if (user != null) {
      return user.getLastsigninip();
    }
    return null;
  }
  
  public String getRolename() {
    Role role = getRole();
    if (role != null) {
      return role.getName();
    }
    return null;
  }
  
  public String getOrganname() {
    Organization organ = getOrgan();
    if (organ != null) {
      return organ.getName();
    }
    return null;
  }
  
  @JsonIgnore
  public User getUser() {
    if (StringUtils.isEmpty(this.userid)) {
      return null;
    }
    return AccountCenter.getUser(this.userid);
  }
  
  @JsonIgnore
  public Role getRole() {
    if (StringUtils.isEmpty(this.roleid)) {
      return null;
    }
    return AccountCenter.getRole(this.roleid);
  }
  
  @JsonIgnore
  public Organization getOrgan()
  {
    if (StringUtils.isEmpty(this.organid)) {
      return null;
    }
    return OrganController.getRoot().getChild(this.organid);
  }
  
  public void setProperty(String nm, Object v)
  {
    if (this.properties == null) {
      this.properties = Maps.newHashMap();
    }
    this.properties.put(nm, v);
  }
  
  public Object getProperty(String nm)
  {
    return getProperty(nm, false);
  }
  
  public Object getProperty(String nm, boolean inherit) {
    Object val = null;
    if (this.properties != null) {
      val = this.properties.get(nm);
      if (!inherit) {
        return val;
      }
    }
    if (val == null) {
      val = AccountCenter.getUser(this.userid).getProperty(nm);
    }
    if (val == null) {
      val = AccountCenter.getRole(this.roleid).getProperty(nm);
    }
    if (val == null) {
      val = ((Organization)OrganController.instance().get(this.organid)).getProperty(nm);
    }
    return val;
  }
  
  public <T> T getProperty(String nm, Class<T> targetType) {
    return (T)ConversionUtils.convert(getProperty(nm), targetType);
  }
  
  public <T> T getProperty(String nm, Class<T> targetType, boolean inherit) {
    return (T)ConversionUtils.convert(getProperty(nm, inherit), targetType);
  }
  
  public Map<String, Object> getProperties() {
    return this.properties;
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
  
  public boolean equals(Object o) {
    if ((o == null) || (o.getClass().equals(getClass()))) {
      return false;
    }
    if (o.hashCode() == hashCode()) {
      return true;
    }
    return false;
  }
  
  @JsonIgnore
  public static UserRoleToken getCurrent() {
    UserRoleToken urt = (UserRoleToken)ContextUtils.get("$urt", UserRoleToken.class);
    if (urt == null) {
      throw new IllegalStateException("[UserRoleToken] not exist in thread context");
    }
    return urt;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\UserRoleToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */