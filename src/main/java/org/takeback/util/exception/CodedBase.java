package org.takeback.util.exception;

public abstract interface CodedBase
{
  public abstract int getCode();
  
  public abstract String getMessage();
  
  public abstract Throwable getCause();
  
  public abstract StackTraceElement[] getStackTrace();
  
  public abstract void throwThis()
    throws Exception;
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exception\CodedBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */