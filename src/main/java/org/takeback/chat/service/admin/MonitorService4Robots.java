package org.takeback.chat.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service("monitorService4Robots")
public class MonitorService4Robots extends MonitorService
{
  public Map<String, Object> list(Map<String, Object> req)
  {
    List<?> ls = this.gameMonitor.robotsList();
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Integer.valueOf(ls.size()));
    result.put("body", ls);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\MonitorService4Robots.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */