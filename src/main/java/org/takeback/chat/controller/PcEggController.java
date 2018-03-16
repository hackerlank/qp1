package org.takeback.chat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.PcEggLog;
import org.takeback.chat.entity.PcRateConfig;
import org.takeback.chat.service.PK10Service;
import org.takeback.chat.service.PcEggService;
import org.takeback.chat.service.UserService;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.UserStore;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.annotation.AuthPassport;








@Controller
public class PcEggController
{
  @Autowired
  private UserService userService;
  @Autowired
  private UserStore userStore;
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private PcEggService eggService;
  @Autowired
  private PK10Service pk10Service;
  
  @AuthPassport
  @RequestMapping(value={"/rates"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getRates()
  {
    Map<String, List<PcRateConfig>> list = this.eggService.getPcRateConfigs();
    if ((list != null) && (!list.isEmpty())) {
      return ResponseUtils.jsonView(200, "OK", list);
    }
    return ResponseUtils.jsonView(400, "no data.");
  }
  




  @AuthPassport
  @RequestMapping(value={"/pc/bet"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView bet(@RequestBody Map<String, String> params, HttpServletRequest request)
  {
    int num = Integer.parseInt((String)params.get("num"));
    String key = (String)params.get("key");
    double money = Double.parseDouble((String)params.get("money"));
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    String roomId = (String)WebUtils.getSessionAttribute(request, "roomId");
    try {
      this.eggService.bet(Integer.valueOf(num), key, Double.valueOf(money), uid, roomId);
      return ResponseUtils.jsonView(200, num + "期成功投注,祝君好运!");
    } catch (Exception e) {
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  





  @AuthPassport
  @RequestMapping(value={"/pc/cancelBet"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView cancelBet(@RequestBody Map<String, String> params, HttpServletRequest request)
  {
    int num = Integer.parseInt((String)params.get("num"));
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try {
      double money = this.eggService.cancelBet(Integer.valueOf(num), uid).doubleValue();
      return ResponseUtils.jsonView(200, "你已取消本期所有下注：" + money + "金币，请核对账户余额，如有疑问请第一时间联系我们！");
    } catch (Exception e) {
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @RequestMapping(value={"/test/open"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView testOpen(HttpServletRequest request)
  {
    Integer num = Integer.valueOf(793399);
    String exp = "8+2+4";
    String lucky = "14";
    try {
      this.eggService.open(num, exp, lucky);
      return ResponseUtils.jsonView(200, "开奖成功!");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @RequestMapping(value={"/pk"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView pk(String date, HttpServletRequest request) {
    return ResponseUtils.jsonView(200, "ok", this.pk10Service.getData(date));
  }
  





  @RequestMapping({"/pc/getPcEggLog"})
  public ModelAndView getPcEggLog(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize, HttpServletRequest request)
  {
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "from PcEggLog order by id desc ";
    List<PcEggLog> list = this.eggService.findByHql(hql, null, pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    List<Map<String, Object>> records = new ArrayList(list.size());
    for (PcEggLog pcEggLog : list) {
      Map<String, Object> data = new HashMap();
      data.put("id", pcEggLog.getId());
      data.put("lucky", pcEggLog.getLucky());
      data.put("openTime", pcEggLog.getOpenTime());
      Integer intVal = Integer.valueOf(pcEggLog.getLucky());
      if (intVal.intValue() % 2 == 0) {
        data.put("dan", Boolean.valueOf(false));
      } else {
        data.put("dan", Boolean.valueOf(true));
      }
      
      if (intVal.intValue() >= 14) {
        data.put("da", Boolean.valueOf(true));
      } else if (intVal.intValue() <= 13) {
        data.put("da", Boolean.valueOf(false));
      }
      
      Integer[] arrayOfInteger = PcEggService.red;int i = arrayOfInteger.length; for (int j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
        if (i == intVal.intValue()) {
          data.put("color", "red");
        }
      }
      
      arrayOfInteger = PcEggService.green;i = arrayOfInteger.length; for (j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
        if (i == intVal.intValue()) {
          data.put("color", "green");
        }
      }
      
      arrayOfInteger = PcEggService.blue;i = arrayOfInteger.length; for (j = 0; j < i; j++) { int i = arrayOfInteger[j].intValue();
        if (i == intVal.intValue()) {
          data.put("color", "blue");
        }
      }
      records.add(data);
    }
    return ResponseUtils.jsonView(records);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\PcEggController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */