package org.takeback.thirdparty.support;



public class WxConfig
  extends AbstractThirdPartyConfig
{
  private String wxJSAPIAppId;
  

  private String wxJSAPISecret;
  


  public String getWxJSAPIAppId()
  {
    return this.wxJSAPIAppId;
  }
  
  public void setWxJSAPIAppId(String wxJSAPIAppId) {
    this.wxJSAPIAppId = wxJSAPIAppId;
  }
  
  public String getWxJSAPISecret() {
    return this.wxJSAPISecret;
  }
  
  public void setWxJSAPISecret(String wxJSAPISecret) {
    this.wxJSAPISecret = wxJSAPISecret;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\thirdparty\support\WxConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */