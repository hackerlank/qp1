package org.takeback.mvc.interceptor;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.util.JSONUtils;
import org.takeback.util.annotation.AuthPassport;

public class AuthInterceptor extends HandlerInterceptorAdapter
{
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);
  



  @Autowired
  private UserStore userStore;
  




  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception
  {
    if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
      return true;
    }
    AuthPassport authPassport = (AuthPassport)((HandlerMethod)handler).getMethodAnnotation(AuthPassport.class);
    if ((authPassport == null) || (!authPassport.value())) {
      return true;
    }
    




    String uid = request.getHeader("x-access-uid");
    if ((uid == null) || (uid.equals("null"))) {
      writeAuthorizeFailResponse(response);
      LOGGER.error("No x-access-uid found, failed to authorize, URI: {}.", request.getRequestURI());
      return false;
    }
    User user = (User)this.userStore.get(Integer.valueOf(uid));
    if (user == null) {
      writeAuthorizeFailResponse(response);
      LOGGER.error("User not found with id: {}, failed to authorize, URI: {}.", uid, request.getRequestURI());
      return false;
    }
    String token = request.getHeader("x-access-token");
    if (token == null) {
      writeAuthorizeFailResponse(response);
      LOGGER.error("No x-access-token found, failed to authorize, URI: {}.", request.getRequestURI());
      return false;
    }
    if (!token.equals(user.getAccessToken())) {
      writeAuthorizeFailResponse(response);
      LOGGER.error("Token is not validate, failed to authorize, URI: {}.", request.getRequestURI());
      return false;
    }
    if (user.getTokenExpireTime().compareTo(new Date()) >= 0) {
      if (request.getSession(true).getAttribute("$uid") == null) {
        request.getSession().setAttribute("$uid", user.getId());
      }
      return true;
    }
    writeAuthorizeFailResponse(response);
    return false;
  }
  



  private void writeAuthorizeFailResponse(HttpServletResponse response)
    throws IOException
  {
    String json = JSONUtils.toString(ImmutableMap.of("code", Integer.valueOf(401), "msg", "请登录账号。"));
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\interceptor\AuthInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */