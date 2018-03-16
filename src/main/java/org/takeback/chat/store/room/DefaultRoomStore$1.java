package org.takeback.chat.store.room;

import com.google.common.cache.CacheLoader;
import java.util.Map;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.service.RoomService;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;















class DefaultRoomStore$1
  extends CacheLoader<String, Room>
{
  DefaultRoomStore$1(DefaultRoomStore this$0) {}
  
  public Room load(String s)
    throws Exception
  {
    GcRoom gcRoom = (GcRoom)DefaultRoomStore.access$000(this.this$0).get(GcRoom.class, s);
    if (gcRoom == null) {
      throw new CodedBaseException(530, "room " + s + " not exists");
    }
    Room room = (Room)BeanUtils.map(gcRoom, Room.class);
    Map<String, Object> props = DefaultRoomStore.access$000(this.this$0).getRoomProperties(gcRoom.getId());
    room.getProperties().putAll(props);
    return room;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\DefaultRoomStore$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */