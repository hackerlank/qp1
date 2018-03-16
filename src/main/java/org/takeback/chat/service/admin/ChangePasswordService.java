package org.takeback.chat.service.admin;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubUser;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.encrypt.CryptoUtils;


@Service("changePasswordService")
public class ChangePasswordService
  extends MyListServiceInt
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    Integer id = Integer.valueOf(data.get("id").toString());
    String pwd = (String)data.get("pwd");
    
    req.put("id", id);
    PubUser prev = (PubUser)load(req);
    String prevPwd = prev.getPwd();
    
    String salt = prev.getSalt();
    if (!prevPwd.equals(pwd)) {
      String newPwd = CryptoUtils.getHash(pwd, StringUtils.reverse(salt));
      prev.setPwd(newPwd);
      prev.setMoneyCode(newPwd);
      this.dao.saveOrUpdate(PubUser.class, prev);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\ChangePasswordService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */