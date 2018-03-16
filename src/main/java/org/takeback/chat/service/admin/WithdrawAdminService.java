package org.takeback.chat.service.admin;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.utils.SmsUtil2;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.exception.CodedBaseRuntimeException;




@Service("withdrawAdminService")
public class WithdrawAdminService
  extends MyListService
{
  @Transactional
  public void save(Map<String, Object> req)
  {
    Map<String, Object> data = (Map)req.get("data");
    Integer id = Integer.valueOf((String)data.get("id"));
    String hql1 = "select status from PubWithdraw where id=" + id;
    String preStatus = (String)this.dao.getUnique(hql1, new Object[0]);
    if (!"1".equals(preStatus)) {
      throw new CodedBaseRuntimeException("申请处理已经完成,无法修改保存！");
    }
    String status = (String)data.get("status");
    Integer uid = Integer.valueOf(data.get("uid").toString());
    PubUser user = (PubUser)this.dao.get(PubUser.class, uid);
    Double fee = Double.valueOf(data.get("fee").toString());
    if ("2".equals(status))
    {








      if ((user.getMobile() != null) && (!"".equals(user.getMobile())))
        SmsUtil2.send(user.getMobile(), "您的提现申请已经处理成功,请注意查收!");
    }
    if ("9".equals(status)) {
      String hql = "update PubUser set chargeAmount=chargeAmount + :water , money=money+ :money where id = :id";
      Map<String, Object> param = new HashMap();
      param.put("water", fee);
      param.put("money", fee);
      param.put("id", uid);
      this.dao.executeUpdate(hql, param);
      if ((user.getMobile() != null) && (!"".equals(user.getMobile()))) {
        SmsUtil2.send(user.getMobile(), "您的提现申请没有通过审核,请尽快处理并重新提交申请!");
      }
    }
    super.save(req);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\WithdrawAdminService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */