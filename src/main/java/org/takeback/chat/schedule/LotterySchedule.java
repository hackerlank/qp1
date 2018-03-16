package org.takeback.chat.schedule;

import com.google.common.cache.LoadingCache;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;


public class LotterySchedule
{
  @Autowired
  RoomStore roomStore;
  
  public void work()
  {
    List<Room> roomList = this.roomStore.getByCatalog("");
    try {
      for (Room room : roomList) {
        Map<String, Lottery> lotteries = room.getLotteries().asMap();
        Iterator<Lottery> iterator = lotteries.values().iterator();
        while (iterator.hasNext()) {
          Lottery lottery = (Lottery)iterator.next();
          if ((lottery.isOpen()) && (lottery.isExpired())) {
            try {
              lottery.expired();
            } catch (Exception e) {
              e.printStackTrace(); }
            continue;
          }
          
          if ((!lottery.isOpen()) && 
            (lottery.isExpired())) {
            String roomId = lottery.getRoomId();
            Room rm = (Room)this.roomStore.get(roomId);
            rm.getLotteries().invalidate(lottery.getId());
            lottery = null;
            room.showLotteries();
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\LotterySchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */