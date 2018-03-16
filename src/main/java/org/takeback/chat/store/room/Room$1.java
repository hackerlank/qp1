package org.takeback.chat.store.room;

import com.google.common.cache.CacheLoader;
import java.util.List;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.lottery.DefaultLottery;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.LotteryService;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;















































































class Room$1
  extends CacheLoader<String, Lottery>
{
  Room$1(Room this$0) {}
  
  public Lottery load(String s)
    throws Exception
  {
    LotteryService lotteryService = (LotteryService)ApplicationContextHolder.getBean("lotteryService");
    GcLottery gcLottery = (GcLottery)lotteryService.get(GcLottery.class, s);
    if (gcLottery == null) {
      throw new CodedBaseException(530, "lottery " + s + " not exists");
    }
    Lottery lottery = new DefaultLottery(gcLottery.getMoney(), gcLottery.getNumber());
    if (Room.access$000(this.this$0) != null) {
      lottery.setRoomAndLotteryListener(Room.access$000(this.this$0));
    }
    BeanUtils.copy(gcLottery, lottery);
    List<GcLotteryDetail> ls = lotteryService.findByProperty(GcLotteryDetail.class, "lotteryid", s);
    for (GcLotteryDetail gcLotteryDetail : ls) {
      LotteryDetail lotteryDetail = new LotteryDetail(gcLotteryDetail.getUid(), gcLotteryDetail.getCoin());
      BeanUtils.copy(gcLotteryDetail, lotteryDetail);
      lottery.addDetail(lotteryDetail);
    }
    return lottery;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\room\Room$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */