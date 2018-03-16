package org.takeback.chat.service.admin;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcRoomProperty;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.utils.RoomTemplate;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;










@Service("roomAdminService")
public class RoomAdminService
  extends MyListService
{
  @Autowired
  RoomStore roomStore;
  
  @Transactional
  public void save(Map<String, Object> req)
  {
    super.save(req);
    Map<String, Object> data = (Map)req.get("data");
    String id = (String)data.get("id");
    if ("create".equals(req.get("cmd"))) {
      List<GcRoomProperty> defaults = RoomTemplate.get(data.get("type").toString());
      if (defaults != null) {
        for (GcRoomProperty prop : defaults) {
          prop.setRoomId(id);
          this.dao.getSession().save(prop);
        }
      }
    }
    this.roomStore.reload(id);
  }
  
  @Transactional
  public void delete(Map<String, Object> req) {
    super.delete(req);
    Object pkey = req.get("id");
    this.dao.executeUpdate("delete from GcRoomProperty where roomId =:roomId", ImmutableMap.of("roomId", pkey));
    this.roomStore.delete(pkey.toString());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RoomAdminService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */