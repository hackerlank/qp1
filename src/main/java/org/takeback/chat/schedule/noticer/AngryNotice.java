package org.takeback.chat.schedule.noticer;

import java.util.concurrent.TimeUnit;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.utils.LotteryUtilBean;
import org.takeback.chat.utils.MessageUtils;







public class AngryNotice
  extends Thread
{
  RoomStore roomStore;
  public static final Integer TO_SLEEP = Integer.valueOf(10);
  private String[] rooms;
  
  public AngryNotice(String[] rooms, RoomStore roomStore)
  {
    this.rooms = rooms;
    this.roomStore = roomStore;
  }
  
  public void run()
  {
    try {
      for (;;) {
        TimeUnit.SECONDS.sleep(TO_SLEEP.intValue());
        LotteryUtilBean lub = new LotteryUtilBean();
        Long timeLeft = Long.valueOf(lub.getNextOpenRestTime());
        String txt = "<span style='color:#B22222'>" + lub.getNextStage() + "期下注剩余时间：" + timeLeft.longValue() / 60L + "分" + timeLeft.longValue() % 60L + "秒</span>";
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


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\noticer\AngryNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */