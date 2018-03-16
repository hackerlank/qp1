package org.takeback.util.converter.support;

import org.springframework.core.convert.converter.Converter;

public class IntegerToBoolean implements Converter<Integer, Boolean>
{
  public Boolean convert(Integer source)
  {
    if (source.intValue() == 0) {
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\IntegerToBoolean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */