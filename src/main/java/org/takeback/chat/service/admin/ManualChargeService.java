package org.takeback.chat.service.admin;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.context.ContextUtils;




@Service("manualChargeService")
public class ManualChargeService
  extends MyListService
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    Integer id = Integer.valueOf((String)data.get("id"));
    

    String hql1 = "select status from PubRecharge where id=" + id;
    String status = (String)this.dao.getUnique(hql1, new Object[0]);
    if ("1".equals(status)) {
      data.put("status", "2");
      HttpServletRequest request = (HttpServletRequest)ContextUtils.get("$httpRequest");
      data.put("operator", WebUtils.getSessionAttribute(request, "$uid"));
      Integer uid = Integer.valueOf((String)data.get("uid"));
      String tradeno = (String)data.get("tradeno");
      Double fee = Double.valueOf(data.get("fee") == null ? 0.0D : Double.valueOf((String)data.get("fee")).doubleValue());
      Double gift = Double.valueOf(data.get("gift") == null ? 0.0D : Double.valueOf((String)data.get("gift")).doubleValue());
      String hql = "update PubUser set money = COALESCE(money,0) + :add where id=:uid";
      Map<String, Object> param = new HashMap();
      param.put("add", Double.valueOf(fee.doubleValue() + gift.doubleValue()));
      param.put("uid", uid);
      this.dao.executeUpdate(hql, param);
      
      super.save(req);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\ManualChargeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */