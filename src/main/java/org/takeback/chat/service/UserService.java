package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.LoginLog;
import org.takeback.chat.entity.PubBank;
import org.takeback.chat.entity.PubExchangeLog;
import org.takeback.chat.entity.PubRecharge;
import org.takeback.chat.entity.PubRoomApply;
import org.takeback.chat.entity.PubShop;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.entity.PubWithdraw;
import org.takeback.chat.entity.TransferLog;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.exception.CodedBaseRuntimeException;

@org.springframework.stereotype.Service
public class UserService extends org.takeback.service.BaseService
{
  public static Double ROOM_FEE = Double.valueOf(50.0D);
  
  @Transactional(rollbackFor={Throwable.class})
  public void createRoom(int uid) {
    Double price = Double.valueOf(SystemConfigService.getInstance().getValue("conf_room_money").toString());
    if (price.doubleValue() < 0.0D) {
      throw new CodedBaseRuntimeException("购买配置出错!");
    }
    
    String hql = "update PubUser set money=money - :price where money>:price and  id=:uid";
    if (0 == this.dao.executeUpdate(hql, ImmutableMap.of("price", price, "uid", Integer.valueOf(uid)))) {
      throw new CodedBaseRuntimeException("金币不足,无法创建房间!");
    }
    


    GcRoom rm = new GcRoom();
    rm.setCatalog("");
    rm.setCreatedate(new Date());
    rm.setFeeAdd(Double.valueOf(0.05D));
    rm.setHot(Integer.valueOf(1));
    rm.setLimitNum(Integer.valueOf(5000));
    rm.setName("牛牛房间");
    rm.setOwner(Integer.valueOf(uid));
    rm.setPsw("");
    rm.setStatus("0");
    rm.setSumFee(Double.valueOf(0.0D));
    rm.setType("G022");
    String roomId = System.currentTimeMillis() + "";
    rm.setId(roomId);
    this.dao.save(GcRoom.class, rm);
    
    org.takeback.chat.entity.GcRoomMoney gcRoomMoney = new org.takeback.chat.entity.GcRoomMoney();
    gcRoomMoney.setTotalMoney(price);
    gcRoomMoney.setRestMoney(Double.valueOf(price.doubleValue() - ROOM_FEE.doubleValue()));
    gcRoomMoney.setRoomId(roomId);
    this.dao.save(org.takeback.chat.entity.GcRoomMoney.class, gcRoomMoney);
    
    List<org.takeback.chat.entity.GcRoomProperty> defaults = org.takeback.chat.utils.RoomTemplate.get("G022");
    if (defaults != null) {
      for (org.takeback.chat.entity.GcRoomProperty prop : defaults) {
        prop.setRoomId(rm.getId());
        org.takeback.chat.entity.GcRoomProperty n = new org.takeback.chat.entity.GcRoomProperty();
        org.takeback.util.BeanUtils.copy(prop, n);
        this.dao.save(org.takeback.chat.entity.GcRoomProperty.class, n);
        this.dao.getSession().flush();
      }
    }
  }
  

  @Transactional(rollbackFor={Throwable.class})
  public PubUser updateUser(int uid, Map<String, Object> data)
  {
    PubUser pubUser = (PubUser)this.dao.get(PubUser.class, Integer.valueOf(uid));
    if (pubUser != null) {
      Map<String, Object> params = new java.util.HashMap();
      for (Map.Entry<String, Object> en : data.entrySet()) {
        if ((((String)en.getKey()).equals("nickName")) || (((String)en.getKey()).equals("mobile")) || (((String)en.getKey()).equals("headImg")) || 
          (((String)en.getKey()).equals("pwd")) || (((String)en.getKey()).equals("accessToken")) || (((String)en.getKey()).equals("tokenExpireTime"))) {
          params.put(en.getKey(), en.getValue());
        }
      }
      org.takeback.util.BeanUtils.copy(params, pubUser);
      this.dao.update(PubUser.class, pubUser);
    }
    return null;
  }
  






  @Transactional(rollbackFor={Throwable.class})
  public void transfer(Integer uid, Integer account, double money)
  {
    if (!"1".equals(SystemConfigService.getInstance().getValue("conf_transfer"))) {
      throw new CodedBaseRuntimeException("系统转账功能已关闭!");
    }
    PubUser target = (PubUser)this.dao.get(PubUser.class, account);
    if (target == null) {
      throw new CodedBaseRuntimeException("目标账号不存在!");
    }
    if (target.getId().equals(uid)) {
      throw new CodedBaseRuntimeException("不允许给自己转账!");
    }
    
    int effected = this.dao.executeUpdate("update PubUser set money=money -:money where money >:money and  id=:uid", ImmutableMap.of("money", Double.valueOf(money + 0.0D), "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("金额不足!");
    }
    this.dao.executeUpdate("update PubUser set money=money +:money where id=:uid", ImmutableMap.of("money", Double.valueOf(money + 0.0D), "uid", target.getId()));
    PubUser fromUser = (PubUser)this.dao.get(PubUser.class, uid);
    TransferLog tl = new TransferLog();
    tl.setFromUid(uid);
    tl.setFromNickName(fromUser.getUserId());
    tl.setToUid(target.getId());
    tl.setToNickName(target.getUserId());
    tl.setMoney(Double.valueOf(money));
    tl.setTransferDate(new Date());
    this.dao.save(TransferLog.class, tl);
  }
  






  @Transactional(rollbackFor={Throwable.class})
  public void prixyRecharge(Integer uid, Integer account, Integer money)
  {
    if (!"1".equals(SystemConfigService.getInstance().getValue("conf_proxyRecharge"))) {
      throw new CodedBaseRuntimeException("功能已关闭!");
    }
    
    PubUser target = (PubUser)this.dao.get(PubUser.class, account);
    if (target == null) {
      throw new CodedBaseRuntimeException("目标账号不存在!");
    }
    if (target.getId().equals(uid)) {
      throw new CodedBaseRuntimeException("不允许给自己充值!");
    }
    if (!uid.equals(target.getParent())) {
      throw new CodedBaseRuntimeException("只能给直接下线上分!");
    }
    if (money.intValue() <= 0) {
      throw new CodedBaseRuntimeException("请输入大于0的金额!");
    }
    

    int effected = this.dao.executeUpdate("update PubUser set money=money -:money where money >:money and  id=:uid", ImmutableMap.of("money", Double.valueOf(money.intValue() + 0.0D), "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("金额不足!");
    }
    this.dao.executeUpdate("update PubUser set money=money +:money where  id=:uid", ImmutableMap.of("money", Double.valueOf(money.intValue() + 0.0D), "uid", target.getId()));
    
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt("上分");
    pubRecharge.setFee(Double.valueOf(money.intValue() + 0.0D));
    pubRecharge.setGoodsname("上分");
    pubRecharge.setTradeno(org.takeback.util.identity.UUIDGenerator.get());
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("2");
    pubRecharge.setUid(account);
    pubRecharge.setUserIdText(target.getUserId());
    pubRecharge.setOperator(uid);
    this.dao.save(PubRecharge.class, pubRecharge);
  }
  





  @Transactional(rollbackFor={Throwable.class})
  public void prixyUnRecharge(Integer uid, Integer account, Integer money)
  {
    if (!"1".equals(SystemConfigService.getInstance().getValue("conf_proxyWithdraw"))) {
      throw new CodedBaseRuntimeException("功能已关闭!");
    }
    PubUser target = (PubUser)this.dao.get(PubUser.class, account);
    if (target == null) {
      throw new CodedBaseRuntimeException("目标账号不存在!");
    }
    if (target.getId().equals(uid)) {
      throw new CodedBaseRuntimeException("不允许给自己充值!");
    }
    if (!uid.equals(target.getParent())) {
      throw new CodedBaseRuntimeException("只能给直接下线下分!");
    }
    if (money.intValue() <= 0) {
      throw new CodedBaseRuntimeException("请输入大于0的金额!");
    }
    
    int effected = this.dao.executeUpdate("update PubUser set money=money -:money where  id=:uid  and  money >:money", ImmutableMap.of("money", Double.valueOf(money.intValue() + 0.0D), "uid", target.getId()));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("金额不足!");
    }
    this.dao.executeUpdate("update PubUser set money=money+:money where  id=:uid", ImmutableMap.of("money", Double.valueOf(money.intValue() + 0.0D), "uid", uid));
    
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt("下分");
    pubRecharge.setFee(Double.valueOf(money.intValue() + 0.0D));
    pubRecharge.setGoodsname("下分");
    pubRecharge.setTradeno(org.takeback.util.identity.UUIDGenerator.get());
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("3");
    pubRecharge.setUid(account);
    pubRecharge.setUserIdText(target.getUserId());
    pubRecharge.setOperator(uid);
    this.dao.save(PubRecharge.class, pubRecharge);
  }
  


  @Transactional(rollbackFor={Throwable.class})
  public void exchange(Integer uid, Integer goodId, String name, String address, String mobile)
  {
    PubUser u = (PubUser)this.dao.get(PubUser.class, uid);
    PubShop s = (PubShop)this.dao.get(PubShop.class, goodId);
    if (s.getStorage().intValue() < 1) {
      throw new CodedBaseRuntimeException("库存商品!");
    }
    int effected = this.dao.executeUpdate("update PubUser set money = coalesce(money,0) - :money where money>:money  and uid = :uid", ImmutableMap.of("money", s.getMoney(), "uid", uid));
    if (effected < 1) {
      throw new CodedBaseRuntimeException("账户金币不足!");
    }
    
    PubExchangeLog pel = new PubExchangeLog();
    pel.setStatus("0");
    pel.setAddress(address);
    pel.setExchangeTime(new Date());
    pel.setMobile(mobile);
    pel.setMoney(s.getMoney());
    pel.setName(name);
    pel.setShopId(goodId.toString());
    pel.setShopName(s.getName());
    pel.setUid(uid);
    this.dao.save(PubExchangeLog.class, pel);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void bindMobile(int uid, String mobile) {
    this.dao.executeUpdate("update PubUser set mobile=:mobile where id=:uid", ImmutableMap.of("mobile", mobile, "uid", Integer.valueOf(uid)));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void updatePwd(int uid, String pwd) {
    this.dao.executeUpdate("update PubUser set pwd=:pwd where id=:uid", ImmutableMap.of("pwd", pwd, "uid", Integer.valueOf(uid)));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void updateHeadImg(int uid, String headImg) { this.dao.executeUpdate("update PubUser set headImg=:headImg where id=:uid", ImmutableMap.of("headImg", headImg, "uid", Integer.valueOf(uid))); }
  

  @Transactional(readOnly=true)
  public PubUser login(String username, String password)
  {
    return get(username, password);
  }
  
  @Transactional
  public void setLoginInfo(String ip, Integer uid) {
    this.dao.executeUpdate("update PubUser set lastLoginDate=:loginDate,lastLoginIp = :lastLoginIp where id=:uid", ImmutableMap.of("loginDate", new Date(), "lastLoginIp", ip, "uid", uid));
  }
  
  @Transactional(readOnly=true)
  public PubUser get(String username, String password) {
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "userId", username);
    if (user == null) {
      return null;
    }
    if (org.takeback.util.encrypt.CryptoUtils.verify(user.getPwd(), password, org.apache.commons.lang3.StringUtils.reverse(user.getSalt()))) {
      return user;
    }
    return null;
  }
  
  @Transactional(readOnly=true)
  public PubUser get(int uid, String password) {
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "id", Integer.valueOf(uid));
    if (user == null) {
      return null;
    }
    if (org.takeback.util.encrypt.CryptoUtils.verify(user.getPwd(), password, org.apache.commons.lang3.StringUtils.reverse(user.getSalt()))) {
      return user;
    }
    return null;
  }
  
  @Transactional(readOnly=true)
  public double getBalance(int uid) {
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "id", Integer.valueOf(uid));
    if (user == null) {
      return 0.0D;
    }
    return user.getMoney().doubleValue();
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public PubUser register(String username, String password, String mobile, String wx, String alipay, Integer parent, String ip) {
    PubUser user = (PubUser)this.dao.getUnique(PubUser.class, "userId", username);
    if (user != null) {
      throw new CodedBaseRuntimeException("用户名已存在!");
    }
    user = new PubUser();
    String salt = org.takeback.util.encrypt.CryptoUtils.getSalt();
    user.setUserId(username);
    user.setNickName(username);
    user.setSalt(salt);
    user.setWx(wx);
    user.setUserType("1");
    user.setMobile(mobile);
    user.setLastLoginDate(new Date());
    user.setLastLoginIp(ip);
    user.setAlipay(alipay);
    user.setPwd(org.takeback.util.encrypt.CryptoUtils.getHash(password, org.apache.commons.lang3.StringUtils.reverse(salt)));
    user.setMoneyCode(user.getPwd());
    user.setMoney(Double.valueOf(0.0D));
    Object conf = SystemConfigService.getInstance().getValue("conf_init_money");
    if (conf != null) {
      try {
        user.setMoney(Double.valueOf(conf.toString()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    user.setRegistDate(new Date());
    user.setUserType("1");
    user.setRegistIp(ip);
    if (parent != null) {
      PubUser p = (PubUser)this.dao.get(PubUser.class, parent);
      if (p != null) {
        user.setParent(parent);
      }
    }
    this.dao.save(PubUser.class, user);
    

    LoginLog l = new LoginLog();
    l.setLoginTime(new Date());
    l.setIp(ip);
    l.setUserId(user.getId());
    l.setUserName(user.getUserId());
    






    this.dao.save(LoginLog.class, l);
    return user;
  }
  
  @Transactional(readOnly=true)
  public PubUser getByWxOpenId(String openId) {
    return (PubUser)this.dao.getUnique(PubUser.class, "wxOpenId", openId);
  }
  






  @Transactional(rollbackFor={Throwable.class})
  public void roomApply(String name, String mobile, int uid)
  {
    PubRoomApply r = new PubRoomApply();
    r.setName(name);
    r.setCreateTime(new Date());
    r.setMobile(mobile);
    r.setUid(Integer.valueOf(uid));
    PubUser user = (PubUser)this.dao.get(PubUser.class, Integer.valueOf(uid));
    r.setUserIdText(user.getUserId());
    this.dao.save(PubRoomApply.class, r);
  }
  






  @Transactional(rollbackFor={Throwable.class})
  public void proxyApply(Integer uid, Map<String, Object> conf)
  {
    PubUser u = (PubUser)this.dao.get(PubUser.class, uid);
    if ("2".equals(u.getUserType())) {
      throw new CodedBaseRuntimeException("你已经是代理,无需申请!");
    }
    

    Double limit = Double.valueOf(conf.get("money").toString());
    int effected = this.dao.executeUpdate("update PubUser set money=coalesce(money,0)-:money where money>=:money and  id =:uid ", ImmutableMap.of("money", limit, "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("账户金币不足,申请失败!");
    }
    this.dao.executeUpdate("update PubUser set userType = '2' where id =:uid ", ImmutableMap.of("uid", uid));
  }
  






  @Transactional(rollbackFor={Throwable.class})
  public void withdraw(Map<String, Object> data, int uid)
  {
    double money = Double.valueOf(data.get("money").toString()).doubleValue();
    if (money < 100.0D) {
      throw new CodedBaseRuntimeException("最低提现金额100");
    }
    

    List<PubWithdraw> wl = this.dao.findByHql("from PubWithdraw where uid=:uid and tradetime>:startDate and tradetime<=:endDate", 
      ImmutableMap.of("uid", Integer.valueOf(uid), "startDate", org.takeback.chat.utils.DateUtil.getStartOfToday(), "endDate", org.takeback.chat.utils.DateUtil.getEndOfToday()));
    if (wl.size() >= 3) {
      throw new CodedBaseRuntimeException("每天提现次数已达上限:" + wl.size() + "次");
    }
    

    String bankName = data.get("bankName").toString();
    if (!org.takeback.util.valid.ValidateUtil.instance().isChinese(bankName)) {
      throw new CodedBaseRuntimeException("提款银行必须是中文");
    }
    
    String account = data.get("account").toString();
    
    String branch = data.get("branch").toString();
    if ((branch != null) && (!"".equals(branch)) && (!org.takeback.util.valid.ValidateUtil.instance().isChinese(bankName))) {
      throw new CodedBaseRuntimeException("提款银行分支必须是中文");
    }
    
    String ownerName = data.get("ownerName").toString();
    if (!org.takeback.util.valid.ValidateUtil.instance().isChinese(bankName)) {
      throw new CodedBaseRuntimeException("提款姓名必须是中文");
    }
    String mobile = data.get("mobile").toString();
    
    String hql = "update PubUser set money = money - :money where id=:id and money > :money";
    int effect = this.dao.executeUpdate(hql, ImmutableMap.of("money", Double.valueOf(money), "id", Integer.valueOf(uid)));
    if (effect == 0) {
      throw new CodedBaseRuntimeException("金额不足");
    }
    
    PubWithdraw pw = new PubWithdraw();
    pw.setAccount(account);
    pw.setBankName(bankName);
    pw.setBranch(branch);
    pw.setFee(Double.valueOf(money));
    pw.setMobile(mobile);
    pw.setOwnerName(ownerName);
    pw.setUid(Integer.valueOf(uid));
    PubUser user = (PubUser)this.dao.get(PubUser.class, Integer.valueOf(uid));
    pw.setUserIdText(user.getUserId());
    pw.setStatus("1");
    pw.setTradetime(new Date());
    this.dao.save(PubWithdraw.class, pw);
    
    String hql2 = "from PubBank where userId=:userId and account =:account";
    List<PubBank> bankList = this.dao.findByHql(hql2, ImmutableMap.of("userId", Integer.valueOf(uid), "account", account));
    PubBank pb;
    if (bankList.size() == 0) {
      PubBank pb = new PubBank();
      pb.setCreateTime(new Date());
      pb.setUserId(Integer.valueOf(uid));
    } else {
      pb = (PubBank)bankList.get(0);
    }
    pb.setMobile(mobile);
    pb.setUserIdText(user.getUserId());
    pb.setBranch(branch);
    pb.setAccount(account);
    pb.setBankName(bankName);
    pb.setName(ownerName);
    this.dao.saveOrUpdate(PubBank.class, pb);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\UserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */