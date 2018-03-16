package org.takeback.chat.schedule;

import com.google.common.collect.ImmutableMap;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.GcRoomMoney;
import org.takeback.chat.entity.PcBackRecord;
import org.takeback.chat.entity.PcConfig;
import org.takeback.chat.entity.PcRateConfig;
import org.takeback.chat.entity.ProxyVote;
import org.takeback.chat.entity.PubConfig;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.UserService;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.dao.BaseDAO;

@org.springframework.stereotype.Service
public class ProxySchedule
{
  @org.springframework.beans.factory.annotation.Autowired
  BaseDAO dao;
  
  @Transactional
  public void work()
  {
    List<GcRoomMoney> ml = this.dao.findByHql("from GcRoomMoney");
    for (GcRoomMoney grm : ml) {
      if (grm.getRestMoney().doubleValue() >= UserService.ROOM_FEE.doubleValue()) {
        grm.setRestMoney(Double.valueOf(grm.getRestMoney().doubleValue() - UserService.ROOM_FEE.doubleValue()));
      }
      else {
        if (grm.getRestMoney().doubleValue() > 0.0D) {
          GcRoom rm = (GcRoom)this.dao.get(GcRoom.class, grm.getRoomId());
          Integer uid = rm.getOwner();
          this.dao.executeUpdate("update PubUser set money = money +:money  where id =:uid", ImmutableMap.of("money", grm.getRestMoney(), "uid", uid));
        }
        this.dao.executeUpdate("delete from GcRoom where id=:roomId", ImmutableMap.of("roomId", grm.getRoomId()));
        this.dao.executeUpdate("delete from GcRoomProperty where roomId=:roomId", ImmutableMap.of("roomId", grm.getRoomId()));
        this.dao.executeUpdate("delete from GcRoomMember where roomId=:roomId", ImmutableMap.of("roomId", grm.getRoomId()));
        this.dao.executeUpdate("delete from GcRoomMoney where roomId=:roomId", ImmutableMap.of("roomId", grm.getRoomId()));
      }
      
      if (grm.getRestMoney().doubleValue() <= UserService.ROOM_FEE.doubleValue() * 4.0D) {
        Integer uid = ((GcRoom)this.dao.get(GcRoom.class, grm.getRoomId())).getOwner();
        PubUser u = (PubUser)this.dao.get(PubUser.class, uid);
        if (u.getMobile() != null) {
          org.takeback.chat.utils.SmsUtil2.send(u.getMobile(), "您的房间余额:" + grm.getRestMoney() + ",当低于50时房间将被删除,请及时充值!");
        }
      }
    }
    
    Object waterConfigs = getWaterConfig();
    SystemConfigService.getInstance().getValue("b_rate");
    Date startDate = getStartDate();
    Date endDate = getEndDate();
    String hql = "from PubUser where  userType<>'9' ";
    List<PubUser> users = this.dao.findByHql(hql);
    for (PubUser u : users)
    {
      if (u.getId().intValue() == 156) {
        int i = 0;
      }
      
      if (u.getParent() != null) {
        Object winSum = this.dao.findByHql("select coalesce(sum(inoutNum),0) from GcLotteryDetail where uid=:uid and createDate>:startDate and createDate<:endDate and inoutNum>0", 
          ImmutableMap.of("uid", u.getId(), "startDate", startDate, "endDate", endDate));
        Double sum1 = (Double)((List)winSum).get(0);
        List<Double> loseSum = this.dao.findByHql("select coalesce(sum(inoutNum),0) from GcLotteryDetail where uid=:uid and createDate>:startDate and createDate<:endDate and inoutNum<0", 
          ImmutableMap.of("uid", u.getId(), "startDate", startDate, "endDate", endDate));
        Double sum2 = (Double)loseSum.get(0);
        Double sum = Double.valueOf(sum1.doubleValue() + Math.abs(sum2.doubleValue()));
        Double handRate = Double.valueOf(SystemConfigService.getInstance().getValue("conf_invit_rate"));
        if (handRate.doubleValue() > 0.0D)
        {
          System.out.println(u.getId() + "->" + u.getParent() + ":" + sum + ">>" + sum.doubleValue() * handRate.doubleValue());
          
          this.dao.executeUpdate("update PubUser set money = money +:water where id =:uid", ImmutableMap.of("water", Double.valueOf(sum.doubleValue() * handRate.doubleValue()), "uid", u.getParent()));
        }
      }
      







      PubConfig pc = (PubConfig)this.dao.getUnique(PubConfig.class, "param", "water");
      Double rate = Double.valueOf(pc.getVal());
      if ((rate.doubleValue() > 0.0D) && (rate.doubleValue() < 1.0D))
      {


        String pcCountHql = "select coalesce(sum(userInout),0) from PcGameLog where uid=:uid and openTime>:startDate and openTime<:endDate";
        List<Object> pcCounts = this.dao.findByHql(pcCountHql, ImmutableMap.of("uid", u.getId(), "startDate", startDate, "endDate", endDate));
        Double pcWater = Double.valueOf(pcCounts.get(0).toString());
        Double waterMin; if (pcWater.doubleValue() < 0.0D) {
          Long count = Long.valueOf(this.dao.count("select count(*) from PcGameLog where uid =:uid and status <>0", ImmutableMap.of("uid", u.getId())));
          waterMin = (Double)((List)((Map)waterConfigs).get("w_min")).get(0);
          if (count.longValue() >= waterMin.doubleValue()) {
            Double water = getWater((Map)waterConfigs, pcWater);
            if (water.doubleValue() > 0.0D) {
              PcBackRecord backRecord = new PcBackRecord();
              backRecord.setUid(u.getId());
              backRecord.setMoney(water.doubleValue());
              backRecord.setUserInout(pcWater.doubleValue());
              backRecord.setBackDate(new Date());
              backRecord.setUserId(u.getUserId());
              this.dao.save(PcBackRecord.class, backRecord);
              this.dao.executeUpdate("update PubUser set money = money +:water where id =:uid", ImmutableMap.of("water", water, "uid", u.getId()));
            }
          }
        }
        
        if ("2".equals(u.getUserType())) {
          String proxyHql = "select coalesce(sum(freeze),0) from PcGameLog where parentId=:uid and (status='1' or status='2') and  betTime>:startDate and betTime<:endDate";
          if (u.getId().intValue() == 156) {
            waterMin = 1;
          }
          List<Object> proxySum = this.dao.findByHql(proxyHql, ImmutableMap.of("uid", u.getId(), "startDate", startDate, "endDate", endDate));
          Double teamWater = Double.valueOf(proxySum.get(0).toString());
          if (teamWater.doubleValue() > 0.0D) {
            double proxyRate = getProxyBack(teamWater.doubleValue());
            double backNum = teamWater.doubleValue() * proxyRate;
            ProxyVote v = new ProxyVote();
            v.setCacuDate(endDate);
            v.setUserId(u.getUserId());
            v.setUid(u.getId());
            v.setVote(Double.valueOf(backNum));
            v.setTotal(teamWater);
            String addHql = "update PubUser set money=coalesce(money,0) + :vote where id =:uid";
            this.dao.executeUpdate(addHql, ImmutableMap.of("vote", Double.valueOf(backNum), "uid", u.getId()));
            this.dao.save(ProxyVote.class, v);
          }
        }
      }
    }
  }
  
  double getProxyBack(double water) { double[] steps = org.takeback.chat.service.PcEggService.getSteps(SystemConfigService.getInstance().getValue("b_depart"));
    for (int i = 0; 
        i < steps.length; i++) {
      if (steps[i] >= water) {
        break;
      }
    }
    double[] steps2 = org.takeback.chat.service.PcEggService.getSteps(SystemConfigService.getInstance().getValue("b_rate"));
    return steps2[i];
  }
  


  private Double getWater(Map<String, List<Double>> confs, Double money)
  {
    if (money.doubleValue() >= 0.0D) {
      return Double.valueOf(0.0D);
    }
    money = Double.valueOf(Math.abs(money.doubleValue()));
    Double water = Double.valueOf(0.0D);
    for (int i = 1; i <= confs.size(); i++) {
      String key = "w_" + i;
      List<Double> l = (List)confs.get(key);
      if (l == null) {
        return water;
      }
      if (money.doubleValue() < ((Double)l.get(0)).doubleValue()) break;
      water = Double.valueOf(money.doubleValue() * ((Double)l.get(1)).doubleValue() / 100.0D);
    }
    


    return water;
  }
  
  @Transactional
  public Map<String, List<Double>> getWaterConfig()
  {
    Map<String, List<Double>> config = new java.util.HashMap();
    List<PcConfig> list = this.dao.findByHql("from PcConfig where param like 'w_%' order by id");
    for (PcConfig c : list) {
      config.put(c.getParam(), getValues(c.getVal()));
    }
    
    return config;
  }
  
  private List<Double> getValues(String text)
  {
    List<Double> res = new java.util.ArrayList();
    String pattern = "【[0-9]+】";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(text);
    while (m.find()) {
      res.add(Double.valueOf(m.group().replaceAll("[【】]", "")));
    }
    return res;
  }
  
  @Transactional(readOnly=true)
  public Map<String, List<PcRateConfig>> getPcRateConfigs() {
    Map<String, List<PcRateConfig>> rates = new java.util.HashMap();
    List<PcRateConfig> list = this.dao.findByHql("from PcRateConfig order by id, catalog ");
    for (PcRateConfig config : list) {
      List<PcRateConfig> prc = (List)rates.get(config.getCatalog());
      if (prc == null) {
        prc = new java.util.ArrayList();
        rates.put(config.getCatalog(), prc);
      }
      prc.add(config);
    }
    return rates;
  }
  
  private Date getStartDate() {
    Date d = org.takeback.chat.utils.DateUtil.getStartOfToday();
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(11, -24);
    return c.getTime();
  }
  
  private Date getEndDate() {
    Date d = org.takeback.chat.utils.DateUtil.getStartOfToday();
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    
    return c.getTime();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\schedule\ProxySchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */