package org.takeback.core.service;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.takeback.dao.BaseDAO;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;
import org.takeback.util.params.Param;
import org.takeback.util.params.ParamUtils;

@Service("systemParamsService")
public class SystemParamsService
  extends MyListService
{
  protected void beforeSave(Object obj)
  {
    Param p = (Param)obj;
    ParamUtils.reload(p.getParamname());
  }
  
  public void delete(Map<String, Object> req)
  {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    Object pkey = req.get("id");
    Serializable id = null;
    if ((pkey instanceof Integer)) {
      id = (Serializable)ConversionUtils.convert(pkey, Long.class);
    } else {
      id = (Serializable)ConversionUtils.convert(pkey, String.class);
    }
    Param p = (Param)this.dao.get(entityName, id);
    if (p != null) {
      ParamUtils.reload(p.getParamname());
      this.dao.delete(entityName, id);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\service\SystemParamsService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */