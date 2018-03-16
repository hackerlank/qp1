package org.takeback.pay;





public class PaymentException
  extends Exception
{
  public PaymentException() {}
  




  public PaymentException(String message)
  {
    super(message);
  }
  
  public PaymentException(String message, Throwable throwable) {
    super(message, throwable);
  }
  
  public PaymentException(Throwable throwable) {
    super(throwable);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\pay\PaymentException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */