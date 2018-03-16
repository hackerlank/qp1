package org.takeback.chat.service.admin;

import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubConfig;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;



@Component
@Service("configService")
public class SystemConfigService
  extends MyListServiceInt
  implements ApplicationContextAware
{
  public static final String CTRL_FLAG = "control_flag";
  public static final String CTRL_DEFAULT_RATE = "control_default_rate";
  public static final String CTRL_KILL = "control_kill";
  public static final String CTRL_SAVE = "control_save";
  public static final String CTRL_INIT_MONEY = "conf_init_money";
  public static final String CTRL_TALK = "conf_talk";
  public static final String CTRL_TRANSFER = "conf_transfer";
  public static final String CTRL_PROXY_RECHARGE = "conf_proxyRecharge";
  public static final String CTRL_PROXY_WITHDRAW = "conf_proxyWithdraw";
  private List<PubConfig> cache;
  private static SystemConfigService instance;
  
  public static SystemConfigService getInstance()
  {
    return instance;
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void save(Map<String, Object> req) {
    super.save(req);
    reload();
  }
  
  @Transactional(readOnly=true)
  public String getValue(String key) {
    if (this.cache == null) {
      reload();
    }
    for (PubConfig c : this.cache) {
      if (c.getParam().equals(key)) {
        return c.getVal();
      }
    }
    return null;
  }
  
  private void reload() {
    this.cache = this.dao.findByHql("from PubConfig");
  }
  
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
  {
    instance = (SystemConfigService)applicationContext.getBean(SystemConfigService.class);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\SystemConfigService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */