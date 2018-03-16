package org.takeback.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.core.user.User;
import org.takeback.core.user.UserRoleToken;
import org.takeback.dao.BaseDAO;



@Service("initializeService")
public class InitializeService
{
  @Autowired
  protected BaseDAO dao;
  
  @Transactional(readOnly=true)
  public Boolean queryInitialized()
  {
    long count = this.dao.count(User.class, null);
    return Boolean.valueOf(count > 0L);
  }
  
  @Transactional
  public void initUser(User user, UserRoleToken role) {
    this.dao.save(User.class, user);
    this.dao.save(UserRoleToken.class, role);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\service\InitializeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */