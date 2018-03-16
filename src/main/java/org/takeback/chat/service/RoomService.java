package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.GcRoomMoney;
import org.takeback.chat.entity.GcRoomProperty;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;
import org.takeback.util.exception.CodedBaseRuntimeException;

@Service
public class RoomService
  extends BaseService
{
  @Autowired
  RoomStore roomStore;
  private static int ONCE_FETCH_COUNT = 5;
  
  public static int KICK_TIME = 1800;
  
  @Transactional(readOnly=true)
  public List<GcRoom> getRooms(int pageNo, Map<String, Object> params) {
    return this.dao.find(GcRoom.class, params, ONCE_FETCH_COUNT, pageNo, "hot desc,createdate desc");
  }
  
  @Transactional(readOnly=true)
  public List<GcRoom> getActivedRooms() {
    return this.dao.findByHql("from GcRoom a where a.status != '9' order by a.hot desc,a.createdate desc", null);
  }
  
  @Transactional(readOnly=true)
  public Map<String, Object> getRoomProperties(String roomId) {
    Map<String, Object> param = new HashMap();
    param.put("roomId", roomId);
    List<GcRoomProperty> list = this.dao.findByHql("from GcRoomProperty a where roomId=:roomId order by id asc ", param);
    Map<String, Object> res = new HashMap();
    for (GcRoomProperty prop : list) {
      res.put(prop.getConfigKey(), prop.getConfigValue());
    }
    return res;
  }
  
  @Transactional(readOnly=true)
  public int getUserRoomCount(Integer userId) {
    return (int)this.dao.count(GcRoom.class, ImmutableMap.of("owner", userId));
  }
  
  @Transactional(readOnly=true)
  public List<GcRoom> getUserRooms(Integer userId, int pageSize, int pageNo) {
    return this.dao.findByHqlPaging("from GcRoom where owner=:owner", ImmutableMap.of("owner", userId), pageSize, pageNo);
  }
  
  @Transactional
  public void initRoomStatus() {
    this.dao.executeUpdate("update GcRoom set status ='0' ", new HashedMap());
    List<GcRoom> list = this.dao.findByHql("from GcRoom");
    for (GcRoom rm : list) {
      this.roomStore.reload(rm.getId());
    }
  }
  
  @Transactional(readOnly=true)
  public List<Map<String, String>> getRoomProps(String roomId) {
    List<GcRoomProperty> list = this.dao.findByHql("from GcRoomProperty where roomId=:roomId order by id", ImmutableMap.of("roomId", roomId));
    if ((list == null) || (list.isEmpty())) {
      return new ArrayList();
    }
    List<Map<String, String>> props = new ArrayList(list.size());
    for (GcRoomProperty gcRoomProperty : list) {
      String alias = StringUtils.isEmpty(gcRoomProperty.getAlias()) ? gcRoomProperty.getConfigKey() : gcRoomProperty.getAlias();
      Map<String, String> map = ImmutableMap.of("key", gcRoomProperty.getConfigKey(), "value", gcRoomProperty.getConfigValue(), "alias", alias);
      props.add(map);
    }
    return props;
  }
  
  @Transactional(readOnly=true)
  public Map<String, String> getQunInfo(String roomId) {
    List<GcRoomMoney> ls = this.dao.findByProperty(GcRoomMoney.class, "roomId", roomId);
    Map info = new HashedMap();
    if (ls.size() == 1) {
      GcRoomMoney grm = (GcRoomMoney)ls.get(0);
      info.put("restMoney", grm.getRestMoney());
    }
    
    return info;
  }
  
  @Transactional
  public void addMoney(String roomId, Integer uid, int money) {
    List<GcRoomMoney> ls = this.dao.findByProperty(GcRoomMoney.class, "roomId", roomId);
    if (ls.size() > 0) {
      int effected = this.dao.executeUpdate("update PubUser set money =money -:money where id = :uid and  money > :money", ImmutableMap.of("money", Double.valueOf(money + 0.0D), "uid", uid));
      if (effected == 0) {
        throw new CodedBaseRuntimeException("账户金币不足,请及时充值!");
      }
      this.dao.executeUpdate("update GcRoomMoney set totalMoney=totalMoney+:money , restMoney = restMoney+:money where roomId = :roomId", ImmutableMap.of("money", Double.valueOf(money + 0.0D), "roomId", roomId));
    } else {
      throw new CodedBaseRuntimeException("房间不存在!");
    }
  }
  
  @Transactional
  public Double dismissRoom(String roomId, Integer uid) {
    Double restMoney = Double.valueOf(0.0D);
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      throw new CodedBaseRuntimeException("房间不存在!");
    }
    if (!room.getOwner().equals(uid)) {
      throw new CodedBaseRuntimeException("无权解散房间!");
    }
    List<GcRoomMoney> ls = this.dao.findByProperty(GcRoomMoney.class, "roomId", roomId);
    if (ls.size() > 0) {
      GcRoomMoney rec = (GcRoomMoney)ls.get(0);
      if (rec.getRestMoney().doubleValue() > 0.0D) {
        this.dao.executeUpdate("update PubUser set money =money +:money where id = :uid", ImmutableMap.of("money", rec.getRestMoney(), "uid", uid));
      }
      this.dao.executeUpdate("delete from GcRoom where id=:roomId", ImmutableMap.of("roomId", roomId));
      this.dao.executeUpdate("delete from GcRoomProperty where roomId=:roomId", ImmutableMap.of("roomId", roomId));
      this.dao.executeUpdate("delete from GcRoomMember where roomId=:roomId", ImmutableMap.of("roomId", roomId));
      this.dao.executeUpdate("delete from GcRoomMoney where roomId=:roomId", ImmutableMap.of("roomId", roomId));
      restMoney = rec.getRestMoney();
    }
    return restMoney;
  }
  
  @Transactional
  public void modifyRoomInfo(String roomId, String key, String value) {
    if (key.equals("name")) {
      if (value.length() > 10) {
        throw new CodedBaseRuntimeException("房间名不能超过10个字符!");
      }
      List<GcRoom> ls = this.dao.findByProperty(GcRoom.class, "name", value);
      if ((ls.size() > 0) && 
        (!((GcRoom)ls.get(0)).getId().equals(roomId))) {
        throw new CodedBaseRuntimeException("房间名已经存在!");
      }
      
      this.dao.executeUpdate("update GcRoom set name = :name where id=:id", ImmutableMap.of("name", value, "id", roomId));
      this.roomStore.reload(roomId);
    } else if (key.equals("id")) {
      if (value.length() > 10) {
        throw new CodedBaseRuntimeException("房间ID不能超过10个字符!");
      }
      GcRoom r = (GcRoom)this.dao.get(GcRoom.class, value);
      if ((r != null) && (!r.getId().equals(roomId))) {
        throw new CodedBaseRuntimeException("房间ID已经存在!");
      }
      this.dao.executeUpdate("update GcRoom set id = :newId where id=:id", ImmutableMap.of("newId", value, "id", roomId));
      this.dao.executeUpdate("update GcRoomProperty set roomId = :newId where roomId=:id", ImmutableMap.of("newId", value, "id", roomId));
      
      this.dao.executeUpdate("update GcRoomMember set roomId = :newId where roomId=:id", ImmutableMap.of("newId", value, "id", roomId));
      this.dao.executeUpdate("update GcRoomMoney set roomId = :newId where roomId=:id", ImmutableMap.of("newId", value, "id", roomId));
      
      this.roomStore.reload(roomId);
      this.roomStore.reload(value);
    } else if (key.equals("psw")) {
      if (value.length() > 6) {
        throw new CodedBaseRuntimeException("密码不能超过6个字符!");
      }
      this.dao.executeUpdate("update GcRoom set psw = :psw where id=:id", ImmutableMap.of("psw", value, "id", roomId));
      this.roomStore.reload(roomId);
    }
    else {
      Integer v = Integer.valueOf(0);
      try {
        v = Integer.valueOf(value);
      } catch (Exception e) {
        throw new CodedBaseRuntimeException("必须是整数数字!");
      }
      if (v.intValue() < 1) {
        throw new CodedBaseRuntimeException("请填写大于0的整数!");
      }
      
      Room r = (Room)this.roomStore.get(roomId);
      
      if ((key.startsWith("conf_n")) && (!"conf_n15".equals(key))) {
        Double d = Double.valueOf(r.getProperties().get("conf_n15").toString());
        if (v.intValue() > d.doubleValue()) {
          throw new CodedBaseRuntimeException("任何牛牛点数赔率不能超过豹子赔率!");
        }
      } else if ("conf_n15".equals(key)) {
        for (int i = 1; i < 15; i++) {
          Double d = Double.valueOf(r.getProperties().get("conf_n" + i).toString());
          if (v.intValue() < d.doubleValue()) {
            throw new CodedBaseRuntimeException("豹子赔率必须大于其他点数赔率!");
          }
        }
      }
      this.dao.executeUpdate("update GcRoomProperty set configValue=:value where roomId=:roomId and configKey=:key", ImmutableMap.of("value", v.toString(), "roomId", roomId, "key", key));
      this.roomStore.reload(roomId);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\RoomService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */