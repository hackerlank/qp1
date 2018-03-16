package org.takeback.chat.store;

import java.io.Serializable;

public abstract interface Store<T extends Item>
{
  public abstract T get(Serializable paramSerializable);
  
  public abstract void reload(Serializable paramSerializable);
  
  public abstract void init();
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\Store.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */