package org.takeback.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HqlHelper
{
  public static String getWhere(String value, String key, Map<String, Object> map)
  {
    StringBuilder where = new StringBuilder();
    String[] t = value.split(",");
    if (t.length != 3) {
      return "506,Illegal arguments !";
    }
    Pattern ipattern = Pattern.compile("^\\d+$|-\\d+$");
    Pattern fpattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
    if ((!fpattern.matcher(t[0]).matches()) && 
      (!ipattern.matcher(t[0]).matches())) {
      return "507,Illegal arguments !";
    }
    switch (t[2]) {
    case "gt": 
      where.append(" ").append(key).append(" > :").append(key);
      map.put(key, Float.valueOf(Float.parseFloat(t[0])));
      break;
    case "lt": 
      where.append(" ").append(key).append(" < :").append(key);
      map.put(key, Float.valueOf(Float.parseFloat(t[0])));
      break;
    default: 
      if ((!fpattern.matcher(t[1]).matches()) && 
        (!ipattern.matcher(t[1]).matches())) {
        return "508,Illegal arguments !";
      }
      if (Float.parseFloat(t[0]) > Float.parseFloat(t[1])) {
        return "509,Illegal arguments !";
      }
      where.append(" ").append(key).append(" >= ").append(t[0]).append(" and ");
      where.append(key).append(" <= ").append(t[1]);
    }
    
    return where.toString();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\HqlHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */