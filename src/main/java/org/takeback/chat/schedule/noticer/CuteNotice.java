package org.takeback.chat.schedule.noticer;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.utils.LotteryUtilBean;
import org.takeback.chat.utils.MessageUtils;



public class CuteNotice
  extends Thread
{
  @Autowired
  RoomStore roomStore;
  private String[] rooms;
  
  public CuteNotice(String[] rooms, RoomStore roomStore)
  {
    this.rooms = rooms;
    this.roomStore = roomStore;
  }
  
  public void run()
  {
    try {
      for (;;) {
        LotteryUtilBean lub = new LotteryUtilBean();
        Long timeLeft = Long.valueOf(lub.getNextOpenRestTime());
        TimeUnit.SECONDS.sleep(timeLeft.longValue() + 1L);
        lub = new LotteryUtilBean();
        String txt = "<span style='color:#B22222'>" + lub.getCurrentStage() + "期停止下注," + lub.getNextStage() + "开始下注！</span>";
        Message msg = new Message("TXT_SYS", Integer.valueOf(0), txt);
        for (String roomId : this.rooms) {
          Room r = (Room)this.roomStore.get(roomId);
          if (r != null)
          {

            MessageUtils.broadcast(r, msg); }
        }
      }
    } catch (Exception e) { e.printStackTrace();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\noticer\CuteNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */