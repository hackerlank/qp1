package org.takeback.core.service;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.core.dictionary.DictionaryController;
import org.takeback.core.user.User;
import org.takeback.core.user.UserController;
import org.takeback.dao.BaseDAO;
import org.takeback.util.BeanUtils;
import org.takeback.util.MD5StringUtil;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;



@Service("authorityService")
public class AuthorityService
  extends MyListService
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    Map<String, Object> data = (Map)req.get("data");
    String cmd = (String)req.get(CMD);
    beforeProcessSaveData(data);
    try {
      Class<?> cls = Class.forName(entityName);
      User user = (User)ConversionUtils.convert(data, cls);
      String id = user.getId();
      String password = user.getPassword();
      if (!"******".equals(password))
      {
        user.setPassword(MD5StringUtil.MD5Encode(password));
      }
      if ("update".equals(cmd)) {
        User oUser = (User)this.dao.get(User.class, id);
        if ("******".equals(user.getPassword())) {
          user.setPassword(oUser.getPassword());
        }
        BeanUtils.copy(user, oUser);
        beforeSave(oUser);
        this.dao.getSession().update(oUser);
        DictionaryController.instance().reload("dic.users");
        UserController.instance().reload(id);
        return;
      }
      beforeSave(user);
      this.dao.getSession().save(user);
      DictionaryController.instance().reload("dic.users");
    } catch (ClassNotFoundException e) {
      throw new CodedBaseRuntimeException(510, "parse class[" + entityName + "] failed");
    }
  }
  

  protected void afterList(List<?> ls)
  {
    for (Object user : ls) {
      ((User)user).setPassword("******");
    }
  }
  
  protected void afterLoad(Object entity) {
    ((User)entity).setPassword("******");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\service\AuthorityService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */