package org.takeback.chat.service.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.room.RoomThread;
import org.takeback.chat.store.user.RobotUser;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;
import org.takeback.util.exp.ExpressionProcessor;

@Service("robotAdminService")
public class RobotService extends MyListService
{
  @Autowired
  RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  private Map<Integer, Thread> ts = new HashMap();
  
  private Stack<RobotUser> freeRobots = null;
  
  private Map<String, Thread> roomThreads = new HashedMap();
  
  private Map<String, Stack<RobotUser>> workingRobots = new HashMap();
  
  @Transactional(readOnly=true)
  public Map<String, Object> list(Map<String, Object> req) {
    String entityName = "GcRoom";
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    int limit = ((Integer)req.get(LIMIT)).intValue();
    int page = ((Integer)req.get(PAGE)).intValue();
    List<?> cnd = (List)ConversionUtils.convert(req.get(CND), List.class);
    String filter = null;
    if (cnd != null) {
      filter = ExpressionProcessor.instance().toString(cnd);
    }
    String orderInfo = (String)req.get("id");
    List<GcRoom> ls = this.dao.query(entityName, filter, limit, page, orderInfo);
    afterList(ls);
    
    List<Map> list = new ArrayList();
    for (int i = 0; i < ls.size(); i++) {
      GcRoom room = (GcRoom)ls.get(i);
      Map<String, Object> m = new HashMap();
      Room rm = (Room)this.roomStore.get(room.getId());
      int num = 0;
      if (rm != null) {
        num = getRobotSize(rm).intValue();
      }
      m.put("id", room.getId());
      m.put("roomName", room.getName());
      m.put("robotNum", Integer.valueOf(num));
      list.add(m);
    }
    
    long count = this.dao.totalSize(entityName, filter);
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Long.valueOf(count));
    result.put("body", list);
    return result;
  }
  




  private Integer getRobotSize(Room r)
  {
    Map<Integer, User> users = r.getUsers();
    Iterator<Integer> itr = users.keySet().iterator();
    Integer num = Integer.valueOf(0);
    Integer localInteger1; while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      User u = (User)users.get(uid);
      if ((u instanceof RobotUser)) {
        localInteger1 = num;Integer localInteger2 = num = Integer.valueOf(num.intValue() + 1);
      }
    }
    return num;
  }
  
  @Transactional(readOnly=true)
  public Object load(Map<String, Object> req) {
    Object pkey = req.get("id");
    GcRoom room = (GcRoom)this.dao.get(GcRoom.class, pkey.toString());
    Map<String, Object> entity = new HashMap();
    
    Room rm = (Room)this.roomStore.get(room.getId());
    int num = 0;
    if (rm != null) {
      num = getRobotSize(rm).intValue();
    }
    
    entity.put("id", room.getId());
    entity.put("roomName", room.getName());
    entity.put("robotNum", Integer.valueOf(num));
    return entity;
  }
  
  @Transactional
  public void save(Map<String, Object> req) {
    Map<String, Object> data = (Map)req.get("data");
    String id = (String)data.get("id");
    int num = Integer.valueOf((String)data.get("robotNum")).intValue();
    Room rm = (Room)this.roomStore.get(id);
    int curNum = getRobotSize(rm).intValue();
    int change = num - curNum;
    RoomThread rt; if (change > 0) {
      List<RobotUser> robots = getFreeRobots(change);
      Thread thread = (Thread)this.roomThreads.get(rm.getId());
      if (thread == null) {
        rt = new RoomThread();
        rt.setRoom(rm);
        Thread r = new Thread(rt);
        r.start();
        this.roomThreads.put(rm.getId(), r);
      }
      for (RobotUser ru : robots) {
        this.userStore.reload(ru.getId());
        rm.join(ru);
        this.freeRobots.remove(ru);
      }
    } else {
      change = Math.abs(change);
      Thread thread = (Thread)this.roomThreads.get(rm.getId());
      if (thread == null) {
        return;
      }
      for (int i = 0; i < change; i++) {
        RobotUser r = pickRobotInRoom(rm);
        rm.left(r);
        this.freeRobots.add(r);
      }
    }
  }
  








  public RobotUser pickRobotInRoom(Room room)
  {
    Map<Integer, User> users = room.getUsers();
    Iterator<Integer> itr = users.keySet().iterator();
    List<RobotUser> robotsList = new ArrayList();
    while (itr.hasNext()) {
      Integer uid = (Integer)itr.next();
      User u = (User)users.get(uid);
      if ((u instanceof RobotUser)) {
        robotsList.add((RobotUser)u);
      }
    }
    if (robotsList.size() == 0) {
      return null;
    }
    if (robotsList.size() == 1) {
      return (RobotUser)robotsList.get(0);
    }
    
    Integer r = Integer.valueOf(org.apache.commons.lang.math.RandomUtils.nextInt(robotsList.size() - 1));
    return (RobotUser)robotsList.get(r.intValue());
  }
  
  @Transactional(readOnly=true)
  public List<RobotUser> getFreeRobots(int num)
  {
    if (this.freeRobots == null) {
      this.freeRobots = new Stack();
      String hql = "from PubUser where userType=9  order by id asc";
      List<org.takeback.chat.entity.PubUser> rs = this.dao.findByHql(hql);
      for (int i = 0; i < rs.size(); i++) {
        RobotUser r = (RobotUser)BeanUtils.map(rs.get(i), RobotUser.class);
        this.freeRobots.push(r);
      }
    }
    
    if (this.freeRobots.size() < num) {
      throw new CodedBaseRuntimeException("空闲机器人:" + this.freeRobots.size() + "个");
    }
    

    List<RobotUser> robots = new ArrayList();
    for (int i = 0; i < num; i++) {
      robots.add(this.freeRobots.pop());
    }
    
    return robots;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RobotService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */