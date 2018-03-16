package org.takeback.chat.store.lottery;

import java.io.Serializable;
import org.takeback.chat.lottery.Lottery;

public class DefaultLotteryStore
  implements LotteryStore
{
  public Lottery get(Serializable id)
  {
    return null;
  }
  
  public void reload(Serializable id) {}
  
  public void init() {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\lottery\DefaultLotteryStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */