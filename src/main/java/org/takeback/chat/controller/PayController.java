package org.takeback.chat.controller;

import cn.beecloud.BCCache;
import cn.beecloud.bean.BCOrder;
import com.obaopay.util.EkaPayConfig;
import com.obaopay.util.EkaPayEncrypt;
import com.obaopay.util.StringUtils;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.PubRecharge;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.PubRechargeService;
import org.takeback.mvc.ResponseUtils;
import org.takeback.pay.PayOrderFactory;
import org.takeback.pay.PaymentException;
import org.takeback.thirdparty.support.KouDaiConfig;
import org.takeback.thirdparty.support.XinTongConfig;
import org.takeback.util.MD5StringUtil;
import org.takeback.util.annotation.AuthPassport;






@Controller
@RequestMapping({"/pay"})
public class PayController
{
  public static final Logger LOGGER = LoggerFactory.getLogger(PayController.class);
  

  @Autowired
  private PubRechargeService pubRechargeService;
  

  @Autowired
  private KouDaiConfig kouDaiConfig;
  
  @Autowired
  private XinTongConfig xinTongConfig;
  

  public PrintWriter getOut(HttpServletResponse resp)
  {
    try
    {
      resp.setCharacterEncoding("utf-8");
      resp.setContentType("text/html");
      return resp.getWriter();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  @AuthPassport
  @RequestMapping(value={"apply"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView payApply(@RequestBody Map<String, String> data, HttpSession session)
  {
    String payChannel = (String)data.get("payChannel");
    String strTotalFee = (String)data.get("totalFee");
    String title = "充值";
    String identityId = null;
    if (payChannel.equals("YEE_WAP"))
    {
      identityId = UUID.randomUUID().toString().replace("-", "");
      session.setAttribute("identityId", identityId);
    }
    Integer totalFee = Integer.valueOf((int)Double.valueOf(strTotalFee).doubleValue());
    try
    {
      BCOrder bcOrder = PayOrderFactory.getInstance().getPayOrder(payChannel, totalFee, title, identityId);
      Map<String, String> result = new HashMap();
      result.put("url", bcOrder.getUrl());
      result.put("html", bcOrder.getHtml());
      return ResponseUtils.jsonView(result);
    }
    catch (PaymentException e)
    {
      LOGGER.error("Failed to apply payment transaction", e);
    }
    return ResponseUtils.jsonView(500, "支付失败.");
  }
  
  @AuthPassport
  @RequestMapping(value={"apply/wx"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView payApplyWx(@RequestBody Map<String, String> data)
  {
    String strTotalFee = (String)data.get("totalFee");
    String title = "充值";
    double totalFee = Double.valueOf(strTotalFee).doubleValue();
    String url = PayOrderFactory.getInstance().getWxAuthorizeUrl(title, totalFee);
    return ResponseUtils.jsonView(url);
  }
  
  @AuthPassport
  @RequestMapping(value={"apply/wx"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView payApplyWxRedirected(@RequestParam double totalFee, @RequestParam String code, HttpServletRequest request)
  {
    String title = "充值";
    BCOrder bcOrder = null;
    try
    {
      bcOrder = PayOrderFactory.getInstance().getWxJSPayOrder(Integer.valueOf((int)totalFee), title, code);

    }
    catch (PaymentException e)
    {
      LOGGER.error("Failed to apply payment transaction", e);
      new ModelAndView("wxpay", new HashMap() {});
    }
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt(title);
    pubRecharge.setFee(Double.valueOf(totalFee));
    pubRecharge.setGoodsname(title);
    pubRecharge.setPayno(bcOrder.getObjectId());
    pubRecharge.setTradeno(bcOrder.getBillNo());
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("1");
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    pubRecharge.setUid(uid);
    PubUser u = (PubUser)this.pubRechargeService.get(PubUser.class, uid);
    pubRecharge.setUserIdText(u.getUserId());
    this.pubRechargeService.addRechargeRecord(pubRecharge);
    
    Map<String, String> map = bcOrder.getWxJSAPIMap();
    return new ModelAndView("wxpay", map);
  }
  




  @RequestMapping(value={"hrefbackurl"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void hrefbackurl(HttpServletRequest request, HttpServletResponse response)
  {
    System.out.println("进来hrefbackurl");
    


    String md5key = StringUtils.formatString(EkaPayConfig.key);
    


    String orderid = StringUtils.formatString(request.getParameter("orderid"));
    String opstate = StringUtils.formatString(request.getParameter("opstate"));
    String ovalue = StringUtils.formatString(request.getParameter("ovalue"));
    String sign = StringUtils.formatString(request.getParameter("sign"));
    String sysorderid = StringUtils.formatString(request.getParameter("sysorderid"));
    String completiontime = StringUtils.formatString(request.getParameter("completiontime"));
    String attach = StringUtils.formatString(request.getParameter("attach"));
    String msg = StringUtils.formatString(request.getParameter("msg"));
    
    if ((!StringUtils.hasText(orderid).booleanValue()) || (!StringUtils.hasText(opstate).booleanValue()) || 
      (!StringUtils.hasText(ovalue).booleanValue()) || (!StringUtils.hasText(sign).booleanValue()))
    {
      System.out.println("出问题了orderid=" + orderid + "&opstate=" + opstate + "&ovalue=" + ovalue + "&sign=" + sign);
      getOut(response).println("opstate=-1");
      return;
    }
    System.out.println("正确的");
    String checksign = EkaPayEncrypt.obaopayCardBackMd5Sign(orderid, opstate, ovalue, md5key);
    if (checksign.equals(sign)) {
      System.out.println("验证通过");
      
      if ((opstate.equals("0")) || (opstate.equals("-3")))
      {



        PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(orderid);
        















        System.out.println("///////////////////////////////////////////////////" + pubRecharge.getStatus());
        


        if (pubRecharge.getStatus().equals("1"))
        {




          System.out.println("进来这里" + Double.valueOf(ovalue));
          pubRecharge.setRealfee(Double.valueOf(Double.valueOf(ovalue).doubleValue()));
          this.pubRechargeService.setRechargeFinished(pubRecharge);
        }
        
        getOut(response).println("<script>alert('充值成功');location.href='http://www.6556hb.com/#/tab/account';</script>");
      }
      else {
        getOut(response).println("<script>alert('充值失败');location.href='http://www.6556hb.com/#/tab/account';</script>");
      }
    } else {
      System.out.println("验证失败");
      
      getOut(response).println("<script>alert('充值失败');location.href='http://www.6556hb.com/#/tab/account';</script>");
    }
  }
  






  @RequestMapping(value={"callbackurl"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void callbackurl(HttpServletRequest request, HttpServletResponse response)
  {
    String md5key = StringUtils.formatString(EkaPayConfig.key);
    


    String orderid = StringUtils.formatString(request.getParameter("orderid"));
    
    String opstate = StringUtils.formatString(request.getParameter("opstate"));
    String ovalue = StringUtils.formatString(request.getParameter("ovalue"));
    String sign = StringUtils.formatString(request.getParameter("sign"));
    String sysorderid = StringUtils.formatString(request.getParameter("sysorderid"));
    String completiontime = StringUtils.formatString(request.getParameter("completiontime"));
    String attach = StringUtils.formatString(request.getParameter("attach"));
    String msg = StringUtils.formatString(request.getParameter("msg"));
    
    if ((!StringUtils.hasText(orderid).booleanValue()) || (!StringUtils.hasText(opstate).booleanValue()) || 
      (!StringUtils.hasText(ovalue).booleanValue()) || (!StringUtils.hasText(sign).booleanValue()))
    {
      getOut(response).println("opstate=-1");
      return;
    }
    String checksign = EkaPayEncrypt.obaopayCardBackMd5Sign(orderid, opstate, ovalue, md5key);
    if (checksign.equals(sign))
    {
      if (opstate.equals("0"))
      {



        PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(orderid);
        


        System.out.println("**************************" + pubRecharge.getStatus());
        

        if (pubRecharge.getStatus().equals("1"))
        {
          pubRecharge.setRealfee(Double.valueOf(Double.valueOf(ovalue).doubleValue()));
          this.pubRechargeService.setRechargeFinished(pubRecharge);
        }
      }
      




      getOut(response).println("opstate=0");
    }
    else {
      getOut(response).println("opstate=-2");
    }
  }
  













  @RequestMapping(value={"goPay"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView goPay(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response)
  {
    Object payIndex = params.get("payIndex");
    



    Object amount = params.get("amount");
    






    String parter = StringUtils.formatString(EkaPayConfig.parter);
    
    String md5key = StringUtils.formatString(EkaPayConfig.key);
    
    String api_url = StringUtils.formatString(EkaPayConfig.bank_url);
    


    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    String title = "充值";
    String orderid = uid + new Date().getTime();
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt(title);
    pubRecharge.setFee(Double.valueOf(amount.toString()));
    pubRecharge.setGoodsname(title);
    pubRecharge.setPayno(null);
    pubRecharge.setTradeno(orderid);
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("1");
    
    pubRecharge.setUid(uid);
    PubUser u = (PubUser)this.pubRechargeService.get(PubUser.class, uid);
    pubRecharge.setUserIdText(u.getUserId());
    this.pubRechargeService.addRechargeRecord(pubRecharge);
    
    String callbackurl = StringUtils.formatString("http://www.6556hb.com/pay/callbackurl");
    String hrefbackurl = StringUtils.formatString("http://www.6556hb.com/pay/hrefbackurl");
    orderid = StringUtils.formatString(orderid);
    String type = "";
    Integer pi = Integer.valueOf(payIndex.toString());
    if (pi.intValue() == 0) {
      type = StringUtils.formatString("2099");
    } else if (pi.intValue() == 1) {
      type = StringUtils.formatString("2098");
    }
    

    String value = StringUtils.formatString(amount.toString());
    String attach = "";
    





    String sign = EkaPayEncrypt.obaopayBankMd5Sign(type, parter, value, orderid, callbackurl, md5key);
    String payerIp = request.getRemoteAddr();
    

























    String str = "<form id=\"payBillForm\" action=\"" + api_url + "\" method=\"GET\">" + 
      "<input type='hidden' name='parter'   value='" + parter + "'>" + 
      "<input type='hidden' name='type' value='" + type + "'>" + 
      "<input type='hidden' name='orderid' value='" + orderid + "'>" + 
      "<input type='hidden' name='callbackurl'   value='" + callbackurl + "'>" + 
      "<input type='hidden' name='hrefbackurl'   value='" + hrefbackurl + "'>" + 
      "<input type='hidden' name='value'   value='" + value + "'>" + 
      "<input type='hidden' name='attach'  value='" + attach + "'>" + 
      "<input type='hidden' name='payerIp' value='" + payerIp + "'>" + 
      "<input type='hidden' name='sign'   value='" + sign + "'>" + 
      "</form>";
    

    return ResponseUtils.jsonView(str);
  }
  





  @AuthPassport
  @RequestMapping(value={"apply/koudai"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView payApplyKoudai(@RequestBody Map<String, String> params, HttpServletRequest request)
  {
    Object totalFee = params.get("totalFee");
    if ((totalFee == null) || (!NumberUtils.isNumber(totalFee.toString()))) {
      return ResponseUtils.jsonView(500, "充值金额不对。");
    }
    Double fee = NumberUtils.createDouble(totalFee.toString());
    if (fee.doubleValue() < 2.0D) {
      return ResponseUtils.jsonView(500, "充值金额不能小于￥2.00。");
    }
    String title = "充值";
    if (params.get("title") != null) {
      title = (String)params.get("title");
    }
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt(title);
    pubRecharge.setFee(fee);
    pubRecharge.setGoodsname(title);
    pubRecharge.setTradeno(UUID.randomUUID().toString().replace("-", ""));
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("1");
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    pubRecharge.setUid(uid);
    PubUser u = (PubUser)this.pubRechargeService.get(PubUser.class, uid);
    pubRecharge.setUserIdText(u.getUserId());
    this.pubRechargeService.addRechargeRecord(pubRecharge);
    
    String chanel = (String)params.get("payChannel");
    String price = String.format("%.2f", new Object[] { pubRecharge.getFee() });
    


    String url = this.kouDaiConfig.getRestApiAddress() + "?P_UserId=" + this.kouDaiConfig.getPartnerId() + "&P_OrderId=" + pubRecharge.getTradeno() + "&P_FaceValue=" + price + "&P_Price=" + price + "&P_ChannelId=" + chanel + "&P_Quantity=1&P_Result_URL=" + this.kouDaiConfig.getGameServerBaseUrl() + "pay/feedback/koudai&P_PostKey=";
    



    String raw = this.kouDaiConfig.getPartnerId() + "|" + pubRecharge.getTradeno() + "|||" + String.format("%.2f", new Object[] { pubRecharge.getFee() }) + "|" + chanel + "|" + this.kouDaiConfig.getSecretCode();
    String postKey = MD5StringUtil.MD5Encode(raw);
    url = url + postKey;
    return ResponseUtils.jsonView(url);
  }
  
  @RequestMapping(value={"feedback/koudai"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void koudaiCallback(@RequestParam("P_OrderId") String tradeNo, @RequestParam("P_UserId") String partnerId, @RequestParam("P_ErrCode") int errorCode, @RequestParam(value="P_ErrMsg", defaultValue="") String errorMsg, @RequestParam("P_FaceValue") double totalFee, @RequestParam("P_ChannelId") String chanelId, @RequestParam("P_PostKey") String postKey0, @RequestParam("P_CardId") String cardId, @RequestParam("P_CardPass") String cardPass, HttpServletResponse response)
    throws IOException
  {
    if (!partnerId.equals(this.kouDaiConfig.getPartnerId()))
    {
      LOGGER.error("Pay trade [{}] is not mine.", tradeNo);
      response.getOutputStream().println("errcode=0");
      return;
    }
    PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(tradeNo);
    if (errorCode != 0)
    {
      LOGGER.error("Pay trade [{}] failed: ", tradeNo, errorMsg);
    }
    else
    {
      if (pubRecharge.getStatus().equals("2"))
      {
        response.getOutputStream().println("errcode=0");
        return;
      }
      if (pubRecharge.getFee().doubleValue() != totalFee)
      {
        LOGGER.error("Total fee of trade [{}] is different with the pay trade, expect: {}, {} in fact", new Object[] { tradeNo, pubRecharge.getFee(), Double.valueOf(totalFee) });
        return;
      }
      String raw = partnerId + "|" + tradeNo + "|" + cardId + "|" + cardPass + "|" + String.format("%.5f", new Object[] { Double.valueOf(totalFee) }) + "|" + chanelId + "|" + this.kouDaiConfig.getSecretCode();
      String postKey = MD5StringUtil.MD5Encode(raw);
      if (!postKey.equals(postKey0))
      {
        LOGGER.error("Post key of trade [{}] not match, expected is: {}, {} in fact", new Object[] { tradeNo, postKey, postKey0 });
        return;
      }
      pubRecharge.setRealfee(Double.valueOf(totalFee));
      this.pubRechargeService.setRechargeFinished(pubRecharge);
    }
    response.getOutputStream().println("errcode=0");
  }
  
  @RequestMapping(value={"feedback/koudai"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public void koudaiCallback0(@RequestBody Map<String, Object> data, HttpServletResponse response)
    throws IOException
  {
    String tradeNo = (String)data.get("P_OrderId");
    String partnerId = (String)data.get("P_UserId");
    if (!partnerId.equals(this.kouDaiConfig.getPartnerId()))
    {
      LOGGER.error("Pay trade is not mine.");
      response.getOutputStream().println("errcode=0");
      return;
    }
    int errorCode = ((Integer)data.get("P_ErrCode")).intValue();
    PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(tradeNo);
    if (errorCode != 0)
    {
      LOGGER.error("Pay trade failed: ", data.get("P_ErrMsg"));
    }
    else
    {
      if (pubRecharge.getStatus().equals("2"))
      {
        response.getOutputStream().println("errcode=0");
        return;
      }
      double totalFee = ((Double)data.get("P_FaceValue")).doubleValue();
      String chanelId = (String)data.get("P_ChannelId");
      if (pubRecharge.getFee().doubleValue() != totalFee)
      {
        LOGGER.error("Total fee is different with the pay trade, expect: {}, {} in fact", pubRecharge.getFee(), Double.valueOf(totalFee));
        return;
      }
      String raw = partnerId + "|" + tradeNo + "|||" + String.format("%.2f", new Object[] { Double.valueOf(totalFee) }) + "|" + chanelId + "|" + this.kouDaiConfig.getSecretCode();
      String postKey = MD5StringUtil.MD5Encode(raw);
      if (!postKey.equals(data.get("P_PostKey")))
      {
        LOGGER.error("Post key not match, expected is: {}, {} in fact", postKey, data.get("P_PostKey"));
        return;
      }
      pubRecharge.setRealfee(Double.valueOf(totalFee));
      this.pubRechargeService.setRechargeFinished(pubRecharge);
    }
    response.getOutputStream().println("errcode=0");
  }
  
  @RequestMapping(value={"webhook"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public void callback(@RequestBody Map<String, Object> data, HttpServletResponse response)
    throws IOException
  {
    String sign = (String)data.get("sign");
    Long timestamp = (Long)data.get("timestamp");
    String transactionId = (String)data.get("transaction_id");
    String text = BCCache.getAppID() + BCCache.getAppSecret() + timestamp;
    String mySign = MD5StringUtil.MD5EncodeUTF8(text);
    
    long timeDifference = System.currentTimeMillis() - timestamp.longValue();
    if ((!mySign.equals(sign)) || (timeDifference > 300000L))
    {
      LOGGER.error("Sign validation failed: {}.", transactionId);
      response.getOutputStream().println("fail");
      return;
    }
    Boolean success = (Boolean)data.get("trade_success");
    if (success.booleanValue())
    {
      String transactionType = (String)data.get("transaction_type");
      if (transactionType.equals("PAY"))
      {
        Integer transactionFee = (Integer)data.get("transaction_fee");
        PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(transactionId);
        if (pubRecharge == null)
        {
          response.getOutputStream().println("fail");
          return;
        }
        if (pubRecharge.getStatus().equals("2"))
        {
          response.getOutputStream().println("success");
          return;
        }
        pubRecharge.setRealfee(Double.valueOf(transactionFee.intValue()));
        this.pubRechargeService.setRechargeFinished(pubRecharge);
        
        response.getOutputStream().println("success");
      }
    }
  }
  
  @AuthPassport
  @RequestMapping(value={"apply/xintong"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView payApplyXintong(@RequestBody Map<String, String> params, HttpServletRequest request)
  {
    Object totalFee = params.get("totalFee");
    if ((totalFee == null) || (!NumberUtils.isNumber(totalFee.toString()))) {
      return ResponseUtils.jsonView(500, "充值金额不对。");
    }
    Double fee = NumberUtils.createDouble(totalFee.toString());
    


    String title = "充值";
    if (params.get("title") != null) {
      title = (String)params.get("title");
    }
    PubRecharge pubRecharge = new PubRecharge();
    pubRecharge.setStatus("1");
    pubRecharge.setDescpt(title);
    pubRecharge.setFee(fee);
    pubRecharge.setGoodsname(title);
    pubRecharge.setTradeno(getOrderNo());
    pubRecharge.setTradetime(new Date());
    pubRecharge.setGift(Double.valueOf(0.0D));
    pubRecharge.setRechargeType("1");
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    pubRecharge.setUid(uid);
    PubUser u = (PubUser)this.pubRechargeService.get(PubUser.class, uid);
    pubRecharge.setUserIdText(u.getUserId());
    this.pubRechargeService.addRechargeRecord(pubRecharge);
    
    String price = String.format("%.2f", new Object[] { pubRecharge.getFee() });
    String type = (String)params.get("payChannel");
    



    String sign = "parter=" + this.xinTongConfig.getPartnerId() + "&type=" + type + "&value=" + price + "&orderid=" + pubRecharge.getTradeno() + "&callbackurl=" + this.xinTongConfig.getGameServerBaseUrl() + "pay/feedback/xintong";
    System.out.println(sign + this.xinTongConfig.getSecretCode());
    String signKey = MD5StringUtil.MD5Encode(sign + this.xinTongConfig.getSecretCode());
    
    String url = this.xinTongConfig.getRestApiAddress() + "?" + sign + "&hrefbackurl=" + this.xinTongConfig.getGameServerBaseUrl() + "&agent=&sign=" + signKey;
    
    System.out.println(url);
    return ResponseUtils.jsonView(url);
  }
  
  @RequestMapping(value={"feedback/xintong"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void xintongCallback(@RequestParam("orderid") String tradeNo, @RequestParam("opstate") int opstate, @RequestParam("ovalue") double totalFee, @RequestParam("sign") String postKey0, HttpServletResponse response)
    throws IOException
  {
    String raw = "orderid=" + tradeNo + "&opstate=" + opstate + "&ovalue=" + String.format("%.2f", new Object[] { Double.valueOf(totalFee) }) + this.xinTongConfig.getSecretCode();
    
    String postKey = MD5StringUtil.MD5Encode(raw);
    if (!postKey.equals(postKey0))
    {
      LOGGER.error("Post key of trade [{}] not match, expected is: {}, {} in fact", new Object[] { tradeNo, postKey, postKey0 });
      response.getOutputStream().println("opstate=-2");
      return;
    }
    PubRecharge pubRecharge = this.pubRechargeService.getRechargeRecordByTradeNo(tradeNo);
    if (opstate != 0)
    {
      LOGGER.error("Pay trade [{}] failed: ", tradeNo, "参数无效或签名错误！");
    }
    else
    {
      if (pubRecharge.getStatus().equals("2"))
      {
        response.getOutputStream().println("opstate=0");
        return;
      }
      if (pubRecharge.getFee().doubleValue() != totalFee)
      {
        LOGGER.error("Total fee of trade [{}] is different with the pay trade, expect: {}, {} in fact", new Object[] { tradeNo, pubRecharge.getFee(), Double.valueOf(totalFee) });
        return;
      }
      pubRecharge.setRealfee(Double.valueOf(totalFee));
      this.pubRechargeService.setRechargeFinished(pubRecharge);
    }
    response.getOutputStream().println("opstate=0");
  }
  
  public static void main(String[] args)
  {
    Double d = Double.valueOf(0.1D);
    
    System.out.println(String.format("%.2f", new Object[] { d }));
  }
  
  private static long orderNum = 0L;
  private static String date;
  
  public static synchronized String getOrderNo()
  {
    String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    if ((date == null) || (!date.equals(str)))
    {
      date = str;
      orderNum = 0L;
    }
    orderNum += 1L;
    long orderNo = Long.parseLong(date) * 10000L;
    orderNo += orderNum;
    return orderNo;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\PayController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */