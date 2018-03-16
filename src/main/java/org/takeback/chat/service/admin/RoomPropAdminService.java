package org.takeback.chat.service.admin;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.core.service.MyListServiceInt;







@Service("roomPropAdminService")
public class RoomPropAdminService
  extends MyListServiceInt
{
  @Autowired
  RoomStore roomStore;
  
  @Transactional
  public void save(Map<String, Object> req)
  {
    super.save(req);
    Map<String, Object> data = (Map)req.get("data");
    String roomId = (String)data.get("roomId");
    this.roomStore.reload(roomId);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RoomPropAdminService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */