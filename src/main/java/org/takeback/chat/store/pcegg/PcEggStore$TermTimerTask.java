package org.takeback.chat.store.pcegg;

import java.util.Timer;
import java.util.TimerTask;
import org.takeback.chat.entity.PcEggLog;































































































































class PcEggStore$TermTimerTask
  extends TimerTask
{
  private PcEggLog egg;
  private Timer timer;
  
  PcEggStore$TermTimerTask(PcEggStore paramPcEggStore, PcEggLog egg, Timer timer)
  {
    this.egg = egg;
    this.timer = timer;
  }
  
  public void run()
  {
    this.timer.cancel();
    PcEggStore.access$000(this.this$0, this.egg);
    PcEggStore.access$100(this.this$0);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\pcegg\PcEggStore$TermTimerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */