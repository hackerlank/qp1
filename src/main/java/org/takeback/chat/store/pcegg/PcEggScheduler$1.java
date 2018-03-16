package org.takeback.chat.store.pcegg;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.joda.time.LocalTime;
import org.takeback.chat.entity.PcEggLog;



























































class PcEggScheduler$1
  extends TimerTask
{
  PcEggScheduler$1(PcEggScheduler this$0, PcEggLog paramPcEggLog, Timer paramTimer) {}
  
  public void run()
  {
    if ((PcEggScheduler.access$000(this.this$0).work(this.val$pcEgg)) && (this.val$pcEgg.getLucky() != null)) {
      this.val$timer.cancel();
      try {
        TimeUnit.SECONDS.sleep(3L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      PcEggScheduler.access$100(this.this$0);
    }
    else {
      PeriodConfig config = PcEggScheduler.access$000(this.this$0).getPeriodConfig(new LocalTime());
      if (System.currentTimeMillis() - this.val$pcEgg.getExpireTime().getTime() > (config.getPeriodSeconds() - 30) * 1000) {
        this.val$timer.cancel();
        try
        {
          TimeUnit.SECONDS.sleep(20L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        PcEggScheduler.access$100(this.this$0);
      }
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\pcegg\PcEggScheduler$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */