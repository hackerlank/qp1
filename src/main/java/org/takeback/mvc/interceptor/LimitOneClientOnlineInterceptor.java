package org.takeback.mvc.interceptor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.takeback.mvc.listener.SessionListener;

public class LimitOneClientOnlineInterceptor
  extends HandlerInterceptorAdapter
{
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    Object uid = WebUtils.getSessionAttribute(request, "$uid");
    if ((uid != null) && ((uid instanceof Integer))) {
      int userId = ((Integer)uid).intValue();
      List<String> sessionIds = SessionListener.getUser(userId);
      if ((sessionIds != null) && (sessionIds.size() > 1)) {
        String sid = WebUtils.getSessionId(request);
        if (sid.equals(sessionIds.get(0))) {
          request.getSession().invalidate();
        }
      }
    }
    return true;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\interceptor\LimitOneClientOnlineInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */