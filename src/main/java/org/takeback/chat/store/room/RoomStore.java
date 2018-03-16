package org.takeback.chat.store.room;

import java.util.List;
import org.takeback.chat.store.Store;
import org.takeback.chat.store.user.User;

public abstract interface RoomStore
  extends Store<Room>
{
  public abstract List<Room> getByType(String paramString);
  
  public abstract List<Room> getByCatalog(String paramString);
  
  public abstract List<Room> getByCatalog(String paramString, int paramInt);
  
  public abstract List<Room> query(String paramString);
  
  public abstract void delete(String paramString);
  
  public abstract void fireUserLeft(User paramUser);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\RoomStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */