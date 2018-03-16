package org.takeback.chat.service.support.ord;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PcEggLog;
import org.takeback.chat.service.PcEggService;
import org.takeback.chat.store.pcegg.PcEggStore;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;








@Component("latestTermCmd")
public class LatestTermCmd
  implements Command
{
  @Autowired
  private PcEggService eggService;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    PcEggLog egg = PcEggStore.getStore().getLastest();
    List<PcEggLog> logs = PcEggStore.getStore().getCache();
    if ((egg == null) && (logs.size() > 0)) {
      egg = (PcEggLog)logs.get(0);
    }
    if (egg != null) {
      String simpleWord = "";
      for (PcEggLog pel : logs) {
        simpleWord = simpleWord + " " + (StringUtils.isNotEmpty(pel.getLucky()) ? pel.getLucky() : "?");
      }
      simpleWord = simpleWord.length() > 1 ? simpleWord.substring(1) : simpleWord;
      long l = egg.getExpireTime().getTime() - System.currentTimeMillis();
      MessageUtils.sendCMD(session, "latestTerm", ImmutableMap.of("termId", egg
        .getId(), "expireTime", egg
        .getExpireTime(), "remainSeconds", 
        Integer.valueOf((int)Math.floor(l / 1000L)), "simpleWord", simpleWord, "logs", logs));
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\LatestTermCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */