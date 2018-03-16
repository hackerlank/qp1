package org.takeback.chat.service.admin;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.core.service.MyListServiceInt;

@Service("noticeService")
public class NoticeService extends MyListServiceInt
{
  @Autowired
  private RoomStore roomStore;
  
  @Transactional
  public void save(Map<String, Object> req)
  {
    super.save(req);
    Map<String, Object> data = (Map)req.get("data");
    System.out.println(data);
    String content = (String)data.get("content");
    String meg = "<span style='color:#B22222'>系统消息：" + content + " </span>";
    
    List<Room> rms = this.roomStore.getByCatalog(null);
    for (Room r : rms) {
      Message msg = new Message("TXT_SYS", Integer.valueOf(0), meg);
      MessageUtils.broadcast(r, msg);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\NoticeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */