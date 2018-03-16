package org.takeback.chat.service.support.ord;

import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;


@Component("openLotteryCmd")
public class OpenLotteryCmd
  implements Command
{
  @Autowired
  private UserStore userStore;
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
    try {
      BigDecimal result = lottery.open(user.getId().intValue());
      if (lottery.getRestNumber().intValue() == 0) {
        lottery.finished();
      }
      MessageUtils.sendCMD(session, "lotteryOpenSuccess", lotteryId);
    } catch (GameException e) {
      switch (e.getCode()) {
      case 500: 
        MessageUtils.sendCMD(session, "alert", e.getMessage());
        break;
      case 511: 
        MessageUtils.sendCMD(session, "lotteryExpired", lotteryId);
        break;
      case 512: 
        MessageUtils.sendCMD(session, "lotteryHasOpened", lotteryId);
        break;
      case 501: case 502: case 503: case 504: case 505: 
      case 506: case 507: case 508: case 509: 
      case 510: case 513: case 514: default: 
        MessageUtils.sendCMD(session, "lotteryOpenFailed", lotteryId);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      MessageUtils.sendCMD(session, "lotteryOpenFailed", lotteryId);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\support\ord\OpenLotteryCmd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */