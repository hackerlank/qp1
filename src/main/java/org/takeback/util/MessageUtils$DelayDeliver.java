package org.takeback.chat.utils;

import java.util.concurrent.TimeUnit;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;





















































































































class MessageUtils$DelayDeliver
  implements Runnable
{
  private long delay = 5L;
  private Room room;
  private Message message;
  
  MessageUtils$DelayDeliver(long delay, Room room, Message message) {
    this(room, message);
  }
  
  MessageUtils$DelayDeliver(Room room, Message message)
  {
    this.room = room;
    this.message = message;
  }
  
  public void run()
  {
    try {
      TimeUnit.SECONDS.sleep(this.delay);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    MessageUtils.broadcast(this.room, this.message);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\MessageUtils$DelayDeliver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */