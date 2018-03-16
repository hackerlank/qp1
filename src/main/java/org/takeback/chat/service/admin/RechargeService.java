package org.takeback.chat.service.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.utils.DateUtil;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.exception.CodedBaseRuntimeException;

@Service("rechargeService")
public class RechargeService
  extends MyListService
{
  @Transactional(readOnly=true)
  public Map<String, Object> list(Map<String, Object> req)
  {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    int limit = ((Integer)req.get(LIMIT)).intValue();
    int page = ((Integer)req.get(PAGE)).intValue();
    Map<String, Object> myQuires = (Map)req.get("myQuires");
    String orderInfo = (String)req.get(ORDERINFO);
    StringBuffer hql = new StringBuffer(" from PubRecharge where status =2 ");
    StringBuffer countHql = new StringBuffer("select count(*) from PubRecharge where status =2 ");
    Map<String, Object> param = new HashMap();
    Date startTime = null;
    Date endTime = null;
    Long chargeTimes1 = null;
    Long chargeTimes2 = null;
    Double fee1 = null;
    Double fee2 = null;
    Integer queryUser = null;
    if ((myQuires.containsKey("startTime")) && (myQuires.containsKey("endTime")) && (!"".equals(myQuires.get("startTime"))) && (!"".equals(myQuires.get("endTime")))) {
      startTime = DateUtil.getEndOfTheDay(myQuires.get("startTime").toString());
      endTime = DateUtil.getEndOfTheDay(myQuires.get("endTime").toString());
    }
    if ((myQuires.containsKey("chargeTimes")) && (!"".equals(myQuires.get("chargeTimes")))) {
      String[] chargeTimes = myQuires.get("chargeTimes").toString().split("-");
      chargeTimes1 = Long.valueOf(chargeTimes[0]);
      chargeTimes2 = Long.valueOf(chargeTimes[1]);
    }
    if ((myQuires.containsKey("queryFee")) && (!"".equals(myQuires.get("queryFee")))) {
      String[] fees = myQuires.get("queryFee").toString().split("-");
      fee1 = Double.valueOf(fees[0]);
      fee2 = Double.valueOf(fees[1]);
    }
    if ((myQuires.containsKey("uid")) && (!"".equals(myQuires.get("uid")))) {
      queryUser = (Integer)myQuires.get("uid");
    }
    String idlimit = null;
    if ((queryUser != null) && (!"".equals(queryUser))) {
      PubUser user = (PubUser)this.dao.get(PubUser.class, Integer.valueOf(queryUser.intValue()));
      if (user == null) {
        idlimit = "-1";
      } else {
        idlimit = user.getId().toString();
      }
    } else {
      idlimit = queryUids(chargeTimes1, chargeTimes2, startTime, endTime, fee1, fee2);
    }
    
    Map<String, Object> param2 = new HashMap();
    if ((startTime != null) && (endTime != null)) {
      hql.append(" and finishtime>=:startTime and finishtime<=:endTime");
      countHql.append(" and finishtime>=:startTime and finishtime<=:endTime");
      param.put("startTime", startTime);
      param.put("endTime", endTime);
    }
    if ((idlimit != null) && (idlimit.length() > 0)) {
      hql.append(" and uid in (").append(idlimit).append(")");
      countHql.append(" and uid in (").append(idlimit).append(")");
    }
    hql.append(" order by ").append(orderInfo).append(" , uid ");
    List<?> ls = this.dao.findByHqlPaging(hql.toString(), param, limit, page);
    long count = this.dao.count(countHql.toString(), param);
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Long.valueOf(count));
    result.put("body", ls);
    return result;
  }
  
  @Transactional(readOnly=true)
  private String queryUids(Long chargeTimes1, Long chargeTimes2, Date startTime, Date endTime, Double fee1, Double fee2)
  {
    StringBuffer uids = new StringBuffer();
    StringBuffer limitHql = new StringBuffer(" select uid,count(*) as myCount ,sum(fee) as mySum from PubRecharge where status =2 ");
    Map<String, Object> limitParam = new HashMap();
    if ((startTime != null) && (endTime != null)) {
      limitHql.append(" and finishtime>=:startTime and finishtime<=:endTime ");
      limitParam.put("startTime", startTime);
      limitParam.put("endTime", endTime);
    }
    limitHql.append(" group by uid ");
    if ((fee1 != null) && (fee2 != null)) {
      limitHql.append(" having sum(fee)>= :fee1 and sum(fee)<=:fee2 ");
      limitParam.put("fee1", fee1);
      limitParam.put("fee2", fee2);
    }
    if ((chargeTimes1 != null) && (chargeTimes2 != null)) {
      if (limitHql.indexOf("having") > 0) {
        limitHql.append(" and count(*)>= :count1 and count(*)<=:count2 ");
      } else {
        limitHql.append(" having count(*)>= :count1 and count(*)<=:count2 ");
      }
      limitParam.put("count1", chargeTimes1);
      limitParam.put("count2", chargeTimes2);
    }
    StringBuffer ids = new StringBuffer();
    List<Object[]> res = this.dao.findByHql(limitHql.toString(), limitParam);
    for (int i = 0; i < res.size(); i++) {
      ids.append(((Object[])res.get(i))[0]).append(",");
    }
    if (ids.length() > 0) {
      return ids.substring(0, ids.length() - 1);
    }
    
    return " -1 ";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RechargeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */