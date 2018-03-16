package org.takeback.chat.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
public class Chat implements WebSocketMessageBrokerConfigurer
{
  private static final Set<Chat> connections = new CopyOnWriteArraySet();
  





















  private String nickName;
  




















  private Session session;
  





















  private static void broadCast(String message)
  {
    for (Chat chat : connections) {
      try {
        synchronized (chat) {
          chat.session.getBasicRemote().sendText(message);
        }
      } catch (IOException e) {
        connections.remove(chat);
        try {
          chat.session.close();
        }
        catch (IOException localIOException2) {}
        broadCast(String.format("System> %s %s", new Object[] { chat.nickName, " has bean disconnection." }));
      }
    }
  }
  


  public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry)
  {
    stompEndpointRegistry.addEndpoint(new String[] { "/coordination" }).withSockJS();
  }
  



  public void configureWebSocketTransport(WebSocketTransportRegistration webSocketTransportRegistration) {}
  



  public void configureClientInboundChannel(ChannelRegistration channelRegistration) {}
  



  public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {}
  


  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {}
  


  public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {}
  


  public boolean configureMessageConverters(List<MessageConverter> list)
  {
    return true;
  }
  
  public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry)
  {
    System.out.println("服务器启动成功");
    



    messageBrokerRegistry.enableSimpleBroker(new String[] { "/userChat" });
    messageBrokerRegistry.setApplicationDestinationPrefixes(new String[] { "/app" });
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\Chat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */