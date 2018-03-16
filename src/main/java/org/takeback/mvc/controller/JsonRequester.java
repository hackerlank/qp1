package org.takeback.mvc.controller;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import org.takeback.core.user.AccountCenter;
import org.takeback.core.user.User;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.ReflectUtil;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;



@RestController("jsonRequester")
public class JsonRequester
{
  private static final Logger log = LoggerFactory.getLogger(JsonRequester.class);
  private static final String SERVICE = "service";
  private static final String METHOD = "method";
  private static final String PARAMETERS = "parameters";
  
  @RequestMapping(value={"/**/*.jsonRequest"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, headers={"content-type=application/json"})
  public Map<String, Object> handle(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
  {
    String service = (String)request.get("service");
    String method = (String)request.get("method");
    if ((StringUtils.isEmpty(service)) || (StringUtils.isEmpty(method))) {
      return ResponseUtils.createBody(400, "missing service or method.");
    }
    if (!ApplicationContextHolder.containBean(service)) {
      return ResponseUtils.createBody(401, "service is not defined in spring.");
    }
    String uid = (String)WebUtils.getSessionAttribute(httpServletRequest, "$uid");
    Long urt = (Long)WebUtils.getSessionAttribute(httpServletRequest, "$urt");
    if ((uid == null) || (urt == null)) {
      return ResponseUtils.createBody(403, "notLogon");
    }
    ContextUtils.put("$urt", AccountCenter.getUser(uid).getUserRoleToken(urt.longValue()));
    ContextUtils.put("$httpRequest", httpServletRequest);
    ContextUtils.put("$httpResponse", httpServletResponse);
    Object s = ApplicationContextHolder.getBean(service);
    Object b = request.get("parameters");
    Object[] parameters = null;
    if (b != null) {
      if ((b instanceof List)) {
        List<?> ps = (List)b;
        int len = ps.size();
        parameters = new Object[len];
        for (int i = 0; i < len; i++) {
          parameters[i] = ps.get(i);
        }
      } else {
        parameters = new Object[1];
        parameters[0] = b;
      }
    }
    Method m = null;
    Object r = null;
    try { Method[] ms;
      if (parameters == null) {
        m = s.getClass().getDeclaredMethod(method, new Class[0]);
        r = m.invoke(s, new Object[0]);
      } else {
        ms = s.getClass().getDeclaredMethods();
        for (Method mm : ms) {
          if (method.equals(mm.getName()))
          {

            if (isCompatible(mm, parameters)) {
              m = mm;
              break;
            } }
        }
        r = m.invoke(s, convertToParameters(m.getParameterTypes(), parameters));
      }
      return ResponseUtils.createBody(r);
    } catch (CodedBaseRuntimeException e) {
      log.error("execute service[" + service + "] with " + method + " failed.", e);
      return ResponseUtils.createBody(e.getCode(), e.getMessage());
    } catch (Exception e) {
      log.error("execute service[" + service + "] with " + method + " failed.", e);
      if (e.getCause() != null) {
        return ResponseUtils.createBody(402, e.getCause().getMessage());
      }
      return ResponseUtils.createBody(402, "execute service[" + service + "] with " + method + " failed.");
    } finally {
      ContextUtils.clear();
    }
  }
  
  private boolean isCompatible(Method m, Object[] parameters) {
    boolean r = true;
    Class<?>[] cls = m.getParameterTypes();
    if (parameters == null) {
      if (cls.length != 0) {
        r = false;
      }
    }
    else if (parameters.length != cls.length) {
      r = false;
    } else {
      for (int i = 0; i < cls.length; i++) {
        if (!ReflectUtil.isCompatible(cls[i], parameters[i])) {
          r = false;
          break;
        }
      }
    }
    
    return r;
  }
  
  private Object[] convertToParameters(Class<?>[] parameterTypes, Object[] args) {
    Object[] parameters = new Object[parameterTypes.length];
    int i = 0;
    for (Class<?> type : parameterTypes) {
      parameters[i] = ConversionUtils.convert(args[i], type);
      i++;
    }
    return parameters;
  }
  
  public String a() {
    return "a";
  }
  
  public int a(int a, String b) {
    return a;
  }
  
  public static void main(String[] args) throws NoSuchMethodException, SecurityException {
    Method m = JsonRequester.class.getDeclaredMethod("a", new Class[0]);
    System.out.println(m.getParameterTypes().length);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\JsonRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */