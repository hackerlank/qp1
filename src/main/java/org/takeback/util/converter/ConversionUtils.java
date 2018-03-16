package org.takeback.util.converter;

import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.takeback.util.converter.support.ObjectToElement;
import org.takeback.util.converter.support.StringToSQLDate;

public class ConversionUtils
{
  private static ConfigurableConversionService conversion = new org.springframework.core.convert.support.DefaultConversionService();
  
  static {
    conversion.addConverter(new org.takeback.util.converter.support.LongToDate());
    conversion.addConverter(new org.takeback.util.converter.support.DateToLong());
    conversion.addConverter(new org.takeback.util.converter.support.DateToNumber());
    conversion.addConverter(new org.takeback.util.converter.support.DateToString());
    conversion.addConverter(new org.takeback.util.converter.support.IntegerToBoolean());
    conversion.addConverter(new org.takeback.util.converter.support.StringToDate());
    conversion.addConverter(new StringToSQLDate());
    conversion.addConverter(new org.takeback.util.converter.support.StringToSQLTime());
    conversion.addConverter(new org.takeback.util.converter.support.StringToTimestamp());
    conversion.addConverter(new org.takeback.util.converter.support.StringToMap());
    conversion.addConverter(new org.takeback.util.converter.support.StringToList());
    conversion.addConverter(new org.takeback.util.converter.support.StringToDocument());
    conversion.addConverter(new org.takeback.util.converter.support.StringToElement());
    conversion.addConverter(new org.takeback.util.converter.support.StringToInetSocketAddress());
    conversion.addConverter(new org.takeback.util.converter.support.DocumentToString());
    conversion.addConverter(new org.takeback.util.converter.support.ElementToString());
    conversion.addConverter(new org.takeback.util.converter.support.ElementToObject());
    conversion.addConverter(new ObjectToElement());
    conversion.addConverter(new org.takeback.util.converter.support.MapToObject());
    conversion.addConverter(new org.takeback.util.converter.support.ObjectToMap());
  }
  
  public void setConverters(Set<Converter> converters)
  {
    for (Converter c : converters) {
      conversion.addConverter(c);
    }
  }
  
  public static <T> T convert(Object source, Class<T> targetType)
  {
    if (targetType.isInstance(source)) {
      return (T)source;
    }
    return (T)conversion.convert(source, targetType);
  }
  
  public static boolean canConvert(Class<?> sourceType, Class<?> targetType) {
    return conversion.canConvert(sourceType, targetType);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\ConversionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */