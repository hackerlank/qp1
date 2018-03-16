package org.takeback.chat.websocket.core;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


public class HandshakeInterceptor
  extends HttpSessionHandshakeInterceptor
{
  public static final String HTTP_SESSION_ATTR_NAME = "HTTP.SESSION";
  
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes)
    throws Exception
  {
    if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
      request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
    }
    




    HttpSession session = getSession(request);
    if (session != null) {
      attributes.put("HTTP.SESSION", session);
    }
    return super.beforeHandshake(request, response, wsHandler, attributes);
  }
  




  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex)
  {
    super.afterHandshake(request, response, wsHandler, ex);
  }
  
  private HttpSession getSession(ServerHttpRequest request) {
    if ((request instanceof ServletServerHttpRequest)) {
      ServletServerHttpRequest serverRequest = (ServletServerHttpRequest)request;
      return serverRequest.getServletRequest().getSession(isCreateSession());
    }
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\websocket\core\HandshakeInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */