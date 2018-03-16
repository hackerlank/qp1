package org.takeback.chat.utils;

import com.google.common.collect.Maps;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ValueControl
{
  private static Map<String, Map<Integer, Vector<BigDecimal>>> store = ;
  

  private static final int MAX_SIZE = 50;
  


  public static BigDecimal getValue(String roomId, Integer uid)
  {
    Map<Integer, Vector<BigDecimal>> room = (Map)store.get(roomId);
    if ((room == null) || (room.size() == 0)) {
      return null;
    }
    Vector<BigDecimal> userValues = (Vector)room.get(uid);
    if ((userValues == null) || (userValues.size() == 0)) {
      return null;
    }
    BigDecimal bd = (BigDecimal)userValues.get(0);
    userValues.remove(0);
    return bd;
  }
  
  public static void clean(String roomId, Integer uid) {
    Map<Integer, Vector<BigDecimal>> room = (Map)store.get(roomId);
    if ((room == null) || (room.size() == 0)) {
      return;
    }
    Vector<BigDecimal> userValues = (Vector)room.get(uid);
    if ((userValues == null) || (userValues.size() == 0)) {
      return;
    }
    room.remove(uid);
  }
  
  public static void setValue(String roomId, Integer uid, BigDecimal value) {
    Map<Integer, Vector<BigDecimal>> room = (Map)store.get(roomId);
    if (room == null) {
      room = Maps.newConcurrentMap();
      store.put(roomId, room);
    }
    
    Vector<BigDecimal> userQueue = (Vector)room.get(uid);
    if (userQueue == null) {
      userQueue = new Vector();
      room.put(uid, userQueue);
    }
    userQueue.add(value);
  }
  
  public static Map<String, Map<Integer, Vector<BigDecimal>>> getStore() {
    return store;
  }
  
  public static Map<Integer, Vector<BigDecimal>> getByRoomId(String roomId) {
    return (Map)store.get(roomId);
  }
  
  public static List<Map<String, Object>> query()
  {
    List<Map<String, Object>> res = new java.util.ArrayList();
    Iterator roomItr = store.keySet().iterator();
    while (roomItr.hasNext()) {
      String roomId = (String)roomItr.next();
      Map<Integer, Vector<BigDecimal>> room = (Map)store.get(roomId);
      Iterator userItr = room.keySet().iterator();
      while (userItr.hasNext()) {
        Integer uid = (Integer)userItr.next();
        Vector v = (Vector)room.get(uid);
        for (int i = 0; i < v.size(); i++) {
          Map<String, Object> rec = new java.util.HashMap();
          rec.put("roomId", roomId);
          rec.put("uid", uid);
          rec.put("value", v.get(i));
          res.add(rec);
        }
      }
    }
    return res;
  }
  
  public static void main(String... args) {
    setValue("room1", Integer.valueOf(0), new BigDecimal(0.55D));
    setValue("room1", Integer.valueOf(1), new BigDecimal(0.22D));
    setValue("room1", Integer.valueOf(1), new BigDecimal(0.15D));
    setValue("room1", Integer.valueOf(2), new BigDecimal(0.35D));
    setValue("room1", Integer.valueOf(3), new BigDecimal(0.14D));
    
    System.out.println(getValue("room1", Integer.valueOf(1)));
    System.out.println(query());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\utils\ValueControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */