package org.takeback.util;

import java.util.HashMap;
import java.util.Map;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.mvel2.MVEL;
import org.takeback.util.converter.ConversionUtils;

public class BeanUtils
{
  private static final Mapper dozer = ;
  
  public static <T> T map(Object source, Class<T> destinationClass) {
    return (T)dozer.map(source, destinationClass);
  }
  
  public static <T> T map(Object source, Object dest)
  {
    dozer.map(source, dest);
    return (T)dest;
  }
  
  public static void copy(Object source, Object dest) {
    dozer.map(source, dest);
  }
  
  public static Object getProperty(Object bean, String nm) {
    Object val = null;
    val = MVEL.getProperty(nm, bean);
    return val;
  }
  
  public static <T> T getProperty(Object bean, String nm, Class<T> type) {
    Object val = getProperty(bean, nm);
    return (T)ConversionUtils.convert(val, type);
  }
  
  public static void setProperty(Object bean, String nm, Object v) {
    MVEL.setProperty(bean, nm, v);
  }
  
  public static void setPropertyInMap(Object bean, String nm, Object v) throws Exception {
    Map<String, Object> vars = new HashMap();
    vars.put("key", nm);
    vars.put("value", v);
    MVEL.eval("setProperty(key,value)", bean, vars);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\BeanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */