package org.takeback.chat.lottery.listeners;

import org.takeback.util.exception.CodedBaseException;







public class GameException
  extends CodedBaseException
{
  public GameException(String code)
  {
    super(code);
  }
  
  public GameException(int code, String msg) {
    super(code, msg);
  }
  
  public GameException(int code, Throwable e) {
    super(e, code);
  }
  
  public GameException(int code, String msg, Throwable e) {
    super(e, code, msg);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\GameException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */