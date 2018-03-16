package org.takeback.chat.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.service.RobotService;
import org.takeback.chat.service.UserService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.RobotUser;
import org.takeback.chat.store.user.UserStore;
import org.takeback.mvc.ResponseUtils;






@Controller
public class RobotUserController
{
  @Autowired
  private UserService userService;
  @Autowired
  private UserStore userStore;
  @Autowired
  private RobotService robotService;
  @Autowired
  private RoomStore roomStore;
  private List<Thread> ts = new ArrayList();
  
  @RequestMapping({"/robots"})
  public ModelAndView robots(HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (uid == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    return ResponseUtils.jsonView(uid);
  }
  
  @RequestMapping(value={"/joinRobot"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView join(HttpServletRequest request)
  {
    try {
      List<Room> rms = this.roomStore.getByCatalog("C01");
      rms.addAll(this.roomStore.getByCatalog("C02"));
      for (Room rm : rms)
        if (!"9".equals(rm.getStatus()))
        {

          List<RobotUser> robots = this.robotService.load(Integer.valueOf(8));
          if (robots.size() == 0) {
            return ResponseUtils.jsonView(500, "error", "机器人不够用");
          }
          for (int i = 0; i < robots.size(); i++) {
            RobotUser r = (RobotUser)robots.get(i);
            r.setRoom(rm);
            this.userStore.reload(r.getId());
            rm.join(r);
            Thread t = new Thread(r);
            this.ts.add(t);
            t.start();
          }
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseUtils.jsonView(200, "OK");
  }
  
  @RequestMapping(value={"/show"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView show(HttpServletRequest request) { for (int i = 0; i < this.ts.size(); i++) {
      Thread t = (Thread)this.ts.get(i);
      System.out.println(this.ts.get(i) + " alive:" + t.isInterrupted() + " isInterrupted:" + t.isDaemon() + " isDaemon:" + t.isAlive());
    }
    return ResponseUtils.jsonView(200, "OK");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\RobotUserController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */