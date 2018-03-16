package org.takeback.core.schema;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.takeback.util.converter.ConversionUtils;


public class DataTypes
{
  public static final String STRING = "string";
  public static final String INT = "int";
  public static final String LONG = "long";
  public static final String DOUBLE = "double";
  public static final String BOOLEAN = "boolean";
  public static final String DATE = "date";
  public static final String BIGDECIMAL = "bigDecimal";
  public static final String TIME = "timestamp";
  public static final String DATETIME = "datetime";
  public static final String CHAR = "char";
  public static final String BINARY = "binary";
  public static final String OBJECT = "object";
  private static HashMap<String, Class<?>> types = new HashMap();
  
  static {
    types.put("bigDecimal", BigDecimal.class);
    types.put("int", Integer.class);
    types.put("long", Long.class);
    types.put("double", Double.class);
    types.put("string", String.class);
    types.put("date", Date.class);
    types.put("timestamp", Date.class);
    types.put("char", Character.class);
    types.put("boolean", Boolean.class);
    types.put("datetime", Date.class);
    types.put("binary", byte[].class);
    types.put("object", Object.class);
  }
  
  public static Class<?> getTypeClass(String nm) {
    return (Class)types.get(StringUtils.uncapitalize(nm));
  }
  
  public static boolean isSupportType(String type) {
    return types.containsKey(StringUtils.uncapitalize(type));
  }
  
  public static Object toTypeValue(String type, Object value) {
    if (!types.containsKey(type)) {
      throw new IllegalStateException("type[" + type + "] is not supported.");
    }
    return ConversionUtils.convert(value, getTypeClass(type));
  }
  
  public static boolean isNumberType(String type) {
    if (!types.containsKey(type)) {
      return false;
    }
    Class<?> typeClass = getTypeClass(type);
    return Number.class.isAssignableFrom(typeClass);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\schema\DataTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */