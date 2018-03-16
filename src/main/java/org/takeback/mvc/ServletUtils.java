package org.takeback.mvc;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

































public class ServletUtils
{
  public static final String TOKEN = "$token";
  
  public static String getClientIP(HttpServletRequest request)
  {
    String ip = request.getHeader("x-forwarded-for");
    if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase("unknown"))) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase("unknown"))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase("unknown"))) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
  




  public static boolean isLogonExpired(HttpServletRequest request)
  {
    return (WebUtils.getSessionAttribute(request, "$uid") == null) || 
      (WebUtils.getSessionAttribute(request, "$urt") == null);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\ServletUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */