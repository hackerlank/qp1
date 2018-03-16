package org.takeback.util.exp.exception;

import org.takeback.util.exception.CodedBaseRuntimeException;

public class ExprException extends CodedBaseRuntimeException {
  private static final long serialVersionUID = -3712765640188038285L;
  
  public ExprException(String msg) {
    super(msg);
  }
  
  public ExprException(Throwable e) {
    super(e);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\exception\ExprException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */