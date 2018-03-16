package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubRecharge;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;










@Service
@Transactional(readOnly=true)
public class PubRechargeService
  extends BaseService
{
  @Transactional(rollbackFor={Throwable.class})
  public void addRechargeRecord(PubRecharge pubRecharge)
  {
    save(PubRecharge.class, pubRecharge);
  }
  
  public PubRecharge getRechargeRecordByTradeNo(String tradeNo) {
    return (PubRecharge)getUnique(PubRecharge.class, "tradeno", tradeNo);
  }
  


  @Transactional(rollbackFor={Throwable.class})
  public void setRechargeFinished(PubRecharge pubRecharge)
  {
    String sql1 = "update PubRecharge a set a.status = '2',realfee=:realfee,finishtime=:finishtime where tradeno=:tradeno and status='1'";
    int effected = this.dao.executeUpdate(sql1, ImmutableMap.of("realfee", pubRecharge.getRealfee(), "tradeno", pubRecharge.getTradeno(), "finishtime", new Date()));
    if (effected == 1) {
      String sql2 = "update PubUser a set a.money = COALESCE(a.money,0)+:money where id=:id";
      this.dao.executeUpdate(sql2, ImmutableMap.of("money", pubRecharge.getRealfee(), "id", pubRecharge.getUid()));
    }
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int setRechargeFailed(PubRecharge pubRecharge) {
    String sql = "update PubRecharge a set a.status = '3',finishtime=:finishtime where tradeno=:tradeno and status='1'";
    return this.dao.executeUpdate(sql, ImmutableMap.of("tradeno", pubRecharge.getTradeno(), "finishtime", new Date()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\PubRechargeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */