package org.takeback.thirdparty.support;



public class FastPayConfig
  extends AbstractThirdPartyConfig
{
  private String partnerId;
  

  private String secretCode;
  
  private String restApiAddress;
  

  public String getPartnerId()
  {
    return this.partnerId;
  }
  
  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }
  
  public String getSecretCode() {
    return this.secretCode;
  }
  
  public void setSecretCode(String secretCode) {
    this.secretCode = secretCode;
  }
  
  public String getRestApiAddress() {
    return this.restApiAddress;
  }
  
  public void setRestApiAddress(String restApiAddress) {
    this.restApiAddress = restApiAddress;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\thirdparty\support\FastPayConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */