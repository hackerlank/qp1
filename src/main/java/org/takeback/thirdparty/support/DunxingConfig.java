package org.takeback.thirdparty.support;



public class DunxingConfig
  extends AbstractThirdPartyConfig
{
  private String appId;
  

  private String secretKey;
  
  private String restApiAddress;
  

  public String getAppId()
  {
    return this.appId;
  }
  
  public void setAppId(String appId) {
    this.appId = appId;
  }
  
  public String getSecretKey() {
    return this.secretKey;
  }
  
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
  
  public String getRestApiAddress() {
    return this.restApiAddress;
  }
  
  public void setRestApiAddress(String restApiAddress) {
    this.restApiAddress = restApiAddress;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\thirdparty\support\DunxingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */