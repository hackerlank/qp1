package org.takeback.mvc.resolver;

import java.io.PrintStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ExceptionResolver
  implements HandlerExceptionResolver
{
  public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
  {
    System.out.println("e----------------------------");
    e.printStackTrace();
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\resolver\ExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */