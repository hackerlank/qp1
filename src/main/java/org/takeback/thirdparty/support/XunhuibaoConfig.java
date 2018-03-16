package org.takeback.thirdparty.support;


public class XunhuibaoConfig
  extends AbstractThirdPartyConfig
{
  private String merchantId;
  private String secretKey;
  private String restApiAddress;
  
  public String getMerchantId()
  {
    return this.merchantId;
  }
  
  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
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


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\thirdparty\support\XunhuibaoConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */