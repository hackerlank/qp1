package org.takeback.chat.service.admin;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.ControlModel;
import org.takeback.chat.service.GameMonitor;
import org.takeback.chat.utils.NumberUtil;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;
import org.takeback.util.exp.ExpressionProcessor;









@Service("monitorService")
public class MonitorService
  extends MyListServiceInt
{
  @Autowired
  GameMonitor gameMonitor;
  
  public Map<String, Object> list(Map<String, Object> req)
  {
    List<?> cnd = (List)ConversionUtils.convert(req.get(CND), List.class);
    String filter = null;
    List<?> ls = null;
    
    if (cnd != null) {
      filter = ExpressionProcessor.instance().toString(cnd);
      if (filter.indexOf("and") > 0) {
        filter = filter.split("and")[1];
      }
      
      String[] exp = filter.split("=");
      String key = exp[0].replaceAll(" ", "");
      String value = exp[1].replaceAll("'", "").replaceAll(" ", "").replaceAll("\\)", "");
      
      if (("".equals(key)) || ("".equals(value))) {
        ls = this.gameMonitor.userList();
      }
      else if ("uid".equals(key)) {
        ls = this.gameMonitor.listByUid(Integer.valueOf(value));
      } else if ("roomId".equals(key)) {
        ls = this.gameMonitor.listByRoomId(value);
      }
    }
    else {
      ls = this.gameMonitor.userList();
    }
    System.out.println("................>>" + filter + "::::" + this.gameMonitor.userList().size());
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Integer.valueOf(ls.size()));
    result.put("body", ls);
    return result;
  }
  
  @Transactional(readOnly=true)
  public Object load(Map<String, Object> req)
  {
    Object pkey = req.get("id");
    Long id = Long.valueOf(pkey.toString());
    ControlModel c = this.gameMonitor.getById(id);
    if (c == null) {
      throw new CodedBaseRuntimeException("记录丢失!");
    }
    afterLoad(c);
    return c;
  }
  
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    Long id = Long.valueOf(data.get("id").toString());
    Double targetRate = Double.valueOf(data.get("targetRateText").toString());
    String suggests = data.get("suggests") == null ? "" : data.get("suggests").toString();
    ControlModel c = this.gameMonitor.getById(id);
    if (c == null) {
      throw new CodedBaseRuntimeException("记录丢失!");
    }
    c.setTargetRate(Double.valueOf(NumberUtil.round(targetRate.doubleValue() / 100.0D)));
    c.setSuggests(suggests);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void clear(Map<String, Object> req) {
    this.gameMonitor.cleanUsers();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\MonitorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */