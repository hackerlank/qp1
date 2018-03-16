package org.takeback.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import org.springframework.core.ParameterNameDiscoverer;

public class ReflectUtil
{
  private static final ParameterNameDiscoverer localVarDiscoverer = new org.springframework.core.DefaultParameterNameDiscoverer();
  
  private static final ImmutableSet<Class<?>> PrimitiveBigTypes = ImmutableSet.builder()
    .add(new Class[] { Integer.class, Character.class, Boolean.class, Long.class, Float.class, Double.class, Character.class, Byte.class, Short.class, String.class, java.util.Date.class, java.sql.Timestamp.class, java.math.BigDecimal.class, java.sql.Date.class, java.sql.Time.class })
    
    .build();
  
  public static boolean isSimpleType(Class<?> c) {
    if (c.isPrimitive()) {
      return true;
    }
    return PrimitiveBigTypes.contains(c);
  }
  
  public static boolean isCompatible(Class<?> c, Object o)
  {
    boolean pt = c.isPrimitive();
    if (o == null) {
      return !pt;
    }
    if (pt) {
      if (c == Integer.TYPE) {
        c = Integer.class;
      } else if (c == Boolean.TYPE) {
        c = Boolean.class;
      } else if (c == Long.TYPE) {
        c = Long.class;
      } else if (c == Float.TYPE) {
        c = Float.class;
      } else if (c == Double.TYPE) {
        c = Double.class;
      } else if (c == Character.TYPE) {
        c = Character.class;
      } else if (c == Byte.TYPE) {
        c = Byte.class;
      } else if (c == Short.TYPE)
        c = Short.class;
    }
    if (c == o.getClass())
      return true;
    return c.isInstance(o);
  }
  
  public static String getPrimitiveBigTypeName(String p) {
    Class<?> c = null;
    switch (p) {
    case "int": 
      c = Integer.class;
      break;
    
    case "boolean": 
      c = Boolean.class;
      break;
    
    case "long": 
      c = Long.class;
      break;
    
    case "float": 
      c = Float.class;
      break;
    
    case "double": 
      c = Double.class;
      break;
    
    case "char": 
      c = Character.class;
      break;
    
    case "byte": 
      c = Byte.class;
    
    case "short": 
      c = Short.class;
    }
    if (c == null) {
      return null;
    }
    return c.getName();
  }
  
  public static Type findParentParameterizedType(Class<?> clz) {
    return findParentParameterizedType(clz, 0);
  }
  
  public static Type findParentParameterizedType(Class<?> clz, int index) {
    Type supr = clz.getGenericSuperclass();
    while (supr != null) {
      if ((supr instanceof ParameterizedType)) {
        ParameterizedType tp = (ParameterizedType)supr;
        Type resultClz = tp.getActualTypeArguments()[index];
        return resultClz;
      }
      Class<?> suprClz = (Class)supr;
      supr = suprClz.getGenericSuperclass();
    }
    
    return null;
  }
  
  public static Type findTypeVariableParameterizedType(TypeVariable<?> tv, Class<?> clz) {
    String nm = tv.getName();
    TypeVariable<?>[] clsTvs = tv.getGenericDeclaration().getTypeParameters();
    
    for (int i = 0; i < clsTvs.length; i++) {
      TypeVariable<?> clsTv = clsTvs[i];
      if (nm.equals(clsTv.getName())) {
        break;
      }
    }
    return findParentParameterizedType(clz, i);
  }
  
  public static String getCodeBase(Class<?> cls) {
    if (cls == null)
      return null;
    ProtectionDomain domain = cls.getProtectionDomain();
    if (domain == null)
      return null;
    CodeSource source = domain.getCodeSource();
    if (source == null)
      return null;
    java.net.URL location = source.getLocation();
    if (location == null)
      return null;
    return location.getFile();
  }
  
  public static String getName(Class<?> c) {
    if (c.isArray()) {
      StringBuilder sb = new StringBuilder();
      do {
        sb.append("[]");
        c = c.getComponentType();
      } while (c.isArray());
      return c.getName() + sb.toString();
    }
    return c.getName();
  }
  
  public static String getName(Method m) {
    StringBuilder ret = new StringBuilder();
    ret.append(getName(m.getReturnType())).append(' ');
    ret.append(m.getName()).append('(');
    Class<?>[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i > 0)
        ret.append(',');
      ret.append(getName(parameterTypes[i]));
    }
    ret.append(')');
    return ret.toString();
  }
  
  public static String getName(Constructor<?> c) {
    StringBuilder ret = new StringBuilder("(");
    Class<?>[] parameterTypes = c.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i > 0)
        ret.append(',');
      ret.append(getName(parameterTypes[i]));
    }
    ret.append(')');
    return ret.toString();
  }
  
  public static String[] getMethodParameterNames(Method m) {
    return localVarDiscoverer.getParameterNames(m);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\ReflectUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */