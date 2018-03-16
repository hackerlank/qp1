package org.takeback.chat.service.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.entity.ValueControlLog;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.ValueControl;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;









@Service("controlService")
public class ControlService
  extends MyListServiceInt
{
  @Autowired
  RoomStore roomStore;
  @Autowired
  UserStore userStore;
  
  @Transactional(rollbackFor={Throwable.class})
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String roomId = (String)data.get("roomId");
    GcRoom r = (GcRoom)this.dao.get(GcRoom.class, roomId);
    if (r == null) {
      throw new CodedBaseRuntimeException("错误的房间号!");
    }
    Integer uid = Integer.valueOf(data.get("uid").toString());
    PubUser u = (PubUser)this.dao.get(PubUser.class, uid);
    if (u == null) {
      throw new CodedBaseRuntimeException("错误的用户ID!");
    }
    
    Double value = Double.valueOf(data.get("value").toString());
    ValueControl.setValue(roomId, uid, new BigDecimal(value.doubleValue()));
    
    ValueControlLog vcl = new ValueControlLog();
    vcl.setRoomId(roomId);
    vcl.setRoomName(r.getName());
    vcl.setUid(uid);
    vcl.setNickName(u.getNickName());
    vcl.setCreateDate(new Date());
    vcl.setVal(value);
    HttpServletRequest request = (HttpServletRequest)ContextUtils.get("$httpRequest");
    vcl.setAdmin((String)WebUtils.getSessionAttribute(request, "$uid"));
    this.dao.save(ValueControlLog.class, vcl);
  }
  
  public Map<String, Object> list(Map<String, Object> req)
  {
    int limit = ((Integer)req.get(LIMIT)).intValue();
    int page = ((Integer)req.get(PAGE)).intValue();
    List<?> ls = ValueControl.query();
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Integer.valueOf(ls.size()));
    result.put("body", ls);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\ControlService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */