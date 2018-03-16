package org.takeback.chat.store.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.store.Item;



public class User
  implements Item
{
  public static final String DEFAULT_HEADIMG = "img/avatar.png";
  public static final String SYSTEM_HEADIMG = "img/system.png";
  private Integer id;
  private String userId;
  private String nickName;
  private String mobile;
  private String headImg;
  private String signture;
  private Double money;
  private Double exp = Double.valueOf(0.0D);
  private Integer score = Integer.valueOf(0);
  
  private String email;
  
  private String qq;
  private Integer parent;
  private Double point;
  private Double subPoint;
  private String parentTree;
  private String registIp;
  private Date registDate;
  private String lastLoginIp;
  private Date lastLoginDate;
  private String userType;
  private String wxOpenId;
  private String wbOpenId;
  private String qqOpenId;
  private String alipay;
  private Double chargeAmount;
  private String status;
  private String onlineStatus;
  private String accessToken;
  private Date tokenExpireTime;
  private String roomId;
  private Map<String, Object> properties;
  private WebSocketSession webSocketSession;
  private Boolean handsUp = Boolean.FALSE;
  private String url;
  private String invitImg;
  
  public Boolean getHandsUp() {
    return this.handsUp;
  }
  
  public void setHandsUp(Boolean handsUp) {
    this.handsUp = handsUp;
  }
  
  @JsonIgnore
  public WebSocketSession getWebSocketSession() {
    return this.webSocketSession;
  }
  
  public void setWebSocketSession(WebSocketSession webSocketSession) {
    this.webSocketSession = webSocketSession;
  }
  
  public User() {
    this.properties = Maps.newConcurrentMap();
  }
  
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getNickName() {
    return StringUtils.isEmpty(this.nickName) ? this.userId : this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public String getHeadImg() {
    return StringUtils.isEmpty(this.headImg) ? "img/avatar.png" : this.headImg;
  }
  
  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }
  
  public String getSignture() {
    return this.signture;
  }
  
  public void setSignture(String signture) {
    this.signture = signture;
  }
  
  public Double getMoney() {
    BigDecimal bd = new BigDecimal(this.money.doubleValue());
    return Double.valueOf(bd.setScale(2, 4).doubleValue());
  }
  
  public void setMoney(Double money) {
    this.money = money;
  }
  
  public Double getExp() {
    return this.exp;
  }
  
  public void setExp(Double exp) {
    this.exp = exp;
  }
  
  public Integer getScore() {
    return this.score;
  }
  
  public void setScore(Integer score) {
    this.score = score;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getQq() {
    return this.qq;
  }
  
  public void setQq(String qq) {
    this.qq = qq;
  }
  
  public Integer getParent() {
    return this.parent;
  }
  
  public void setParent(Integer parent) {
    this.parent = parent;
  }
  
  public Double getPoint() {
    return this.point;
  }
  
  public void setPoint(Double point) {
    this.point = point;
  }
  
  public Double getSubPoint() {
    return this.subPoint;
  }
  
  public void setSubPoint(Double subPoint) {
    this.subPoint = subPoint;
  }
  
  public String getParentTree() {
    return this.parentTree;
  }
  
  public void setParentTree(String parentTree) {
    this.parentTree = parentTree;
  }
  
  public String getRegistIp() {
    return this.registIp;
  }
  
  public void setRegistIp(String registIp) {
    this.registIp = registIp;
  }
  
  public Date getRegistDate() {
    return this.registDate;
  }
  
  public void setRegistDate(Date registDate) {
    this.registDate = registDate;
  }
  
  public String getLastLoginIp() {
    return this.lastLoginIp;
  }
  
  public void setLastLoginIp(String lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
  }
  
  public Date getLastLoginDate() {
    return this.lastLoginDate;
  }
  
  public void setLastLoginDate(Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }
  
  public String getUserType() {
    return this.userType;
  }
  
  public void setUserType(String userType) {
    this.userType = userType;
  }
  
  public String getWxOpenId() {
    return this.wxOpenId;
  }
  
  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }
  
  public String getWbOpenId() {
    return this.wbOpenId;
  }
  
  public void setWbOpenId(String wbOpenId) {
    this.wbOpenId = wbOpenId;
  }
  
  public String getQqOpenId() {
    return this.qqOpenId;
  }
  
  public void setQqOpenId(String qqOpenId) {
    this.qqOpenId = qqOpenId;
  }
  
  public String getAlipay() {
    return this.alipay;
  }
  
  public void setAlipay(String alipay) {
    this.alipay = alipay;
  }
  
  public Double getChargeAmount() {
    return this.chargeAmount;
  }
  
  public void setChargeAmount(Double chargeAmount) {
    this.chargeAmount = chargeAmount;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getOnlineStatus() {
    return this.onlineStatus;
  }
  
  public void setOnlineStatus(String onlineStatus) {
    this.onlineStatus = onlineStatus;
  }
  
  public Map<String, Object> getProperties() {
    return this.properties;
  }
  
  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }
  
  public String getAccessToken() {
    return this.accessToken;
  }
  
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  
  public Date getTokenExpireTime() {
    return this.tokenExpireTime;
  }
  
  public void setTokenExpireTime(Date tokenExpireTime) {
    this.tokenExpireTime = tokenExpireTime;
  }
  
  public String getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public void setUrl(String url) {
    this.url = url;
    this.invitImg = ("http://pan.baidu.com/share/qrcode?w=300&h=300&url=" + this.url);
  }
  
  public String getInvitImg() {
    return this.invitImg;
  }
  
  public void setInvitImg(String invitImg) {
    this.invitImg = invitImg;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */