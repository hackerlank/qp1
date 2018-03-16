package org.takeback.chat.service.support.ord;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.utils.MessageUtils;

















@Component("checkLotteryStatusCmd")
public class CheckLotteryStatusCmd
  implements Command
{
  @Autowired
  private LotteryService lotteryService;
  
  public void exec(Map<String, Object> data, Message message, WebSocketSession session, Room room, User user)
  {
    String lotteryId = (String)data.get("lotteryId");
    if (StringUtils.isEmpty(lotteryId)) {
      return;
    }
    Lottery lottery = room.getLottery(lotteryId);
    if (lottery == null) {
      lottery = this.lotteryService.loadLottery(lotteryId);
    }
    if (lottery == null) {
      return;
    }
    if (!lottery.isOpen()) {
      MessageUtils.sendCMD(session, "lotteryFinished", lotteryId);
      return;
    }
    String status = lottery.getStatus();
    if ((status.equals("0")) && (!lottery.isExpired()))
    {




      MessageUtils.sendCMD(session, "lotteryCanOpen", lotteryId);
    }
    else if (status.equals("1")) {
      MessageUtils.sendCMD(session, "lotteryFinished", lotteryId);
    } else if ((status.equals("2")) || (lottery.isExpired())) {
      MessageUtils.sendCMD(session, "lotteryExpired", lotteryId);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\CheckLotteryStatusCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */