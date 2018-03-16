package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.Message;
import org.takeback.chat.entity.PcConfig;
import org.takeback.chat.entity.PcGameLog;
import org.takeback.chat.entity.PcRateConfig;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.listeners.GameException;
import org.takeback.chat.store.pcegg.PcEggStore;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.dao.BaseDAO;
import org.takeback.util.exception.CodedBaseRuntimeException;

@org.springframework.stereotype.Service
public class PcEggService extends org.takeback.service.BaseService
{
  @Autowired
  RoomStore roomStore;
  @Autowired
  org.takeback.chat.store.user.UserStore userStore;
  public static final Integer[] red = { Integer.valueOf(3), Integer.valueOf(6), Integer.valueOf(9), Integer.valueOf(12), Integer.valueOf(15), Integer.valueOf(18), Integer.valueOf(21), Integer.valueOf(24) };
  public static final Integer[] green = { Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(7), Integer.valueOf(10), Integer.valueOf(16), Integer.valueOf(19), Integer.valueOf(22), Integer.valueOf(25) };
  public static final Integer[] blue = { Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(8), Integer.valueOf(11), Integer.valueOf(17), Integer.valueOf(20), Integer.valueOf(23), Integer.valueOf(26) };
  
  @Transactional(readOnly=true)
  public Map<String, PcRateConfig> getPcRateConfig() {
    Map<String, PcRateConfig> config = new HashMap();
    List<PcRateConfig> list = this.dao.findByHql("from PcRateConfig order by id , catalog ");
    for (PcRateConfig c : list) {
      config.put(c.getParam(), c);
    }
    
    return config;
  }
  
  @Transactional(readOnly=true)
  public Map<String, List<PcRateConfig>> getPcRateConfigs() {
    Map<String, List<PcRateConfig>> rates = new HashMap();
    List<PcRateConfig> list = this.dao.findByHql("from PcRateConfig order by id, catalog ");
    for (PcRateConfig config : list) {
      List<PcRateConfig> prc = (List)rates.get(config.getCatalog());
      if (prc == null) {
        prc = new ArrayList();
        rates.put(config.getCatalog(), prc);
      }
      prc.add(config);
    }
    return rates;
  }
  
  private List<Double> getValues(String text) {
    List<Double> res = new ArrayList();
    String pattern = "【[0-9]*\\.*[0-9]+】";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(text);
    while (m.find()) {
      res.add(Double.valueOf(m.group().replaceAll("[【】]", "")));
    }
    return res;
  }
  
  @Transactional
  public Map<String, Double> getLimitConfig() {
    Map<String, Double> config = new HashMap();
    List<PcConfig> list = this.dao.findByHql("from PcConfig where param like 'l_%'  order by id");
    for (PcConfig c : list) {
      config.put(c.getParam(), Double.valueOf(c.getVal().toString()));
    }
    return config;
  }
  
  @Transactional
  public Map<String, String> getAddtionalRateConfig() {
    Map<String, String> config = new HashMap();
    List<PcConfig> list = this.dao.findByHql("from PcConfig where param like 'r_%'  order by id");
    for (PcConfig c : list) {
      config.put(c.getParam(), c.getVal());
    }
    
    return config;
  }
  
  @Transactional
  public void bet(Integer num, String key, Double money, Integer uid, String roomId)
  {
    if (PcEggStore.getStore().isClosed(num.intValue())) {
      throw new CodedBaseRuntimeException(num + "期已停止下注!");
    }
    
    Map<String, Double> limitConf = getLimitConfig();
    Double min = (Double)limitConf.get("l_min");
    Double max = (Double)limitConf.get("l_max");
    if (money.doubleValue() < min.doubleValue()) {
      throw new CodedBaseRuntimeException("下注金额必须大于:" + min);
    }
    
    Double curMax = (Double)limitConf.get("l_max_cur");
    String curSumHql = "select coalesce(sum(freeze),0) from PcGameLog where num=:num";
    List<Double> curSum = this.dao.findByHql(curSumHql, ImmutableMap.of("num", num));
    if (((Double)curSum.get(0)).doubleValue() + money.doubleValue() > curMax.doubleValue()) {
      throw new CodedBaseRuntimeException("本期全站可下注金额:" + (curMax.doubleValue() - ((Double)curSum.get(0)).doubleValue()));
    }
    
    String sumSql = "select coalesce(sum(freeze),0) from PcGameLog where num=:num and uid = :uid";
    List<Double> sum = this.dao.findByHql(sumSql, ImmutableMap.of("num", num, "uid", uid));
    if (((Double)sum.get(0)).doubleValue() + money.doubleValue() > max.doubleValue()) {
      throw new CodedBaseRuntimeException("本期个人可下注金额:" + (max.doubleValue() - ((Double)sum.get(0)).doubleValue()));
    }
    
    Map<String, PcRateConfig> rates = getPcRateConfig();
    PcRateConfig rateConf = (PcRateConfig)rates.get(key);
    if (rateConf == null) {
      throw new CodedBaseRuntimeException("非法的下注值：" + key);
    }
    
    String betType = rateConf.getCatalog();
    

    String moneyHql = "update PubUser set money = COALESCE(money,0) - :money where id=:uid and money>:money";
    int effected = this.dao.executeUpdate(moneyHql, ImmutableMap.of("money", money, "uid", uid));
    if (effected == 0) {
      throw new CodedBaseRuntimeException("账户金额不足,请及时充值!");
    }
    

    PcGameLog pgl = new PcGameLog();
    pgl.setBet(key);
    pgl.setNum(num);
    pgl.setFreeze(money.doubleValue());
    pgl.setBetTime(new Date());
    pgl.setBetType(betType);
    pgl.setUid(uid);
    pgl.setStatus("0");
    pgl.setAddBack(0.0D);
    PubUser user = (PubUser)this.dao.get(PubUser.class, uid);
    pgl.setParentId(user.getParent());
    pgl.setUserId(user.getUserId());
    this.dao.save(PcGameLog.class, pgl);
    String text = ((PcRateConfig)rates.get(key)).getAlias();
    if ("num".equals(rateConf.getCatalog())) {
      text = "数字" + text;
    }
    




    Lottery lottery = org.takeback.chat.store.room.LotteryFactory.getDefaultBuilder(new java.math.BigDecimal(money.doubleValue()), Integer.valueOf(1)).setExpiredSeconds(Integer.valueOf(1)).setType("2").setTitle(text + " " + money + "金币").setSender(uid.intValue()).setDescription(num + "期").build();
    try {
      lottery.open(0);
    } catch (GameException e) {
      e.printStackTrace();
    }
    Message redMessage = new Message("RED", uid, lottery);
    User u = (User)this.userStore.get(uid);
    redMessage.setHeadImg(u.getHeadImg());
    redMessage.setNickName(u.getNickName());
    redMessage.setMsgTime(new Date());
    org.takeback.chat.store.room.Room room = (org.takeback.chat.store.room.Room)this.roomStore.get(roomId);
    org.takeback.chat.utils.MessageUtils.broadcast(room, redMessage);
  }
  
  @Transactional
  public Double cancelBet(Integer num, Integer uid) {
    if (PcEggStore.getStore().isClosed(num.intValue())) {
      throw new CodedBaseRuntimeException(num + "期已停止下注!");
    }
    Map<String, Object> pram = new HashMap();
    pram.put("num", num);
    pram.put("uid", uid);
    List<PcGameLog> list = this.dao.findByHql("from PcGameLog where status='0' and num =:num and uid =:uid", pram);
    if (list.size() == 0) {
      throw new CodedBaseRuntimeException("本期没有您的下注记录!");
    }
    Double money = Double.valueOf(0.0D);
    for (PcGameLog log : list) {
      money = Double.valueOf(money.doubleValue() + log.getFreeze());
      
      String logHql = "update PcGameLog set status = '3' where id = :id and status ='0' ";
      int effected = this.dao.executeUpdate(logHql, ImmutableMap.of("id", log.getId()));
      if (effected == 1) {
        String moneyHql = "update PubUser set money = COALESCE(money,0) + :money where id=:uid";
        this.dao.executeUpdate(moneyHql, ImmutableMap.of("money", Double.valueOf(log.getFreeze()), "uid", uid));
      }
    }
    
    return money;
  }
  
  @Transactional
  public List<PcGameLog> getGameLog(Integer num) {
    return this.dao.findByHql("from PcGameLog where num =:num and status =0", ImmutableMap.of("num", num));
  }
  
  @Transactional
  public void open(Integer num, String exp, String val) {
    Map<String, PcRateConfig> rates = getPcRateConfig();
    Map<String, String> additionalConfig = getAddtionalRateConfig();
    double[] steps = getSteps(((String)additionalConfig.get("r_depart")).toString());
    
    double[] steps2 = getSteps(((String)additionalConfig.get("r_depart2")).toString());
    

    Integer intVal = Integer.valueOf(val);
    List<PcGameLog> gameRecs = this.dao.findByHql("from PcGameLog where num =:num and status =0", ImmutableMap.of("num", num));
    List<String> otherLucky = getSpecialLucky(val, exp);
    for (PcGameLog l : gameRecs) {
      Double rate = Double.valueOf(0.0D);
      String betType = l.getBetType();
      l.setLuckyNumber(exp);
      
      String hql = "select coalesce(sum(freeze),0) from PcGameLog where num = :num and  uid = :uid";
      List<Double> res = this.dao.findByHql(hql, ImmutableMap.of("num", num, "uid", l.getUid()));
      Double sum = (Double)res.get(0);
      l.setOpenTime(new Date());
      if ("num".equals(betType)) {
        if (intVal.toString().equals(l.getBet())) {
          rate = Double.valueOf(((PcRateConfig)rates.get(intVal.toString())).getVal());
        }
      } else {
        for (String key : otherLucky) {
          if (key.equals(l.getBet())) {
            rate = Double.valueOf(((PcRateConfig)rates.get(key)).getVal());
            if (intVal.intValue() == 14) {
              if (("da".equals(key)) || ("sh".equals(key))) {
                rate = Double.valueOf(getAddtionalRate(steps, "r_" + key, sum.doubleValue()));
              } else if ("ds".equals(key)) {
                rate = Double.valueOf(getAddtionalRate(steps2, "r_" + key, sum.doubleValue()));
              }
            } else if (intVal.intValue() == 13) {
              if (("xo".equals(key)) || ("dn".equals(key))) {
                rate = Double.valueOf(getAddtionalRate(steps, "r_" + key, sum.doubleValue()));
              } else if ("xd".equals(key)) {
                rate = Double.valueOf(getAddtionalRate(steps2, "r_" + key, sum.doubleValue()));
              }
            }
          }
        }
      }
      
      if (rate.doubleValue() > 0.0D) {
        Double betMoney = Double.valueOf(l.getFreeze());
        Double bonus = Double.valueOf(betMoney.doubleValue() * rate.doubleValue());
        l.setAddBack(bonus.doubleValue());
        l.setBackMoney(0.0D);
        l.setBonus(bonus.doubleValue());
        l.setUserInout(bonus.doubleValue() - betMoney.doubleValue());
        l.setStatus("1");
        this.dao.executeUpdate("update PubUser set money = money +:bonus where id =:uid", ImmutableMap.of("bonus", bonus, "uid", l.getUid()));
      } else {
        Integer uid = l.getUid();
        l.setBonus(0.0D);
        l.setAddBack(0.0D);
        l.setUserInout(-l.getFreeze());
        l.setStatus("2");
      }
      this.dao.save(PcGameLog.class, l);
    }
  }
  
  double getAddtionalRate(double[] steps, String key, double money) {
    for (int i = 0; 
        i < steps.length; i++) {
      if (steps[i] >= money) {
        break;
      }
    }
    
    double[] steps2 = getSteps(((String)getAddtionalRateConfig().get(key)).toString());
    return steps2[i];
  }
  
  public static double[] getSteps(String conf)
  {
    String[] steps = conf.split(",");
    double[] intSteps = new double[steps.length];
    for (int i = 0; i < steps.length; i++) {
      intSteps[i] = Double.valueOf(steps[i]).doubleValue();
    }
    return intSteps;
  }
  

  public List<String> getSpecialLucky(String val, String exp)
  {
    List res = new ArrayList();
    Integer intVal = Integer.valueOf(val);
    if (intVal.intValue() % 2 == 0) {
      res.add("sh");
      if (intVal.intValue() >= 14) {
        res.add("ds");
      } else if (intVal.intValue() < 13) {
        res.add("xs");
      }
    } else {
      res.add("dn");
      if (intVal.intValue() > 14) {
        res.add("dd");
      } else if (intVal.intValue() <= 13) {
        res.add("xd");
      }
    }
    
    if (intVal.intValue() >= 14) {
      res.add("da");
    } else if (intVal.intValue() <= 13) {
      res.add("xo");
    }
    
    if (intVal.intValue() >= 22) {
      res.add("jd");
    }
    if (intVal.intValue() <= 5) {
      res.add("jx");
    }
    
    Integer[] arrayOfInteger = red;int i = arrayOfInteger.length; for (int j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
      if (i == intVal.intValue()) {
        res.add("rd");
      }
    }
    
    arrayOfInteger = green;i = arrayOfInteger.length; for (j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
      if (i == intVal.intValue()) {
        res.add("grn");
      }
    }
    
    arrayOfInteger = blue;i = arrayOfInteger.length; for (j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
      if (i == intVal.intValue()) {
        res.add("bl");
      }
    }
    
    if ("8+8+8".equals(exp)) {
      res.add("bz");
    }
    return res;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\PcEggService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */