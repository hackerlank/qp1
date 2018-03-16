package org.takeback.chat.service;

import com.google.common.collect.ImmutableMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PK10;
import org.takeback.chat.utils.DateUtil;
import org.takeback.dao.BaseDAO;
import org.takeback.service.BaseService;

@Service
public class PK10Service
  extends BaseService
{
  @Transactional
  public Map<String, List<List<String>>> getData(String date)
  {
    Date d = null;
    if (date != null) {
      SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
      try {
        d = format.parse(date);
      } catch (ParseException e) {
        d = new Date();
      }
    }
    if (d == null) {
      d = new Date();
    }
    Date start = DateUtil.getStartOfTheDay(d);
    Date end = DateUtil.getEndOfTheDay(d);
    List<PK10> list = this.dao.findByHql("from PK10 where openTime>:start and openTime <:end order by number asc", ImmutableMap.of("start", start, "end", end));
    
    List<List<String>> bs1 = new ArrayList();
    
    List<List<String>> ds1 = new ArrayList();
    
    List<List<String>> lh1 = new ArrayList();
    List<List<String>> he = new ArrayList();
    
    List<String> dsData;
    for (int i = 0; i < 10; i++) {
      List<String> bsData = new ArrayList();
      dsData = new ArrayList();
      for (PK10 pk : list) {
        String lucky = pk.getLucky().split(",")[i];
        Integer num = Integer.valueOf(Integer.parseInt(lucky));
        if (num.intValue() > 5) {
          bsData.add("大[" + pk.getNumber() + ":" + lucky + "]");
        } else {
          bsData.add("小[" + pk.getNumber() + ":" + lucky + "]");
        }
        
        if (num.intValue() % 2 == 0) {
          dsData.add("双[" + pk.getNumber() + ":" + lucky + "]");
        } else {
          dsData.add("单[" + pk.getNumber() + ":" + lucky + "]");
        }
      }
      bs1.add(bsData);
      ds1.add(dsData);
    }
    
    for (int i = 0; i < 5; i++) {
      List<String> lhData = new ArrayList();
      for (PK10 pk : list) {
        String lucky1 = pk.getLucky().split(",")[i];
        Integer num1 = Integer.valueOf(Integer.parseInt(lucky1));
        
        String lucky2 = pk.getLucky().split(",")[(9 - i)];
        Integer num2 = Integer.valueOf(Integer.parseInt(lucky2));
        
        if (num1.intValue() > num2.intValue()) {
          lhData.add("龙");
        } else {
          lhData.add("虎");
        }
      }
      lh1.add(lhData);
    }
    
    List<String> dshe = new ArrayList();
    List<String> dxhe = new ArrayList();
    
    for (PK10 pk : list) {
      String lucky1 = pk.getLucky().split(",")[0];
      String lucky2 = pk.getLucky().split(",")[1];
      Integer num1 = Integer.valueOf(Integer.parseInt(lucky1));
      Integer num2 = Integer.valueOf(Integer.parseInt(lucky2));
      if ((num1.intValue() + num2.intValue()) % 2 == 0) {
        dshe.add("双");
      } else {
        dshe.add("单");
      }
      if (num1.intValue() + num2.intValue() >= 11) {
        dxhe.add("大");
      } else {
        dxhe.add("小");
      }
    }
    he.add(dshe);
    he.add(dxhe);
    
    Map<String, List<List<String>>> data = new HashMap();
    data.put("daxiao", bs1);
    data.put("danshuang", ds1);
    data.put("longhu", lh1);
    data.put("he", he);
    

    Object newInfo = new ArrayList();
    
    List<PK10> latest = this.dao.findByHqlPaging("from PK10 order by id desc ", 1, 1);
    if ((latest != null) && (latest.size() != 0)) {
      PK10 newOne = (PK10)latest.get(0);
      List<String> newData = new ArrayList();
      newData.add(newOne.getNumber());
      newData.add(newOne.getLucky());
      newData.add(newOne.getOpenTime().toString());
      ((List)newInfo).add(newData);
    }
    data.put("new", newInfo);
    return data;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\PK10Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */