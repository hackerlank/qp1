package org.takeback.chat.service.admin;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubExchangeLog;
import org.takeback.core.service.MyListServiceInt;
import org.takeback.dao.BaseDAO;
import org.takeback.util.exception.CodedBaseRuntimeException;








@Service("exchangeAdminService")
public class ExchangeAdminService
  extends MyListServiceInt
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    String status = (String)data.get("status");
    Double money = Double.valueOf(data.get("money").toString());
    String hql = "from PubExchangeLog where id=:id";
    Integer id = Integer.valueOf(data.get("id").toString());
    PubExchangeLog log = (PubExchangeLog)this.dao.findByHql(hql, ImmutableMap.of("id", id)).get(0);
    if ((!status.equals(log.getStatus())) && (!"1".equals(log.getStatus()))) {
      throw new CodedBaseRuntimeException(500, "处理状态已更新，不能重复修改");
    }
    if (("2".equals(status)) && (!"2".equals(log.getStatus()))) {
      Integer uid = Integer.valueOf(data.get("uid").toString());
      String upd = "update PubUser set money =coalesce(money,0) + :money where id=:uid";
      this.dao.executeUpdate(upd, ImmutableMap.of("money", money, "uid", uid));
    }
    if (("1".equals(status)) && ("2".equals(log.getStatus()))) {
      Integer uid = Integer.valueOf(data.get("uid").toString());
      String upd = "update PubUser set money =coalesce(money,0) - :money where id=:uid";
      this.dao.executeUpdate(upd, ImmutableMap.of("money", money, "uid", uid));
    }
    super.save(req);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\ExchangeAdminService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */