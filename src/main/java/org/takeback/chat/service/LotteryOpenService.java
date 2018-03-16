package org.takeback.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.service.lotteryGame.Game100;
import org.takeback.chat.service.lotteryGame.IGame;
import org.takeback.service.BaseService;




@Service
public class LotteryOpenService
  extends BaseService
{
  private String sscNum;
  private String openNum;
  private List<Integer> users;
  private Game100 game100;
  private Map<String, IGame> games = new HashMap();
  private static final Logger log = LoggerFactory.getLogger(LotteryOpenService.class);
  





  public void set(String sscNum, String openNum, List<Integer> users)
  {
    this.sscNum = sscNum;
    this.openNum = openNum;
    this.users = users;
    
    this.game100 = new Game100(openNum);
    this.games.put("100", this.game100);
  }
  
  @Transactional
  public void run() {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\LotteryOpenService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */