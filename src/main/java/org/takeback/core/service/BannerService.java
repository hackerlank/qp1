package org.takeback.core.service;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.dao.BaseDAO;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;

@org.springframework.stereotype.Service("bannerService")
public class BannerService extends MyListService
{
  private String bannerPath = "/content/images/banner";
  
  @Transactional
  public Object save1(Map<String, Object> req) {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    Map<String, Object> data = (Map)req.get("data");
    beforeProcessSaveData(data);
    try {
      Class<?> cls = Class.forName(entityName);
      Object obj = ConversionUtils.convert(data, cls);
      beforeSave(obj);
      this.dao.getSession().saveOrUpdate(obj);
      return obj;
    } catch (ClassNotFoundException e) {
      throw new CodedBaseRuntimeException(510, "parse class[" + entityName + "] failed");
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\service\BannerService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */