package org.takeback.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.takeback.core.controller.support.AbstractConfigurable;

public class User extends AbstractConfigurable
{
  private static final long serialVersionUID = 3175037043404273987L;
  public static final String DEFAULT_AVATAR = "avatar/default.jpg";
  private Map<Long, UserRoleToken> roles = Maps.newConcurrentMap();
  private String password;
  private String name;
  private String phonenumb;
  private String email;
  private String avatar;
  private Timestamp registertime;
  private Timestamp lastsignintime;
  private String lastsigninip;
  private String status;
  
  public String getPhonenumb()
  {
    return this.phonenumb;
  }
  
  public void setPhonenumb(String phonenumb) {
    this.phonenumb = phonenumb;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public Timestamp getLastsignintime() {
    return this.lastsignintime;
  }
  
  public void setLastsignintime(Timestamp lastsignintime) {
    this.lastsignintime = lastsignintime;
  }
  
  public String getLastsigninip() {
    return this.lastsigninip;
  }
  
  public void setLastsigninip(String lastsigninip) {
    this.lastsigninip = lastsigninip;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public boolean validatePassword(String pwd) {
    if ((StringUtils.isEmpty(pwd)) && (StringUtils.isEmpty(this.password))) {
      return true;
    }
    return this.password.equals(pwd);
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public Date getRegistertime() {
    return this.registertime;
  }
  
  public void setRegistertime(Timestamp registertime) {
    this.registertime = registertime;
  }
  
  public String getAvatar() {
    if (StringUtils.isEmpty(this.avatar)) {
      return "avatar/default.jpg";
    }
    return this.avatar;
  }
  
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
  
  public void addUserRoleToken(UserRoleToken ur) {
    this.roles.put(ur.getId(), ur);
  }
  
  public void removeUserRoleToken(int id) {
    this.roles.remove(Integer.valueOf(id));
  }
  
  public boolean hasUserRoleToken(UserRoleToken ur) {
    return this.roles.containsValue(ur);
  }
  
  public boolean hasUserRoleToken(Integer urId) {
    return this.roles.containsKey(urId);
  }
  
  public UserRoleToken getUserRoleToken(long urId) {
    return (UserRoleToken)this.roles.get(Long.valueOf(urId));
  }
  
  @JsonIgnore
  public Collection<UserRoleToken> getUserRoleTokens() {
    return this.roles.values();
  }
  
  @JsonIgnore
  public boolean isForbidden() {
    if ("1".equals(this.status)) {
      return false;
    }
    return true;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\user\User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */