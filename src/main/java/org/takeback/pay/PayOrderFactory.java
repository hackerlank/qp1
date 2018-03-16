package org.takeback.pay;

import cn.beecloud.BCEumeration.PAY_CHANNEL;
import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.bean.BCException;
import cn.beecloud.bean.BCOrder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.takeback.thirdparty.support.HttpHelper;
import org.takeback.thirdparty.support.WxConfig;









public class PayOrderFactory
  implements InitializingBean
{
  public static final Logger LOGGER = LoggerFactory.getLogger(PayOrderFactory.class);
  
  private static PayOrderFactory instance;
  private byte vcode = 111;
  private String appId;
  private String testSecret;
  private String appSecret;
  private String masterSecret;
  private WxConfig wxConfig;
  
  private PayOrderFactory() throws IllegalAccessException {
    if (this.vcode != 111) {
      throw new IllegalAccessException("Illegal access to this constructor.");
    }
    instance = this;
    this.vcode = ((byte)(this.vcode + 1));
  }
  
  public static PayOrderFactory getInstance() {
    return instance;
  }
  



  public BCOrder getPayOrder(String payChannel, Integer totalFee, String title, String identityId)
    throws PaymentException
  {
    try
    {
      channel = BCEumeration.PAY_CHANNEL.valueOf(payChannel);
    } catch (Exception e) { BCEumeration.PAY_CHANNEL channel;
      throw new PaymentException("Unsupported pay type: " + payChannel, e); }
    BCEumeration.PAY_CHANNEL channel;
    String billNo = PayOrderNoGenerator.generator(true);
    BCOrder bcOrder = new BCOrder(channel, totalFee, billNo, title);
    bcOrder.setBillTimeout(Integer.valueOf(360));
    
    String returnUrl = this.wxConfig.getGameServerBaseUrl() + "payReturn.jsp";
    bcOrder.setReturnUrl(returnUrl);
    if (StringUtils.isNotEmpty(identityId)) {
      bcOrder.setIdentityId(identityId);
    }
    try {
      return BCPay.startBCPay(bcOrder);
    }
    catch (BCException e) {
      throw new PaymentException("Failed to get pay order.", e);
    }
  }
  






  public BCOrder getWxJSPayOrder(Integer totalFee, String title, String code)
    throws PaymentException
  {
    BCEumeration.PAY_CHANNEL channel = BCEumeration.PAY_CHANNEL.WX_JSAPI;
    String billNo = PayOrderNoGenerator.generator(true);
    BCOrder bcOrder = new BCOrder(channel, totalFee, billNo, title);
    bcOrder.setBillTimeout(Integer.valueOf(360));
    
    String returnUrl = this.wxConfig.getGameServerBaseUrl() + "payReturn.jsp";
    bcOrder.setReturnUrl(returnUrl);
    bcOrder.setOpenId(getWxOpenId(code));
    try {
      return BCPay.startBCPay(bcOrder);
    }
    catch (BCException e) {
      throw new PaymentException("Failed to get pay order.", e);
    }
  }
  





  public String getWxAuthorizeUrl(String title, double totalFee, int userId)
  {
    try
    {
      String encodedWSJSAPIRedirectUrl = URLEncoder.encode(this.wxConfig.getGameServerBaseUrl() + "pay/apply/wx?title=" + title + "&totalFee=" + totalFee + "&userId=" + userId, "UTF-8");
      return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + this.wxConfig.getWxJSAPIAppId() + "&redirect_uri=" + encodedWSJSAPIRedirectUrl + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
  




  public String getWxOpenId(String code)
    throws PaymentException
  {
    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + this.wxConfig.getWxJSAPIAppId() + "&secret=" + this.wxConfig.getWxJSAPISecret() + "&code=" + code + "&grant_type=authorization_code";
    try
    {
      resultObject = HttpHelper.getJson(url);
    } catch (IOException e) { JSONObject resultObject;
      throw new PaymentException("Failed to get open id.", e); }
    JSONObject resultObject;
    if (resultObject.containsKey("errcode")) {
      throw new PaymentException("Failed to get open id, caused by: " + resultObject.get("errmsg"));
    }
    return resultObject.get("openid").toString();
  }
  
  public void afterPropertiesSet()
    throws Exception
  {
    BeeCloud.registerApp(this.appId, this.testSecret, this.appSecret, this.masterSecret);
  }
  
  public String getAppId() {
    return this.appId;
  }
  
  public void setAppId(String appId) {
    this.appId = appId;
  }
  
  public String getTestSecret() {
    return this.testSecret;
  }
  
  public void setTestSecret(String testSecret) {
    this.testSecret = testSecret;
  }
  
  public String getAppSecret() {
    return this.appSecret;
  }
  
  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }
  
  public String getMasterSecret() {
    return this.masterSecret;
  }
  
  public void setMasterSecret(String masterSecret) {
    this.masterSecret = masterSecret;
  }
  
  public WxConfig getWxConfig() {
    return this.wxConfig;
  }
  
  public void setWxConfig(WxConfig wxConfig) {
    this.wxConfig = wxConfig;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\pay\PayOrderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */