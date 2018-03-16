package org.takeback.util.exception;

public class CodedBaseException extends Exception implements CodedBase {
  protected static final long serialVersionUID = -8481811634176212223L;
  protected int code = 500;
  

  public CodedBaseException() {}
  

  public CodedBaseException(int code)
  {
    this.code = code;
  }
  
  public CodedBaseException(String msg) {
    super(msg);
  }
  
  public CodedBaseException(int code, String msg) {
    super(msg);
    this.code = code;
  }
  
  public CodedBaseException(Throwable e) {
    super(e);
  }
  
  public CodedBaseException(Throwable e, int code) {
    super(e);
    this.code = code;
  }
  
  public CodedBaseException(Throwable e, String msg) {
    super(msg, e);
  }
  
  public CodedBaseException(Throwable e, int code, String msg) {
    super(msg, e);
    this.code = code;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public void throwThis() throws Exception
  {
    throw this;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exception\CodedBaseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */