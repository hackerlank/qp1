package org.takeback.chat.store.room;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.service.RoomService;
import org.takeback.chat.store.user.User;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;

public class DefaultRoomStore
  implements RoomStore
{
  @Autowired
  private RoomService roomService;
  private LoadingCache<String, Room> roomPojoMap;
  private int pageSize = 5;
  
  public void init()
  {
    this.roomPojoMap = CacheBuilder.newBuilder().build(new CacheLoader()
    {
      public Room load(String s) throws Exception {
        GcRoom gcRoom = (GcRoom)DefaultRoomStore.this.roomService.get(GcRoom.class, s);
        if (gcRoom == null) {
          throw new CodedBaseException(530, "room " + s + " not exists");
        }
        Room room = (Room)BeanUtils.map(gcRoom, Room.class);
        Map<String, Object> props = DefaultRoomStore.this.roomService.getRoomProperties(gcRoom.getId());
        room.getProperties().putAll(props);
        return room;
      }
    });
    List<GcRoom> list = this.roomService.getActivedRooms();
    if ((list != null) && (!list.isEmpty())) {
      for (GcRoom gcRoom : list) {
        Room room = (Room)BeanUtils.map(gcRoom, Room.class);
        Map<String, Object> props = this.roomService.getRoomProperties(gcRoom.getId());
        room.getProperties().putAll(props);
        this.roomPojoMap.put(room.getId(), room);
      }
    }
  }
  
  public Room get(Serializable roomId)
  {
    try {
      return (Room)this.roomPojoMap.get((String)roomId);
    } catch (Exception e) {
      e.printStackTrace(); }
    return null;
  }
  

  public void reload(Serializable roomId)
  {
    Room room = get(roomId);
    GcRoom gcRoom = (GcRoom)this.roomService.get(GcRoom.class, roomId);
    if (gcRoom == null) {
      return;
    }
    if (room == null) {
      room = (Room)BeanUtils.map(gcRoom, Room.class);
      this.roomPojoMap.put(room.getId(), room);
    }
    else {
      BeanUtils.copy(gcRoom, room);
      if (StringUtils.isEmpty(gcRoom.getPsw())) {
        room.setPsw(null);
      }
      if (gcRoom.getUnDead() == null) {
        room.setUnDead(null);
      }
    }
    Map<String, Object> props = this.roomService.getRoomProperties(gcRoom.getId());
    room.getProperties().putAll(props);
  }
  
  public void fireUserLeft(User user)
  {
    Map<String, Room> rooms = this.roomPojoMap.asMap();
    if ((rooms != null) && (!rooms.isEmpty())) {
      for (Room room : rooms.values()) {
        room.left(user);
      }
    }
  }
  
  public List<Room> getByType(String type)
  {
    List<Room> ls = Lists.newArrayList();
    for (String s : this.roomPojoMap.asMap().keySet()) {
      Room room = get(s);
      if (room.getType().equals(type)) {
        ls.add(room);
      }
    }
    return ls;
  }
  
  public List<Room> getByCatalog(String catalog)
  {
    List<Room> ls = Lists.newArrayList();
    for (String s : this.roomPojoMap.asMap().keySet()) {
      Room room = get(s);
      if (StringUtils.isEmpty(catalog)) {
        ls.add(room);
      }
      else if (catalog.equals(room.getCatalog())) {
        ls.add(room);
      }
    }
    
    return ls;
  }
  
  public List<Room> getByCatalog(String catalog, int pageNo)
  {
    List<Room> rooms = getByCatalog(catalog);
    if (rooms != null) {
      int start = this.pageSize * (pageNo - 1);
      if (start > rooms.size() - 1) {
        return null;
      }
      if (start + this.pageSize > rooms.size() - 1) {
        return rooms.subList(start, rooms.size() - 1);
      }
      return rooms.subList(start, start + this.pageSize);
    }
    
    return null;
  }
  
  public List<Room> query(String condition)
  {
    return null;
  }
  
  public int getPageSize() {
    return this.pageSize;
  }
  
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
  public void delete(String roomId)
  {
    this.roomPojoMap.invalidate(roomId);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\DefaultRoomStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */