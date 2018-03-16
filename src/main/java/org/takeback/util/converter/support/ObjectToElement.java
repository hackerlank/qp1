package org.takeback.util.converter.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.takeback.util.BeanUtils;
import org.takeback.util.converter.ConversionUtils;



public class ObjectToElement
  implements GenericConverter
{
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    try
    {
      Map<String, Object> map = (Map)BeanUtils.map(source, HashMap.class);
      Element beanEl = DocumentHelper.createElement(source.getClass().getSimpleName());
      Set<String> fields = map.keySet();
      for (String field : fields) {
        Element fieldEl = DocumentHelper.createElement(field);
        Object val = map.get(field);
        
        if (val != null) {
          fieldEl.setText((String)ConversionUtils.convert(val, String.class));
        }
        beanEl.add(fieldEl);
      }
      return beanEl;
    }
    catch (Exception e) {
      throw new IllegalStateException("falied to convert bean to element", e);
    }
  }
  

  public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
  {
    Set<GenericConverter.ConvertiblePair> set = new HashSet();
    set.add(new GenericConverter.ConvertiblePair(Object.class, Element.class));
    return set;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\ObjectToElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */