package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubExchangeLog;
import org.takeback.chat.entity.PubShop;
import org.takeback.chat.entity.PubUser;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;
import org.takeback.util.exception.CodedBaseRuntimeException;


@Service
public class ShopService
  extends BaseService
{
  @Transactional
  public List<PubShop> list(Integer pageNo, Integer pageSize)
  {
    return this.dao.findByHqlPaging("from PubShop order by sortNum desc ", pageSize.intValue(), pageNo.intValue());
  }
  
  @Transactional
  public PubShop get(Integer id) {
    return (PubShop)this.dao.get(PubShop.class, id);
  }
  
  @Transactional
  public PubExchangeLog getContactInfo(Integer id) {
    List<PubExchangeLog> list = this.dao.findByHql("from PubExchangeLog where uid =:uid order by id desc", ImmutableMap.of("uid", id));
    if (list.size() == 0) {
      return null;
    }
    return (PubExchangeLog)list.get(0);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void doExchage(Integer uid, Integer shopId, String name, String address, String mobile) {
    PubShop s = (PubShop)this.dao.get(PubShop.class, shopId);
    if (s == null) {
      throw new CodedBaseRuntimeException("商品信息丢失");
    }
    String hql = "update PubUser set money = coalesce(money,0) - :money where money > :money and id = :uid";
    int effected = this.dao.executeUpdate(hql, ImmutableMap.of("money", s.getMoney(), "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("账户金额不足!");
    }
    PubUser u = (PubUser)this.dao.get(PubUser.class, uid);
    PubExchangeLog pel = new PubExchangeLog();
    
    pel.setUid(uid);
    pel.setNickName(u.getUserId());
    pel.setExchangeTime(new Date());
    
    pel.setMobile(mobile);
    pel.setMoney(s.getMoney());
    pel.setName(name);
    pel.setAddress(address);
    
    pel.setShopId(shopId.toString());
    pel.setShopName(s.getName());
    pel.setStatus("0");
    this.dao.save(PubExchangeLog.class, pel);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\ShopService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */