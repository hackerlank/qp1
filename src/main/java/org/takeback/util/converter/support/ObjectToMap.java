package org.takeback.util.converter.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.takeback.util.BeanUtils;


public class ObjectToMap
  implements GenericConverter
{
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (targetType.isMap()) {
      try {
        return BeanUtils.map(source, HashMap.class);
      }
      catch (Exception e) {
        throw new IllegalStateException("falied to convert map to bean", e);
      }
    }
    
    throw new IllegalStateException("source object must be a map");
  }
  

  public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
  {
    Set<GenericConverter.ConvertiblePair> set = new HashSet();
    set.add(new GenericConverter.ConvertiblePair(Object.class, Map.class));
    return set;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\ObjectToMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */