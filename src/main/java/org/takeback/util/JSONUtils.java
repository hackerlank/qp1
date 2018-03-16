package org.takeback.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class JSONUtils
{
  private static ObjectMapper mapper = new MyObjectMapper();
  
  public static ObjectMapper getMapper() {
    return mapper;
  }
  
  public static <T> T parse(String value, Class<T> clz)
  {
    if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
      return null;
    }
    try {
      return (T)mapper.readValue(value, clz);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static <T> T parse(byte[] bytes, Class<T> clz) {
    try {
      return (T)mapper.readValue(bytes, clz);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static <T> T parse(InputStream ins, Class<T> clz) {
    try {
      return (T)mapper.readValue(ins, clz);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static <T> T parse(Reader reader, Class<T> clz) {
    try {
      return (T)mapper.readValue(reader, clz);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static <T> T update(String value, T object)
  {
    try {
      return (T)mapper.readerForUpdating(object).readValue(value);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static String writeValueAsString(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static void write(OutputStream outs, Object o)
  {
    try {
      mapper.writeValue(outs, o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static void write(Writer writer, Object o) {
    try {
      mapper.writeValue(writer, o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static String toString(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static String toString(Object o, Class<?> clz)
  {
    try {
      return mapper.writerWithType(clz).writeValueAsString(o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public static byte[] toBytes(Object o) {
    try {
      return mapper.writeValueAsBytes(o);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\JSONUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */