package org.takeback.util.converter.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.springframework.core.convert.converter.Converter;

public class StringToDocument implements Converter<String, Document>
{
  public Document convert(String source)
  {
    try
    {
      return org.dom4j.DocumentHelper.parseText(source);
    }
    catch (DocumentException e) {
      throw new IllegalArgumentException("Failed to parse xml " + source + ", cause: " + e.getMessage(), e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */