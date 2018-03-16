package org.takeback.chat.websocket.listener.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.service.SystemService;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.chat.service.support.OrdMessageProcessor;
import org.takeback.chat.service.support.RedMessageProcessor;
import org.takeback.chat.service.support.TxtMessageProcessor;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;
import org.takeback.chat.websocket.listener.MessageReceiveListener;
import org.takeback.util.JSONUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;

public class MessageLisnener implements MessageReceiveListener
{
  private static final Logger log = LoggerFactory.getLogger(MessageLisnener.class);
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private UserStore userStore;
  @Autowired
  private TxtMessageProcessor txtMessageProcessor;
  @Autowired
  private RedMessageProcessor redMessageProcessor;
  @Autowired
  private OrdMessageProcessor ordMessageProcessor;
  @Autowired
  private SystemService systemService;
  
  public void onMessageReceive(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
    Integer uid = UserListener.getUid(session);
    String roomId = UserListener.getRoomId(session);
    Room room = (Room)this.roomStore.get(roomId);
    if (room == null) {
      throw new CodedBaseRuntimeException(522, "room with id " + roomId + " not exists");
    }
    Message message = processMessage(webSocketMessage);
    String messageType = message.getType();
    
    if ((!messageType.equals("CMD")) && (!isSigned(session))) {
      return;
    }
    

    if ((messageType.equals("TXT")) && (isCloseTalk(session))) {
      return;
    }
    User user = null;
    if (uid != null) {
      user = (User)this.userStore.get(uid);
      message.setSender(user.getId());
      message.setNickName(user.getNickName());
      message.setHeadImg(user.getHeadImg());
    }
    log.info("receive msg {}", JSONUtils.toString(message));
    switch (messageType) {
    case "TXT": 
      this.txtMessageProcessor.process(message, session, room, user);
      break;
    case "RED": 
      this.redMessageProcessor.process(message, session, room, user);
      break;
    case "CMD": 
    case "ORD": 
      this.ordMessageProcessor.process(message, session, room, user);
    }
  }
  
  private Message processMessage(WebSocketMessage<?> webSocketMessage)
  {
    try {
      return (Message)JSONUtils.parse((String)webSocketMessage.getPayload(), Message.class);
    }
    catch (Exception e) {
      throw new CodedBaseRuntimeException(501, "process msg " + webSocketMessage.getPayload() + " failed", e);
    }
  }
  
  protected boolean isSigned(WebSocketSession session) {
    Integer uid = UserListener.getUid(session);
    if (uid == null) {
      MessageUtils.sendCMD(session, "notLogin", "注册用户才可以发言和参与游戏");
      return false;
    }
    return true;
  }
  
  protected boolean isCloseTalk(WebSocketSession session) { this.systemService.getProxyConfig();
    String confTalk = SystemConfigService.getInstance().getValue("conf_talk");
    if ("0".equals(confTalk)) {
      MessageUtils.sendCMD(session, "alert", "禁止发言");
      return true;
    }
    return false;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\listener\support\MessageLisnener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */