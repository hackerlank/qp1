package org.takeback.util.converter.support;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.core.convert.converter.Converter;

public class StringToElement implements Converter<String, Element>
{
  public Element convert(String source)
  {
    try
    {
      return org.dom4j.DocumentHelper.parseText(source).getRootElement();
    }
    catch (DocumentException e) {
      throw new IllegalArgumentException("Failed to parse xml " + source + ", cause: " + e.getMessage(), e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */