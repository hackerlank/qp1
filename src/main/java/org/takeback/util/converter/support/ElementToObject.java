package org.takeback.util.converter.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.takeback.util.BeanUtils;

public class ElementToObject
  implements GenericConverter
{
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (Element.class.isInstance(source)) {
      try {
        Element el = (Element)source;
        Object dest = targetType.getType().newInstance();
        
        List<Attribute> attrs = el.attributes();
        for (Attribute attr : attrs) {
          try {
            BeanUtils.setProperty(dest, attr.getName(), attr.getValue());
          } catch (Exception e) {
            try {
              BeanUtils.setPropertyInMap(dest, attr.getName(), attr.getValue());
            }
            catch (Exception localException1) {}
          }
        }
        
        return dest;
      } catch (Exception e) {
        throw new IllegalStateException("failed to convert element to bean", e);
      }
    }
    throw new IllegalStateException("source object must be a Element");
  }
  

  public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
  {
    Set<GenericConverter.ConvertiblePair> set = new HashSet();
    set.add(new GenericConverter.ConvertiblePair(Element.class, Object.class));
    return set;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\ElementToObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */