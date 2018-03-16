package org.takeback.chat.schedule.noticer;

import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.store.room.RoomStore;

public class NoticeController
{
  @Autowired
  RoomStore roomStore;
  private String rooms;
  
  public String getRooms()
  {
    return this.rooms;
  }
  
  public void setRooms(String rooms) {
    this.rooms = rooms;
  }
  

  public void init()
  {
    String[] roomArr = this.rooms.split(",");
    AngryNotice an = new AngryNotice(roomArr, this.roomStore);
    CuteNotice cn = new CuteNotice(roomArr, this.roomStore);
    
    an.start();
    cn.start();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\noticer\NoticeController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */