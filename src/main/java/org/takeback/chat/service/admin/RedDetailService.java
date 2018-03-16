package org.takeback.chat.service.admin;

import com.google.common.collect.ImmutableMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.chat.utils.DateUtil;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;









@Service("redDetailService")
public class RedDetailService
  extends MyListService
{
  @Transactional(rollbackFor={Throwable.class})
  public void clear(Map<String, Object> req)
  {
    this.dao.executeUpdate("delete from GcLottery", ImmutableMap.of());
    this.dao.executeUpdate("delete from GcLotteryDetail", ImmutableMap.of());
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void clear2(Map<String, Object> req)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(DateUtil.getStartOfToday());
    c.add(5, -2);
    Date d = c.getTime();
    this.dao.executeUpdate("delete from GcLottery where createTime <=:time", ImmutableMap.of("time", c.getTime()));
    this.dao.executeUpdate("delete from GcLotteryDetail where createDate<=:time", ImmutableMap.of("time", c.getTime()));
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public void clear5(Map<String, Object> req) {
    Calendar c = Calendar.getInstance();
    c.setTime(DateUtil.getStartOfToday());
    c.add(5, -5);
    this.dao.executeUpdate("delete from GcLottery where createTime <=:time", ImmutableMap.of("time", c.getTime()));
    this.dao.executeUpdate("delete from GcLotteryDetail where createDate<=:time", ImmutableMap.of("time", c.getTime()));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\RedDetailService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */