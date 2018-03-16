package org.takeback.chat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.store.user.RobotUser;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;
import org.takeback.util.BeanUtils;

@Service
public class RobotService
  extends BaseService
{
  private List<RobotUser> freeRobots = new ArrayList();
  
  private Integer maxId = null;
  
  @Transactional(readOnly=true)
  public List<RobotUser> getRobots(Integer num) {
    if (this.freeRobots.size() < num.intValue()) {
      load(Integer.valueOf(num.intValue() - this.freeRobots.size()));
    }
    return null;
  }
  
  @Transactional(readOnly=true)
  public List<RobotUser> load(Integer num) {
    String hql = "from PubUser where userType=9 and id>:id order by id asc";
    Map<String, Object> param = new HashMap();
    param.put("id", this.maxId);
    if (this.maxId == null) {
      param.put("id", Integer.valueOf(0));
    }
    List<PubUser> users = this.dao.findByHqlPaging(hql, param, num.intValue(), 1);
    if (users.size() > 0) {
      this.maxId = ((PubUser)users.get(users.size() - 1)).getId();
    }
    
    List<RobotUser> robots = new ArrayList();
    for (int i = 0; i < users.size(); i++) {
      RobotUser r = (RobotUser)BeanUtils.map(users.get(i), RobotUser.class);
      robots.add(r);
    }
    return robots;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\RobotService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */