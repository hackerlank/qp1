package org.takeback.chat.websocket.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer
{
  private String uri = "chat";
  
  public WebSocketConfig() {}
  
  public WebSocketConfig(String uri) {
    this.uri = uri;
  }
  
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
  {
    registry.addHandler(chatCoreWebSocketHandler(), new String[] { "/" + this.uri }).addInterceptors(new org.springframework.web.socket.server.HandshakeInterceptor[] { httpSessionHandshakeInterceptor() }).setAllowedOrigins(new String[] { "*" });
  }
  



  @Bean(name={"chatCoreWebSocketHandler"})
  public WebSocketHandler chatCoreWebSocketHandler()
  {
    return new ChatCoreWebSocketHandler();
  }
  
  @Bean(name={"handshakeInterceptor"})
  public HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
    return new HandshakeInterceptor();
  }
  
  @Bean
  public ServletServerContainerFactoryBean createWebSocketContainer() {
    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
    container.setMaxTextMessageBufferSize(8192);
    container.setMaxBinaryMessageBufferSize(8192);
    return container;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\core\WebSocketConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */