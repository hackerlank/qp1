package org.takeback.dao.exception;

import org.takeback.util.exception.CodedBaseRuntimeException;




public class DaoException
  extends CodedBaseRuntimeException
{
  public DaoException() {}
  
  public DaoException(int code)
  {
    super(code);
  }
  
  public DaoException(int code, String msg) {
    super(code, msg);
  }
  
  public DaoException(String msg) {
    super(msg);
  }
  
  public DaoException(int code, Throwable t) {
    super(code, t);
  }
  
  public DaoException(String msg, Throwable t) {
    super(msg, t);
  }
  
  public DaoException(Throwable t) {
    super(t);
  }
  
  public DaoException(int code, String msg, Throwable t) {
    super(msg, t);
    this.code = code;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\dao\exception\DaoException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */