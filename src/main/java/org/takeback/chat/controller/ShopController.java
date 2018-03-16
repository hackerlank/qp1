package org.takeback.chat.controller;

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
import org.takeback.chat.entity.PubExchangeLog;
import org.takeback.chat.entity.PubShop;
import org.takeback.chat.service.ShopService;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.annotation.AuthPassport;
import org.takeback.util.exception.CodedBaseRuntimeException;






@Controller
public class ShopController
{
  @Autowired
  private ShopService shopService;
  
  @RequestMapping(value={"/shop/list"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView shopList(@RequestBody Map<String, Object> param, HttpServletRequest request)
  {
    int pageNo = 1;
    int pageSize = 20;
    if (param.containsKey("pageNo")) {
      pageNo = Integer.valueOf(param.get("pageNo").toString()).intValue();
    }
    if (param.containsKey("pageSize")) {
      pageSize = Integer.valueOf(param.get("pageSize").toString()).intValue();
    }
    
    List<PubShop> list = this.shopService.list(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @RequestMapping({"/shop/get"})
  public ModelAndView get(@RequestParam int id, HttpServletRequest request)
  {
    PubShop shop = this.shopService.get(Integer.valueOf(id));
    return ResponseUtils.jsonView(shop);
  }
  
  @AuthPassport
  @RequestMapping({"/shop/getContactInfo"})
  public ModelAndView getContactInfo(HttpServletRequest request) {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    PubExchangeLog log = this.shopService.getContactInfo(uid);
    return ResponseUtils.jsonView(log);
  }
  
  @AuthPassport
  @RequestMapping(value={"/shop/doExchange"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView doExchange(@RequestBody Map<String, Object> param, HttpServletRequest request) {
    try {
      Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
      Integer shopId = Integer.valueOf(param.get("shopId").toString());
      String name = param.get("name").toString();
      String mobile = param.get("mobile").toString();
      String address = param.get("address").toString();
      this.shopService.doExchage(uid, shopId, name, address, mobile);
    } catch (CodedBaseRuntimeException e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "兑换成功");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\ShopController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */