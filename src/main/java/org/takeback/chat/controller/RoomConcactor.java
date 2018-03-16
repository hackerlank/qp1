package org.takeback.chat.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.GcRoomKickLog;
import org.takeback.chat.entity.GcRoomMember;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.RoomService;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.DateUtil;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.annotation.AuthPassport;


@Controller
@RequestMapping({"/room"})
public class RoomConcactor
{
  @Autowired
  private RoomService roomService;
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  private final Lock lock = new ReentrantLock();
  



  @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getRoom(@PathVariable String id)
  {
    Room room = (Room)this.roomStore.get(id);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + id + "不存在.");
    }
    return ResponseUtils.jsonView(200, "success", room);
  }
  




  @RequestMapping(value={"/authorize"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView validatePassword(@RequestBody Map<String, String> params)
  {
    String roomId = (String)params.get("roomId");
    String password = (String)params.get("password");
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if ((!room.isNeedPsw().booleanValue()) || (password.equals(room.getPsw()))) {
      return ResponseUtils.jsonView(200, "success");
    }
    return ResponseUtils.jsonView(401, "incorrect password");
  }
  




  @RequestMapping({"/join/{id}"})
  public ModelAndView joinIn(@PathVariable String id, HttpServletRequest request)
  {
    Room room = (Room)this.roomStore.get(id);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + id + "不存在.");
    }
    String lastRoomId = (String)WebUtils.getSessionAttribute(request, "roomId");
    if (lastRoomId != null) {
      WebUtils.setSessionAttribute(request, "lastRoomId", lastRoomId);
    }
    WebUtils.setSessionAttribute(request, "roomId", id);
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (uid != null) {
      try {
        this.lock.lock();
        if (room.getPosition().intValue() >= room.getLimitNum().intValue()) {
          return ResponseUtils.jsonView(530, "房间已满.");
        }
      } finally {
        this.lock.unlock();
      }
    }
    Object kickLogs = this.roomService.findByHql("from GcRoomKickLog where kickTime>=:t and  roomId=:roomId and uid =:uid ", 
      ImmutableMap.of("t", DateUtil.getDateBeforeSeconds(RoomService.KICK_TIME), "roomId", id, "uid", uid), 1, 1);
    if (((List)kickLogs).size() > 0) {
      return ResponseUtils.jsonView(500, "禁止进入房间.");
    }
    
    Object body = Maps.newHashMap();
    ((Map)body).put("room", room);
    ((Map)body).put("uid", uid);
    return ResponseUtils.jsonView(200, "success", body);
  }
  
  @RequestMapping({"/list/{pageNo}"})
  public ModelAndView rooms(@PathVariable Integer pageNo, HttpServletRequest request) {
    String s = "";
    Map<String, Object> params = Maps.newHashMap();
    String cata = request.getParameter("cata");
    String type = request.getParameter("type");
    if (!StringUtils.isEmpty(cata)) {
      s = " and a.catalog = :p";
      params.put("p", cata);
    }
    if (!StringUtils.isEmpty(type)) {
      s = " and a.type = :p";
      params.put("p", type);
    }
    List<GcRoom> rooms = this.roomService.findByHql("from GcRoom a where a.status !='9'" + s + " order by a.hot desc, a.createdate desc", params, 100, pageNo.intValue());
    if ((rooms == null) || (rooms.size() == 0)) {
      return ResponseUtils.jsonView(new ArrayList());
    }
    List<Room> result = Lists.newArrayList();
    for (GcRoom room : rooms) {
      result.add(this.roomStore.get(room.getId()));
    }
    return ResponseUtils.jsonView(result);
  }
  
  @AuthPassport
  @RequestMapping({"/members/{id}"})
  public ModelAndView members(@PathVariable String id)
  {
    Room room = (Room)this.roomStore.get(id);
    if (room == null) {
      return ResponseUtils.jsonView(Lists.newArrayList());
    }
    if ("G022".equals(room.getType())) {
      List<GcRoomMember> ls = this.roomService.findByProperty(GcRoomMember.class, "roomId", id);
      List<User> members = new ArrayList();
      for (GcRoomMember m : ls) {
        members.add(this.userStore.get(m.getUid()));
      }
      return ResponseUtils.jsonView(members);
    }
    return ResponseUtils.jsonView(room.getUsers().values());
  }
  





  @AuthPassport
  @RequestMapping(value={"/props"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getRoomProps(@RequestParam("roomId") String roomId, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(404, "无权执行该操作!");
    }
    
    Map<String, Object> res = new HashMap();
    res.put("room", this.roomService.get(GcRoom.class, roomId));
    res.put("props", this.roomService.getRoomProps(roomId));
    res.put("qunInfo", this.roomService.getQunInfo(roomId));
    return ResponseUtils.jsonView(res);
  }
  
  @AuthPassport
  @RequestMapping(value={"/myMembers"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getMyMembers(@RequestParam String roomId, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(404, "无权执行该操作!");
    }
    List<GcRoomMember> ls = this.roomService.findByProperties(GcRoomMember.class, ImmutableMap.of("roomId", roomId));
    






    return ResponseUtils.jsonView(ls);
  }
  
  @AuthPassport
  @RequestMapping(value={"/myMemberDetail"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView myMemberDetail(@RequestParam Integer id, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    GcRoomMember m = (GcRoomMember)this.roomService.get(GcRoomMember.class, id);
    String roomId = m.getRoomId();
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(404, "无权执行该操作!");
    }
    return ResponseUtils.jsonView(m);
  }
  
  @AuthPassport
  @RequestMapping(value={"/kick"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView kick(@RequestParam Integer id, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    GcRoomMember m = (GcRoomMember)this.roomService.get(GcRoomMember.class, id);
    String roomId = m.getRoomId();
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(500, "无权执行该操作!");
    }
    if (m != null) {
      this.roomService.delete(GcRoomMember.class, m);
      GcRoomKickLog gl = new GcRoomKickLog();
      gl.setRoomId(roomId);
      gl.setUid(m.getUid());
      gl.setKickTime(new Date());
      this.roomService.save(GcRoomKickLog.class, gl);
      
      Map<Integer, User> users = room.getUsers();
      if (users.containsKey(m.getUid())) {
        User user = (User)this.userStore.get(m.getUid());
        room.left(user);
        Message msg = new Message("ORD", Integer.valueOf(0), "kick");
        MessageUtils.sendCMD(user, "kick", msg);
      }
    }
    return ResponseUtils.jsonView(Integer.valueOf(200));
  }
  
  @AuthPassport
  @RequestMapping(value={"/setPartner"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView setPartner(@RequestParam Integer id, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    GcRoomMember m = (GcRoomMember)this.roomService.get(GcRoomMember.class, id);
    String roomId = m.getRoomId();
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(500, "无权执行该操作!");
    }
    if (m != null) {
      m.setIsPartner("1");
      this.roomService.update(GcRoomMember.class, m);
    }
    return ResponseUtils.jsonView(Integer.valueOf(200));
  }
  
  @AuthPassport
  @RequestMapping(value={"/cancelPartner"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView cancelPartner(@RequestParam Integer id, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    GcRoomMember m = (GcRoomMember)this.roomService.get(GcRoomMember.class, id);
    String roomId = m.getRoomId();
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(500, "无权执行该操作!");
    }
    if (m != null) {
      m.setIsPartner("0");
      this.roomService.update(GcRoomMember.class, m);
    }
    return ResponseUtils.jsonView(Integer.valueOf(200));
  }
  
  @AuthPassport
  @RequestMapping(value={"/saveRate"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView saveRate(@RequestParam Integer id, @RequestParam Double rate, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    GcRoomMember m = (GcRoomMember)this.roomService.get(GcRoomMember.class, id);
    String roomId = m.getRoomId();
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      return ResponseUtils.jsonView(404, "房间" + roomId + "不存在.");
    }
    if (!room.getOwner().equals(uid)) {
      return ResponseUtils.jsonView(500, "无权执行该操作!");
    }
    List<GcRoomMember> ls = this.roomService.findByProperties(GcRoomMember.class, ImmutableMap.of("roomId", roomId));
    Double fullRateExceptCurrent = Double.valueOf(0.0D);
    for (GcRoomMember mb : ls) {
      if (!mb.getId().equals(m.getId())) {
        fullRateExceptCurrent = Double.valueOf(fullRateExceptCurrent.doubleValue() + mb.getRate().doubleValue());
      }
    }
    
    if (m != null) {
      if (fullRateExceptCurrent.doubleValue() + rate.doubleValue() > 100.0D) {
        return ResponseUtils.jsonView(500, "可设置最大股份:" + (100.0D - fullRateExceptCurrent.doubleValue()));
      }
      m.setRate(rate);
      this.roomService.update(GcRoomMember.class, m);
    }
    return ResponseUtils.jsonView(Integer.valueOf(200));
  }
  
  @AuthPassport
  @RequestMapping(value={"/updateProp"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView updateProp(@RequestParam String roomId, @RequestParam String key, @RequestParam String value, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    
    Room r = (Room)this.roomStore.get(roomId);
    if ((r == null) || (!r.getOwner().equals(uid))) {
      return ResponseUtils.jsonView(500, "无权执行该操作!");
    }
    try {
      this.roomService.modifyRoomInfo(roomId, key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(Integer.valueOf(200));
  }
  
  @AuthPassport
  @RequestMapping(value={"/dismiss"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView dismiss(@RequestParam String roomId, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try {
      Double restMoney = this.roomService.dismissRoom(roomId, uid);
      return ResponseUtils.jsonView(200, "ok", restMoney);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @AuthPassport
  @RequestMapping(value={"/addMoney"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView addMoney(@RequestParam String roomId, @RequestParam Integer money, HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try {
      this.roomService.addMoney(roomId, uid, money.intValue());
      return ResponseUtils.jsonView(200, "ok");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @RequestMapping(value={"/roomApplyConfig"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView roomApplyConfig(HttpServletRequest request) {
    Double d = Double.valueOf(SystemConfigService.getInstance().getValue("conf_room_money").toString());
    return ResponseUtils.jsonView(200, "success", d);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\RoomConcactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */