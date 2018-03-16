package org.takeback.util.converter.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.takeback.util.BeanUtils;



public class MapToObject
  implements GenericConverter
{
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (sourceType.isMap()) {
      try {
        Object target = targetType.getType().newInstance();
        Map<String, Object> map = (Map)source;
        
        BeanUtils.copy(map, target);
        

        return target;
      }
      catch (Exception e)
      {
        throw new IllegalStateException("falied to convert map to bean", e);
      }
    }
    
    throw new IllegalStateException("source object must be a map");
  }
  

  public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
  {
    Set<GenericConverter.ConvertiblePair> set = new HashSet();
    set.add(new GenericConverter.ConvertiblePair(Map.class, Object.class));
    return set;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\MapToObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */