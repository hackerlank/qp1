package org.takeback.util.converter.support;

import org.dom4j.Element;
import org.springframework.core.convert.converter.Converter;

public class ElementToString implements Converter<Element, String>
{
  public String convert(Element source)
  {
    return source.asXML();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\ElementToString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */