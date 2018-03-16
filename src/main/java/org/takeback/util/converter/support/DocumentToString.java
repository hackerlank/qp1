package org.takeback.util.converter.support;

import org.dom4j.Document;
import org.springframework.core.convert.converter.Converter;

public class DocumentToString implements Converter<Document, String>
{
  public String convert(Document source)
  {
    return source.asXML();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\DocumentToString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */