package org.takeback.chat.websocket.listener.support;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.GcRoomMember;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.AnonymousUser;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.FailedResult;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.websocket.listener.ConnectListener;
import org.takeback.chat.websocket.listener.DisconnectListener;
import org.takeback.chat.websocket.listener.TransportErrorListener;
import org.takeback.util.exception.CodedBaseRuntimeException;







public class UserListener
  implements ConnectListener, DisconnectListener, TransportErrorListener
{
  private static final Logger log = LoggerFactory.getLogger(UserListener.class);
  
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  private LotteryService lotteryService;
  public static final String ROOMID = "roomId";
  public static final String LAST_ROOM_ID = "lastRoomId";
  
  public void onConnect(WebSocketSession session)
  {
    String roomId = getRoomId(session);
    if (StringUtils.isEmpty(roomId)) {
      return;
    }
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      throw new CodedBaseRuntimeException(522, "room with id " + roomId + " not exists");
    }
    
    Integer uid = getUid(session);
    
    boolean needNotice = false;
    List<GcRoomMember> ls; if (uid != null) {
      User user = (User)this.userStore.get(uid);
      String lastRoomId = getLastRoomId(session);
      if (lastRoomId != null) {
        Room lastRoom = (Room)this.roomStore.get(lastRoomId);
        if (lastRoom != null) {
          letUserLeftRoom(user, lastRoom, true);
          removeAttribute(session, "lastRoomId");
        }
      }
      this.roomStore.fireUserLeft(user);
      
      user.setWebSocketSession(session);
      user.setHandsUp(Boolean.valueOf(false));
      room.join(user);
      if ("G022".equals(room.getType())) {
        ls = this.lotteryService.findByProperties(GcRoomMember.class, ImmutableMap.of("roomId", roomId, "uid", uid));
        if (ls.size() == 0) {
          GcRoomMember grm = new GcRoomMember();
          grm.setIsPartner("0");
          grm.setRate(Double.valueOf(0.0D));
          grm.setJoinDate(new Date());
          grm.setRoomId(room.getId());
          grm.setNickName(user.getNickName());
          grm.setUid(user.getId());
          grm.setUserId(user.getUserId());
          this.lotteryService.save(GcRoomMember.class, grm);
        }
      }
      
      needNotice = true;
      log.info("user {} join in the room {}", user.getUserId(), roomId);
    } else {
      User user = new AnonymousUser(session);
      room.guestJoin((AnonymousUser)user);
      log.info("anonymous user {} join in the room {}", session.getId(), roomId);
    }
    Map<String, Lottery> lotterys;
    if (needNotice)
    {
























      lotterys = room.getLotteries().asMap();
      for (String k : lotterys.keySet()) {
        Lottery lottery = (Lottery)lotterys.get(k);
        

        if ((lottery.isOpen()) && ("0".equals(lottery.getStatus())))
        {
          Date createTime = lottery.getCreateTime();
          Room r = (Room)this.roomStore.get(lottery.getRoomId());
          if (r.getType().startsWith("G01")) {
            Integer delay = Integer.valueOf(r.getProperties().get("conf_rest_time").toString());
            if ((new Date().getTime() - createTime.getTime()) / 1000L < delay.intValue() + 1) {}

          }
          else
          {
            Integer senderId = lottery.getSender();
            Message lotteryMsg;
            if ((senderId == null) || (senderId.intValue() == 0)) {
              Message lotteryMsg = new Message("RED_SYS", Integer.valueOf(0), lottery);
              lotteryMsg.setHeadImg("img/avatar.png");
              lotteryMsg.setNickName("系统");
            } else {
              User sender = (User)this.userStore.get(senderId);
              lotteryMsg = new Message("RED", senderId, lottery);
              lotteryMsg.setNickName(sender.getNickName());
              lotteryMsg.setHeadImg(sender.getHeadImg());
            }
            
            MessageUtils.send(uid, room, lotteryMsg);
          }
        }
      }
    }
  }
  
  public void onDisconnect(WebSocketSession session, CloseStatus closeStatus) {
    String roomId = getRoomId(session);
    if (StringUtils.isEmpty(roomId)) {
      return;
    }
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      throw new CodedBaseRuntimeException(522, "room with id " + roomId + " not exists");
    }
    Integer uid = getUid(session);
    
    if (uid != null) {
      User user = (User)this.userStore.get(uid);
      letUserLeftRoom(user, room, true);
      removeAttribute(session, "roomId");
    } else {
      User user = new AnonymousUser(session);
      room.guestLeft((AnonymousUser)user);
      log.info("anonymous user {} left the room {}", session.getId(), roomId);
    }
  }
  
  private void letUserLeftRoom(User user, Room room, boolean broadcast) {
    room.left(user);
    log.info("user {} left the room {}", user.getUserId(), room.getId());
    if (broadcast) {
      List<FailedResult> results = MessageUtils.broadcast(room, new Message("TXT_SYS", user.getId(), user.getNickName() + " 离开了房间."));
      if (results.size() <= 0) {}
    }
  }
  


  public void onTransportError(WebSocketSession session, Throwable throwable)
  {
    onDisconnect(session, null);
    if (session.isOpen()) {
      try {
        session.close();
      } catch (IOException e) {
        throw new CodedBaseRuntimeException(500, "try to close session failed.");
      }
    }
  }
  
  protected static String getRoomId(WebSocketSession session) {
    String roomId = (String)getSessionAttribute(session, "roomId");
    


    return roomId;
  }
  
  protected static Integer getUid(WebSocketSession session) {
    HttpSession httpSession = (HttpSession)session.getAttributes().get("HTTP.SESSION");
    if (httpSession == null) {
      return null;
    }
    try {
      return (Integer)httpSession.getAttribute("$uid");
    } catch (IllegalStateException e) {}
    return null;
  }
  
  protected static String getLastRoomId(WebSocketSession session)
  {
    return (String)getSessionAttribute(session, "lastRoomId");
  }
  
  protected static Object getSessionAttribute(WebSocketSession session, String name) {
    Object value = session.getAttributes().get(name);
    if (value == null) {
      HttpSession httpSession = (HttpSession)session.getAttributes().get("HTTP.SESSION");
      try {
        if (httpSession != null) {
          value = httpSession.getAttribute(name);
        }
      }
      catch (IllegalStateException localIllegalStateException) {}
    }
    
    return value;
  }
  
  protected static void removeAttribute(WebSocketSession session, String name) {
    session.getAttributes().remove(name);
    HttpSession httpSession = (HttpSession)session.getAttributes().get("HTTP.SESSION");
    try {
      if (httpSession != null) {
        httpSession.removeAttribute(name);
      }
    }
    catch (IllegalStateException localIllegalStateException) {}
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\listener\support\UserListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */