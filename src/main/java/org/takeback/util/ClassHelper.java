package org.takeback.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClassHelper
{
  public static final String ARRAY_SUFFIX = "[]";
  private static final String INTERNAL_ARRAY_PREFIX = "[L";
  
  public static ClassLoader getClassLoader(Class<?> cls)
  {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    }
    catch (Throwable localThrowable) {}
    

    if (cl == null)
    {
      cl = cls.getClassLoader();
    }
    return cl;
  }
  













  public static ClassLoader getClassLoader()
  {
    return getClassLoader(ClassHelper.class);
  }
  


  public static Class<?> forName(String name)
    throws ClassNotFoundException
  {
    return forName(name, getClassLoader());
  }
  
















  public static Class<?> forName(String name, ClassLoader classLoader)
    throws ClassNotFoundException, LinkageError
  {
    if (name.equals("void")) {
      return Void.TYPE;
    }
    
    Class<?> clazz = resolvePrimitiveClassName(name);
    if (clazz != null) {
      return clazz;
    }
    

    if (name.endsWith("[]")) {
      String elementClassName = name.substring(0, name.length() - "[]".length());
      Class<?> elementClass = forName(elementClassName, classLoader);
      return java.lang.reflect.Array.newInstance(elementClass, 0).getClass();
    }
    

    int internalArrayMarker = name.indexOf("[L");
    if ((internalArrayMarker != -1) && (name.endsWith(";"))) {
      String elementClassName = null;
      if (internalArrayMarker == 0) {
        elementClassName = name.substring("[L".length(), name.length() - 1);
      } else if (name.startsWith("[")) {
        elementClassName = name.substring(1);
      }
      Class<?> elementClass = forName(elementClassName, classLoader);
      return java.lang.reflect.Array.newInstance(elementClass, 0).getClass();
    }
    
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = getClassLoader();
    }
    return classLoaderToUse.loadClass(name);
  }
  












  public static Class<?> resolvePrimitiveClassName(String name)
  {
    Class<?> result = null;
    

    if ((name != null) && (name.length() <= 8))
    {
      result = (Class)primitiveTypeNameMap.get(name);
    }
    return result;
  }
  









  private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap(16);
  




  private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap(8);
  
  static {
    primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
    primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
    primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
    primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
    primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
    primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
    primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
    primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
    
    Set<Class<?>> primitiveTypeNames = new java.util.HashSet(16);
    primitiveTypeNames.addAll(primitiveWrapperTypeMap.values());
    primitiveTypeNames.addAll(java.util.Arrays.asList(new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class }));
    
    for (Iterator<Class<?>> it = primitiveTypeNames.iterator(); it.hasNext();) {
      Class<?> primitiveClass = (Class)it.next();
      primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
    }
  }
  
  public static Class<?> resolvePrimitiveClassName(Class<?> type) {
    return (Class)primitiveWrapperTypeMap.get(type);
  }
  
  public static String toShortString(Object obj) {
    if (obj == null) {
      return "null";
    }
    return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\ClassHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */