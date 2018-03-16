package org.takeback.chat.service;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.ControlModel;
import org.takeback.chat.entity.ValueControlLog;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.service.BaseService;



@Service
public class GameMonitor
{
  private static final Integer START_CONTROL = Integer.valueOf(0);
  
  @Autowired
  RoomStore roomStore;
  
  @Autowired
  UserStore userStore;
  
  @Autowired
  BaseService baseService;
  private static GameMonitor instance;
  private static Integer[][][] NN = { { {
    Integer.valueOf(0), Integer.valueOf(1) }, { Integer.valueOf(9), Integer.valueOf(2) }, { Integer.valueOf(8), Integer.valueOf(3) }, { Integer.valueOf(7), Integer.valueOf(4) }, { Integer.valueOf(5), Integer.valueOf(6) } }, { {
    Integer.valueOf(0), Integer.valueOf(2) }, { Integer.valueOf(9), Integer.valueOf(3) }, { Integer.valueOf(8), Integer.valueOf(4) }, { Integer.valueOf(7), Integer.valueOf(5) } }, { {
    Integer.valueOf(0), Integer.valueOf(3) }, { Integer.valueOf(1), Integer.valueOf(2) }, { Integer.valueOf(9), Integer.valueOf(4) }, { Integer.valueOf(8), Integer.valueOf(5) }, { Integer.valueOf(7), Integer.valueOf(6) } }, { {
    Integer.valueOf(0), Integer.valueOf(4) }, { Integer.valueOf(1), Integer.valueOf(3) }, { Integer.valueOf(2), Integer.valueOf(2) }, { Integer.valueOf(9), Integer.valueOf(5) }, { Integer.valueOf(8), Integer.valueOf(6) } }, { {
    Integer.valueOf(0), Integer.valueOf(5) }, { Integer.valueOf(1), Integer.valueOf(4) }, { Integer.valueOf(2), Integer.valueOf(3) }, { Integer.valueOf(9), Integer.valueOf(6) }, { Integer.valueOf(8), Integer.valueOf(7) } }, { {
    Integer.valueOf(0), Integer.valueOf(6) }, { Integer.valueOf(1), Integer.valueOf(5) }, { Integer.valueOf(2), Integer.valueOf(4) }, { Integer.valueOf(3), Integer.valueOf(3) }, { Integer.valueOf(8), Integer.valueOf(8) }, { Integer.valueOf(9), Integer.valueOf(7) } }, { {
    Integer.valueOf(0), Integer.valueOf(7) }, { Integer.valueOf(1), Integer.valueOf(6) }, { Integer.valueOf(2), Integer.valueOf(5) }, { Integer.valueOf(3), Integer.valueOf(4) }, { Integer.valueOf(9), Integer.valueOf(8) } }, { {
    Integer.valueOf(0), Integer.valueOf(8) }, { Integer.valueOf(1), Integer.valueOf(7) }, { Integer.valueOf(2), Integer.valueOf(6) }, { Integer.valueOf(3), Integer.valueOf(5) }, { Integer.valueOf(4), Integer.valueOf(4) }, { Integer.valueOf(9), Integer.valueOf(9) } }, { {
    Integer.valueOf(0), Integer.valueOf(9) }, { Integer.valueOf(1), Integer.valueOf(8) }, { Integer.valueOf(2), Integer.valueOf(7) }, { Integer.valueOf(3), Integer.valueOf(6) }, { Integer.valueOf(4), Integer.valueOf(5) } }, { {
    Integer.valueOf(0), Integer.valueOf(0) }, { Integer.valueOf(1), Integer.valueOf(9) }, { Integer.valueOf(2), Integer.valueOf(8) }, { Integer.valueOf(3), Integer.valueOf(7) }, { Integer.valueOf(4), Integer.valueOf(6) }, { Integer.valueOf(5), Integer.valueOf(5) } } };
  
  public GameMonitor()
  {
    instance = this;
  }
  
  public static GameMonitor getInstance() {
    return instance;
  }
  
  private List<ControlModel> cache = new ArrayList();
  
  public void setData(String roomId, Integer uid, Double inoutNum) {
    try {
      Room r = (Room)this.roomStore.get(roomId);
      User u = (User)this.userStore.get(uid);
      ControlModel c = getByRoomAndUid(roomId, uid);
      if (c == null) {
        c = new ControlModel(roomId, r.getName(), uid, u.getUserId(), u.getNickName(), Double.valueOf(NumberUtil.round(Double.valueOf(SystemConfigService.getInstance().getValue("control_default_rate")).doubleValue() / 100.0D)));
        this.cache.add(c);
      }
      c.setInoutNum(inoutNum);
      if ("9".equals(u.getUserType())) {
        return;
      }
      
      if (("1".equals(SystemConfigService.getInstance().getValue("control_flag"))) && 
        (c.getPlayTimes().intValue() >= START_CONTROL.intValue())) {
        doControl(c);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void doControl(ControlModel c) {
    Double subRate = Double.valueOf(c.getCurrentRate().doubleValue() - c.getTargetRate().doubleValue());
    Double winControlRate = Double.valueOf(Double.valueOf(SystemConfigService.getInstance().getValue("control_kill")).doubleValue() / 100.0D);
    Double winForceControlRate = Double.valueOf(winControlRate.doubleValue() + 0.1D);
    Double loseControlRate = Double.valueOf(-Double.valueOf(SystemConfigService.getInstance().getValue("control_save")).doubleValue() / 100.0D);
    Room r = (Room)this.roomStore.get(c.getRoomId());
    if (subRate.doubleValue() > winControlRate.doubleValue()) {
      if (subRate.doubleValue() > winForceControlRate.doubleValue()) {
        c.setSuggests(null);
      }
      Double suggest = getPointMaybeLose(r);
      if ((suggest != null) && (
        (c.getSuggests() == null) || ("".equals(c.getSuggests())))) {
        c.setSuggests(suggest.toString());
      }
    }
    else if (subRate.doubleValue() < loseControlRate.doubleValue()) {
      Double suggest = getPointMaybeWin(r);
      if ((suggest != null) && (
        (c.getSuggests() == null) || ("".equals(c.getSuggests())))) {
        c.setSuggests(suggest.toString());
      }
    }
  }
  

  private Double getPointMaybeWin(Room r)
  {
    if (r.getType().startsWith("G01")) {
      Double money = Double.valueOf(r.getProperties().get("conf_money").toString());
      Integer size = Integer.valueOf(r.getProperties().get("conf_size").toString());
      Double targetMoney = Double.valueOf(money.doubleValue() / size.intValue());
      Double addMoney = Double.valueOf(targetMoney.doubleValue() * (RandomUtils.nextInt(2) + RandomUtils.nextDouble()));
      targetMoney = Double.valueOf(targetMoney.doubleValue() + addMoney.doubleValue() * Math.random());
      return Double.valueOf(NumberUtil.round(targetMoney.doubleValue())); }
    if (r.getType().startsWith("G02"))
    {
      Integer point1 = Integer.valueOf(RandomUtils.nextInt(5) + 5);
      return generateNNValue(point1);
    }
    return null;
  }
  
  private static Double generateNNValue(Integer point) {
    Integer[][] conf = NN[point.intValue()];
    Integer random = Integer.valueOf(RandomUtils.nextInt(conf.length - 1));
    Integer[] data = conf[random.intValue()];
    if (RandomUtils.nextInt() % 2 == 0) {
      return Double.valueOf(NumberUtil.round(data[0].intValue() / 10.0D + data[1].intValue() / 100.0D));
    }
    return Double.valueOf(NumberUtil.round(data[1].intValue() / 10.0D + data[0].intValue() / 100.0D));
  }
  
  public static void main(String... args) {
    for (int i = 0; i < 1000; i++) {
      Double money = Double.valueOf(1000.0D);
      Integer size = Integer.valueOf(10);
      size = Integer.valueOf(size.intValue() + (int)(money.doubleValue() / 10.0D));
      Double targetMoney = Double.valueOf(money.doubleValue() / size.intValue());
      targetMoney = Double.valueOf(targetMoney.doubleValue() * Math.random());
      if (targetMoney.doubleValue() == 0.0D) {
        targetMoney = Double.valueOf(0.02D);
      }
      System.out.println(targetMoney);
    }
  }
  
  private Double getPointMaybeLose(Room r) {
    if (r.getType().startsWith("G01")) {
      Double money = Double.valueOf(r.getProperties().get("conf_money").toString());
      Integer size = Integer.valueOf(r.getProperties().get("conf_size").toString());
      
      size = Integer.valueOf(size.intValue() + (int)(money.doubleValue() / 10.0D));
      
      Double targetMoney = Double.valueOf(money.doubleValue() / size.intValue());
      targetMoney = Double.valueOf(targetMoney.doubleValue() * Math.random());
      if (targetMoney.doubleValue() == 0.0D) {
        targetMoney = Double.valueOf(0.02D);
      }
      return Double.valueOf(NumberUtil.round(targetMoney.doubleValue())); }
    if (r.getType().startsWith("G02"))
    {
      Integer point1 = Integer.valueOf(RandomUtils.nextInt(4));
      if (point1.intValue() == 0) {
        point1 = Integer.valueOf(1);
      }
      return generateNNValue(point1);
    }
    
    return null;
  }
  
  private ControlModel getByRoomAndUid(String roomId, Integer uid)
  {
    for (ControlModel c : this.cache) {
      if ((c.getRoomId().equals(roomId)) && (c.getUid().equals(uid))) {
        return c;
      }
    }
    return null;
  }
  
  public List<ControlModel> userList() {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      User u = (User)this.userStore.get(c.getUid());
      if (false == "9".equals(u.getUserType())) {
        sub.add(c);
      }
    }
    Collections.sort(sub);
    return sub;
  }
  
  public List<ControlModel> robotsList() {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      User u = (User)this.userStore.get(c.getUid());
      if ("9".equals(u.getUserType())) {
        sub.add(c);
      }
    }
    Collections.sort(sub);
    return sub;
  }
  
  public List<ControlModel> listByRoomId(String roomId)
  {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      if (c.getRoomId().equals(roomId)) {
        User u = (User)this.userStore.get(c.getUid());
        if (!"9".equals(u.getUserType()))
        {

          sub.add(c); }
      }
    }
    Collections.sort(sub);
    return sub;
  }
  
  public List<ControlModel> listByUid(Integer uid)
  {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      if (c.getUid().equals(uid)) {
        sub.add(c);
      }
    }
    Collections.sort(sub);
    return sub;
  }
  
  public ControlModel getById(Long id) {
    for (ControlModel c : this.cache) {
      if (c.getId().equals(id)) {
        return c;
      }
    }
    return null;
  }
  
  public List<ControlModel> listByUserId(String userId) {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      User u = (User)this.userStore.get(c.getUid());
      if ((c.getUserId().equals(userId)) && 
        (false == "9".equals(u.getUserType()))) {
        sub.add(c);
      }
    }
    
    Collections.sort(sub);
    return sub;
  }
  
  public ControlModel getByRoomIdAndUId(String roomId, Integer uid) {
    List<ControlModel> sub = new ArrayList();
    for (ControlModel c : this.cache) {
      User u = (User)this.userStore.get(c.getUid());
      if ((c.getRoomId().equals(roomId)) && (c.getUid().equals(uid))) {
        return c;
      }
    }
    return null;
  }
  
  public void cleanUsers() {
    this.cache = new ArrayList();
  }
  
  public BigDecimal getOne(String roomId, Integer uid) {
    ControlModel c = getByRoomAndUid(roomId, uid);
    if (c == null) {
      return null;
    }
    String values = c.getSuggests();
    if ((values != null) && (values.length() > 0)) {
      String[] v = values.split(",");
      return new BigDecimal(v[0]);
    }
    return null;
  }
  
  @Transactional
  public void deleteOne(String roomId, Integer uid) {
    try {
      ControlModel c = getByRoomAndUid(roomId, uid);
      if (c == null) {
        return;
      }
      String values = c.getSuggests();
      if ((values != null) && (values.length() > 0)) {
        String[] v = values.split(",");
        if (v.length == 0) {
          return;
        }
        
        String suggest = "";
        String deleteValue = v[0];
        for (int i = 1; i < v.length; i++) {
          suggest = suggest + v[i] + ",";
        }
        if (suggest.length() > 0) {
          suggest = suggest.substring(0, suggest.length() - 1);
        }
        c.setSuggests(suggest);
        
        ValueControlLog vcl = new ValueControlLog();
        User u = (User)this.userStore.get(uid);
        vcl.setCreateDate(new Date());
        vcl.setNickName(u.getUserId());
        vcl.setRoomId(roomId);
        vcl.setUid(uid);
        vcl.setRoomName(((Room)this.roomStore.get(roomId)).getName());
        vcl.setVal(Double.valueOf(deleteValue));
        vcl.setAdmin("监控模块");
        this.baseService.save(ValueControlLog.class, vcl);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\GameMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */