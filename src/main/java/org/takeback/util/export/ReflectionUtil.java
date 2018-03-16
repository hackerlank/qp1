package org.takeback.util.export;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


public class ReflectionUtil
{
  private static Log logger = LogFactory.getLog(ReflectionUtil.class);
  
  static {
    DateLocaleConverter dc = new DateLocaleConverter();
    
    ConvertUtils.register(dc, Date.class);
  }
  


  public static Object invokeGetterMethod(Object target, String propertyName)
  {
    String getterMethodName = "get" + StringUtils.capitalize(propertyName);
    return invokeMethod(target, getterMethodName, new Class[0], new Object[0]);
  }
  




  public static void invokeSetterMethod(Object target, String propertyName, Object value)
  {
    invokeSetterMethod(target, propertyName, value, null);
  }
  





  public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType)
  {
    Class<?> type = propertyType != null ? propertyType : value.getClass();
    String setterMethodName = "set" + StringUtils.capitalize(propertyName);
    invokeMethod(target, setterMethodName, new Class[] { type }, new Object[] { value });
  }
  




  public static Object getFieldValue(Object object, String fieldName)
  {
    Field field = getDeclaredField(object, fieldName);
    
    if (field == null) {
      throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
    }
    

    makeAccessible(field);
    
    Object result = null;
    try {
      result = field.get(object);
    } catch (IllegalAccessException e) {
      logger.error("不可能抛出的异常{}" + e.getMessage());
    }
    return result;
  }
  



  public static void setFieldValue(Object object, String fieldName, Object value)
  {
    Field field = getDeclaredField(object, fieldName);
    
    if (field == null) {
      throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
    }
    

    makeAccessible(field);
    try
    {
      field.set(object, value);
    } catch (IllegalAccessException e) {
      logger.error("不可能抛出的异常:{}" + e.getMessage());
    }
  }
  




  public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
  {
    Method method = getDeclaredMethod(object, methodName, parameterTypes);
    if (method == null) {
      throw new IllegalArgumentException("Could not find method [" + methodName + "] parameterType " + parameterTypes + " on target [" + object + "]");
    }
    


    method.setAccessible(true);
    try
    {
      return method.invoke(object, parameters);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }
  





  protected static Field getDeclaredField(Object object, String fieldName)
  {
    Assert.notNull(object, "object不能为空");
    Assert.hasText(fieldName, "fieldName");
    for (Class<?> superClass = object.getClass(); superClass != Object.class; 
        superClass = superClass.getSuperclass()) {
      try {
        return superClass.getDeclaredField(fieldName);
      }
      catch (NoSuchFieldException localNoSuchFieldException) {}
    }
    
    return null;
  }
  


  protected static void makeAccessible(Field field)
  {
    if ((!Modifier.isPublic(field.getModifiers())) || 
      (!Modifier.isPublic(field.getDeclaringClass().getModifiers()))) {
      field.setAccessible(true);
    }
  }
  




  protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes)
  {
    Assert.notNull(object, "object不能为空");
    
    for (Class<?> superClass = object.getClass(); superClass != Object.class; 
        superClass = superClass.getSuperclass()) {
      try {
        return superClass.getDeclaredMethod(methodName, parameterTypes);
      }
      catch (NoSuchMethodException localNoSuchMethodException) {}
    }
    
    return null;
  }
  








  public static <T> Class<T> getSuperClassGenricType(Class<?> clazz)
  {
    return getSuperClassGenricType(clazz, 0);
  }
  











  public static Class getSuperClassGenricType(Class<?> clazz, int index)
  {
    Type genType = clazz.getGenericSuperclass();
    
    if (!(genType instanceof ParameterizedType)) {
      logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
      
      return Object.class;
    }
    
    Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
    
    if ((index >= params.length) || (index < 0)) {
      logger.warn("Index: " + index + ", Size of " + clazz
        .getSimpleName() + "'s Parameterized Type: " + params.length);
      
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
      
      return Object.class;
    }
    
    return (Class)params[index];
  }
  






  public static List convertElementPropertyToList(Collection collection, String propertyName)
  {
    List list = new ArrayList();
    try
    {
      for (Object obj : collection) {
        list.add(PropertyUtils.getProperty(obj, propertyName));
      }
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
    
    return list;
  }
  








  public static String convertElementPropertyToString(Collection collection, String propertyName, String separator)
  {
    List list = convertElementPropertyToList(collection, propertyName);
    return StringUtils.join(list, separator);
  }
  




  public static <T> T convertStringToObject(String value, Class<T> toType)
  {
    try
    {
      return (T)ConvertUtils.convert(value, toType);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }
  



  public static RuntimeException convertReflectionExceptionToUnchecked(Exception e)
  {
    return convertReflectionExceptionToUnchecked(null, e);
  }
  
  public static RuntimeException convertReflectionExceptionToUnchecked(String desc, Exception e)
  {
    desc = desc == null ? "Unexpected Checked Exception." : desc;
    if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException)))
    {

      return new IllegalArgumentException(desc, e); }
    if ((e instanceof InvocationTargetException))
      return new RuntimeException(desc, ((InvocationTargetException)e)
        .getTargetException());
    if ((e instanceof RuntimeException)) {
      return (RuntimeException)e;
    }
    return new RuntimeException(desc, e);
  }
  
  public static final <T> T getNewInstance(Class<T> cls) {
    try {
      return (T)cls.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
  






  public static void copyPorperties(Object dest, Object source, String[] porperties)
    throws InvocationTargetException, IllegalAccessException
  {
    for (String por : porperties) {
      Object srcObj = invokeGetterMethod(source, por);
      logger.debug("属性名：" + por + "------------- 属性值：" + srcObj);
      if (srcObj != null) {
        try {
          BeanUtils.setProperty(dest, por, srcObj);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          throw e;
        } catch (InvocationTargetException e) {
          throw e;
        }
      }
    }
  }
  







  public static void copyPorperties(Object dest, Object source)
    throws IllegalAccessException, InvocationTargetException
  {
    Class<? extends Object> srcCla = source.getClass();
    Field[] fsF = srcCla.getDeclaredFields();
    
    for (Field s : fsF) {
      String name = s.getName();
      Object srcObj = invokeGetterMethod(source, name);
      try {
        BeanUtils.setProperty(dest, name, srcObj);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        throw e;
      } catch (InvocationTargetException e) {
        throw e;
      }
    }
  }
  
  public static void main(String[] args)
    throws InvocationTargetException, IllegalAccessException
  {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\ReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */