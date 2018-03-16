package org.takeback.chat.websocket.core;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.websocket.listener.ConnectListener;
import org.takeback.chat.websocket.listener.DisconnectListener;
import org.takeback.chat.websocket.listener.MessageReceiveListener;
import org.takeback.chat.websocket.listener.TransportErrorListener;
import org.takeback.util.exception.CodedBase;
import org.takeback.util.exception.CodedBaseRuntimeException;

public class ChatCoreWebSocketHandler
  implements WebSocketHandler
{
  private static final Logger log = LoggerFactory.getLogger(ChatCoreWebSocketHandler.class);
  
  @Resource
  private List<ConnectListener> connectListeners;
  
  @Resource
  private List<DisconnectListener> disconnectListeners;
  
  @Resource
  private List<MessageReceiveListener> messageReceiveListeners;
  
  @Resource
  private List<TransportErrorListener> transportErrorListeners;
  
  public void afterConnectionEstablished(WebSocketSession session)
    throws Exception
  {
    if (this.connectListeners != null) {
      for (ConnectListener connectListener : this.connectListeners) {
        try {
          connectListener.onConnect(session);
        } catch (Exception e) {
          if ((e instanceof CodedBase)) {
            log.error("{} connect failed. code:{} msg:{}", new Object[] { session, Integer.valueOf(((CodedBase)e).getCode()), e.getMessage() });
          } else {
            log.error("Connect failed", e);
          }
        }
      }
    }
  }
  









  public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage)
    throws Exception
  {
    if (this.messageReceiveListeners != null) {
      for (MessageReceiveListener messageReceiveListener : this.messageReceiveListeners) {
        try {
          messageReceiveListener.onMessageReceive(session, webSocketMessage);
        } catch (CodedBaseRuntimeException e) {
          log.error("{} message received error. code:{} msg:{} body:{}", new Object[] { session, Integer.valueOf(e.getCode()), e.getMessage(), webSocketMessage });
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  


















  public void handleTransportError(WebSocketSession session, Throwable throwable)
    throws Exception
  {
    if (this.transportErrorListeners != null) {
      for (TransportErrorListener transportErrorListener : this.transportErrorListeners) {
        transportErrorListener.onTransportError(session, throwable);
      }
    }
  }
  





  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
    throws Exception
  {
    if (this.disconnectListeners != null) {
      for (DisconnectListener disconnectListener : this.disconnectListeners) {
        try {
          disconnectListener.onDisconnect(session, closeStatus);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  







  public boolean supportsPartialMessages()
  {
    return false;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\core\ChatCoreWebSocketHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */