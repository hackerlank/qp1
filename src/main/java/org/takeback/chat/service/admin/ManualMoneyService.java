package org.takeback.chat.service.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.PubUser;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;


@Service("manualMoneyService")
public class ManualMoneyService
  extends MyListServiceInt
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String userIdText = (String)data.get("userIdText");
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "userId", userIdText);
    if (user == null) {
      throw new CodedBaseRuntimeException("用户不存在!");
    }
    try
    {
      money = Double.valueOf(data.get("money").toString());
    } catch (Exception e) { Double money;
      e.printStackTrace();
      throw new CodedBaseRuntimeException("金额不正确");
    }
    Double money;
    String hql = "update PubUser a set a.money = Coalesce(a.money,0) + :money where a.id=:uid";
    Map<String, Object> param = new HashMap();
    param.put("money", money);
    param.put("uid", user.getId());
    int i = this.dao.executeUpdate(hql, param);
    if (i == 1) {
      data.put("userId", user.getId());
      data.put("createTime", new Date());
      HttpServletRequest request = (HttpServletRequest)ContextUtils.get("$httpRequest");
      data.put("operator", WebUtils.getSessionAttribute(request, "$uid"));
      super.save(req);
    } else {
      throw new CodedBaseRuntimeException("余额增加失败!");
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\ManualMoneyService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */