package org.takeback.chat.service.support.ord;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;

@Component("showRoomMembersCmd")
public class ShowRoomMembersCmd
  implements Command
{
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    List<Map<String, Object>> members = Lists.newArrayList();
    Collection<User> users = room.getUsers().values();
    for (User u : users) {
      Map<String, Object> d = Maps.newHashMap();
      d.put("id", u.getId());
      d.put("nickName", u.getNickName());
      d.put("headImg", u.getHeadImg());
      d.put("handsUp", u.getHandsUp());
      members.add(d);
    }
    MessageUtils.sendCMD(session, "showRoomMembers", members);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\ShowRoomMembersCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */