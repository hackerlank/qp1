package org.takeback.chat.service.admin;

import java.util.Date;
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



@Service("messageService")
public class MessageService
  extends MyListServiceInt
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String userName = (String)data.get("userIdText");
    String status = data.get("status") == null ? "0" : data.get("status").toString();
    if ("".equals(status)) {
      status = "0";
    }
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "userId", userName);
    if (user == null) {
      throw new CodedBaseRuntimeException("用户不存在!");
    }
    data.put("userId", user.getId());
    data.put("createTime", new Date());
    data.put("status", status);
    HttpServletRequest request = (HttpServletRequest)ContextUtils.get("$httpRequest");
    data.put("createUser", WebUtils.getSessionAttribute(request, "$uid"));
    super.save(req);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\MessageService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */