package org.takeback.chat.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.takeback.chat.schedule.ProxySchedule;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.UserStore;
import org.takeback.mvc.ResponseUtils;










@Controller("indexController")
public class IndexController
{
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  ProxySchedule ps;
  
  @RequestMapping(value={"/totalOnlineCount"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getTotalOnlineCout()
  {
    List<Room> rooms = this.roomStore.getByCatalog("");
    long count = this.userStore.size();
    if ((rooms != null) && (!rooms.isEmpty())) {
      for (Room room : rooms) {
        count += room.getGuests().size();
      }
    }
    return ResponseUtils.jsonView(200, "ok", Long.valueOf(count));
  }
  


  @RequestMapping({"/test"})
  public ModelAndView test(HttpServletRequest request)
  {
    this.ps.work();
    return new ModelAndView();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\IndexController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */