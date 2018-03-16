package org.takeback.chat.service;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;


@Service
public class SystemService
  extends BaseService
{
  @Transactional(rollbackFor={Throwable.class})
  public Long getWidthdraw()
  {
    Map<String, Object> param = new HashMap();
    param.put("status", "1");
    Long count = Long.valueOf(this.dao.count("select count(*) from PubWithdraw where status =:status", param));
    return count;
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public Map<String, Object> getProxyConfig() {
    Double money = Double.valueOf(SystemConfigService.getInstance().getValue("conf_proxy_money"));
    Double exp = Double.valueOf(SystemConfigService.getInstance().getValue("conf_proxy_exp"));
    Map body = new HashedMap();
    body.put("money", money);
    body.put("exp", exp);
    return body;
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public Long getRecharge() {
    Map<String, Object> param = new HashMap();
    param.put("status", "1");
    Long count = Long.valueOf(this.dao.count("select count(*) from PubRecharge where status=:status", param));
    return count;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\SystemService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */