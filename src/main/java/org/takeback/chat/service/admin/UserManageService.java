package org.takeback.chat.service.admin;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubUser;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.encrypt.CryptoUtils;


@Service("userManageService")
public class UserManageService
  extends MyListServiceInt
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String cmd = (String)req.get(CMD);
    if ("create".equals(cmd)) {
      try {
        String salt = CryptoUtils.getSalt();
        String psw = (String)data.get("pwd");
        data.put("salt", salt);
        
        String pwd = CryptoUtils.getHash(psw, StringUtils.reverse(salt));
        
        data.put("pwd", pwd);
        data.put("moneyCode", pwd);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      PubUser user = (PubUser)this.dao.get(PubUser.class, Integer.valueOf(data.get("id").toString()));
      String oldPwd = user.getPwd();
      if (!oldPwd.equals(data.get("pwd"))) {
        String salt = CryptoUtils.getSalt();
        String psw = (String)data.get("pwd");
        data.put("salt", salt);
        String pwd = CryptoUtils.getHash(psw, StringUtils.reverse(salt));
        data.put("pwd", pwd);
        data.put("moneyCode", pwd);
      }
    }
    super.save(req);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\UserManageService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */