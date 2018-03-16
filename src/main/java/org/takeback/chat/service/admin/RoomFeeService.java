package org.takeback.chat.service.admin;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;



@Service("roomFeeService")
public class RoomFeeService
  extends MyListServiceInt
{
  @Autowired
  RoomStore roomStore;
  
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String roomId = (String)data.get("roomId");
    if (roomId == null) {
      throw new CodedBaseRuntimeException("房间号不能为空");
    }
    
    GcRoom rm = (GcRoom)this.dao.get(GcRoom.class, roomId);
    if (rm == null) {
      throw new CodedBaseRuntimeException("错误的房间号");
    }
    Double fee = Double.valueOf(0.0D);
    try {
      fee = Double.valueOf((String)data.get("val"));
    } catch (Exception e) {
      throw new CodedBaseRuntimeException("错误的数值");
    }
    String hql = "update GcRoom set sumfee = COALESCE(sumfee,0) - :val where sumfee>:val and  id=:roomId";
    int effected = this.dao.executeUpdate(hql, ImmutableMap.of("val", fee, "roomId", roomId));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("超出可用额度：" + rm.getSumFee());
    }
    
    HttpServletRequest request = (HttpServletRequest)ContextUtils.get("$httpRequest");
    String admin = (String)WebUtils.getSessionAttribute(request, "$uid");
    data.put("admin", admin);
    data.put("createDate", new Date());
    data.put("roomName", rm.getName());
    super.save(req);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RoomFeeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */