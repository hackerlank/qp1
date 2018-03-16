package org.takeback.chat.store.user;

import org.takeback.chat.store.Store;

public abstract interface UserStore
  extends Store<User>
{
  public abstract long size();
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\UserStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */