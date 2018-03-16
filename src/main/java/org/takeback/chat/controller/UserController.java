package org.takeback.chat.controller;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.GcLottery;
import org.takeback.chat.entity.GcLotteryDetail;
import org.takeback.chat.entity.GcRoom;
import org.takeback.chat.entity.LoginLog;
import org.takeback.chat.entity.PcGameLog;
import org.takeback.chat.entity.PcRateConfig;
import org.takeback.chat.entity.PubBank;
import org.takeback.chat.entity.PubExchangeLog;
import org.takeback.chat.entity.PubRecharge;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.entity.PubWithdraw;
import org.takeback.chat.entity.TransferLog;
import org.takeback.chat.service.PcEggService;
import org.takeback.chat.service.RoomService;
import org.takeback.chat.service.SystemService;
import org.takeback.chat.service.UserService;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.IPUtil;
import org.takeback.chat.utils.SmsUtil;
import org.takeback.chat.utils.ValueControl;
import org.takeback.mvc.ResponseUtils;
import org.takeback.mvc.listener.SessionListener;
import org.takeback.thirdparty.support.WxConfig;
import org.takeback.util.BeanUtils;
import org.takeback.util.annotation.AuthPassport;
import org.takeback.util.encrypt.CryptoUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;
import org.takeback.util.valid.ValidateUtil;
import web.wx.utils.JsonUtil;
import web.wx.utils.WeixinOauth2Token;
import web.wx.utils.WeixinOauth2Userinfo;
import web.wx.utils.http.HttpClientConnectionManager;












@Controller
public class UserController
{
  @Autowired
  private UserService userService;
  @Autowired
  private UserStore userStore;
  @Autowired
  private RoomStore roomStore;
  @Autowired
  private SystemService systemService;
  @Autowired
  private RoomService roomService;
  @Autowired
  private PcEggService pcService;
  @Autowired
  private WxConfig wxConfig;
  private static final String wx_appid = "wx3607d2c2b18ac6ac";
  private static final String wx_secret = "e518d22c55ba2e4806cac847ad43306d";
  
  @AuthPassport
  @RequestMapping(value={"/user/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView upload(@RequestParam(value="file", required=true) MultipartFile file, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    String path = request.getSession().getServletContext().getRealPath("img/user");
    String fileName = file.getOriginalFilename();
    try
    {
      ImageInputStream iis = ImageIO.createImageInputStream(file);
      Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
      if (!iter.hasNext()) {
        return ResponseUtils.jsonView(300, "文件格式错误!");
      }
      iis.close();
    }
    catch (Exception localException1) {}
    fileName = "/" + uid + fileName.substring(fileName.lastIndexOf("."));
    
    File targetFile = new File(path, fileName);
    if (!targetFile.exists()) {
      targetFile.mkdirs();
    }
    try
    {
      file.transferTo(targetFile);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    String headImage = "img/user" + fileName;
    this.userService.updateHeadImg(uid.intValue(), headImage);
    Map<String, Object> res = new HashMap();
    res.put("headImage", headImage + "?t=" + Math.random());
    this.userStore.reload(uid);
    return ResponseUtils.jsonView(200, "上传成功!", res);
  }
  
  @RequestMapping(value={"/user/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView updateUser(@RequestBody Map data, HttpServletRequest request)
  {
    Integer userId = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (userId == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    try
    {
      if ((data.get("id") == null) || (!userId.equals(data.get("id")))) {
        return ResponseUtils.jsonView(500, "userId not matched");
      }
      PubUser pubUser = this.userService.updateUser(userId.intValue(), data);
      this.userStore.reload(userId);
      return ResponseUtils.jsonView(200, "修改成功!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return ResponseUtils.jsonView(500, "修改失败!");
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/updatePsw"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView updatePsw(@RequestBody Map data, HttpServletRequest request)
  {
    Integer userId = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (userId == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    try
    {
      if ((data.get("id") == null) || (!userId.equals(data.get("id")))) {
        return ResponseUtils.jsonView(500, "userId not matched");
      }
      Object oldPwd = data.get("oldPwd");
      if ((oldPwd == null) || (oldPwd.toString().length() == 0)) {
        return ResponseUtils.jsonView(500, "原密码不能为空!");
      }
      Object newPwd = data.get("newPwd");
      Object confirmPwd = data.get("confirmPwd");
      if ((newPwd == null) || (newPwd.toString().length() == 0)) {
        return ResponseUtils.jsonView(500, "新密码不能为空!");
      }
      if (!newPwd.equals(confirmPwd)) {
        return ResponseUtils.jsonView(500, "两次输入新密码不一致!");
      }
      PubUser user = this.userService.get(userId.intValue(), oldPwd.toString());
      if (user == null) {
        return ResponseUtils.jsonView(500, "原密码不正确!");
      }
      this.userService.updatePwd(userId.intValue(), CryptoUtils.getHash(newPwd.toString(), StringUtils.reverse(user.getSalt())));
      return ResponseUtils.jsonView(200, "密码修改成功!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return ResponseUtils.jsonView(500, "修改失败!");
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/sendSmsCode"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView sendSmsCode(@RequestBody Map data, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (uid == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    if (WebUtils.getSessionAttribute(request, "mobileCodeTime") != null)
    {
      Date d1 = (Date)WebUtils.getSessionAttribute(request, "mobileCodeTime");
      Date d2 = new Date();
      Long deep = Long.valueOf((d2.getTime() - d1.getTime()) / 1000L);
      if (deep.longValue() <= 120L) {
        return ResponseUtils.jsonView(500, "请" + (120L - deep.longValue()) + "秒后再尝试!");
      }
    }
    if (data.get("mobile") == null) {
      return ResponseUtils.jsonView(500, "手机号不能为空!");
    }
    String mobile = (String)data.get("mobile");
    if (!ValidateUtil.instance().validatePhone(mobile)) {
      return ResponseUtils.jsonView(500, "手机号码格式不正确!");
    }
    Long rand = Long.valueOf(Math.round(Math.random() * 1000000.0D));
    String msg = "您的验证码为:" + rand.toString();
    try
    {
      SmsUtil.send(mobile, msg);
      WebUtils.setSessionAttribute(request, "mobile", mobile);
      WebUtils.setSessionAttribute(request, "mobileCode", rand.toString());
      WebUtils.setSessionAttribute(request, "mobileCodeTime", new Date());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, "验证码发送失败!");
    }
    return ResponseUtils.jsonView(200, "验证码已成功发送!");
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/bindMobile"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView bindMobile(@RequestBody Map data, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (uid == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    if ((WebUtils.getSessionAttribute(request, "mobile") == null) || 
      (WebUtils.getSessionAttribute(request, "mobileCode") == null)) {
      return ResponseUtils.jsonView(500, "手机号码绑定失败!");
    }
    String sMobile = (String)WebUtils.getSessionAttribute(request, "mobile");
    Object smsCode = data.get("smsCode");
    String sCode = (String)WebUtils.getSessionAttribute(request, "mobileCode");
    if (!sCode.equals(smsCode)) {
      return ResponseUtils.jsonView(500, "验证码不正确!");
    }
    try
    {
      this.userService.bindMobile(uid.intValue(), sMobile);
      WebUtils.setSessionAttribute(request, "mobile", null);
      WebUtils.setSessionAttribute(request, "mobileCode", null);
      WebUtils.setSessionAttribute(request, "mobileCodeTime", null);
      return ResponseUtils.jsonView(200, "手机号码绑定成功!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return ResponseUtils.jsonView(500, "手机号码绑定失败!");
  }
  
  @RequestMapping(value={"/lottery/adminInfo"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView adminInfo()
  {
    Map<String, Object> rs = new HashMap();
    rs.put("withdraw", this.systemService.getWidthdraw());
    rs.put("online", Integer.valueOf(SessionListener.getOnlineNumber()));
    
    rs.put("recharge", Integer.valueOf(0));
    return ResponseUtils.jsonView(200, "ok", rs);
  }
  
  @AuthPassport
  @RequestMapping({"/user/{uid}"})
  public ModelAndView getUser(@PathVariable Integer uid, HttpServletRequest request)
  {
    Integer userId = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (Objects.equals(userId, uid))
    {
      PubUser user = (PubUser)this.userService.get(PubUser.class, uid);
      




      User user1 = (User)BeanUtils.map(user, User.class);
      user1.setUrl(this.wxConfig.getGameServerBaseUrl() + "/i?u=" + user1.getId());
      return ResponseUtils.jsonView(user1);
    }
    return ResponseUtils.jsonView(501, "not authorized");
  }
  
  @AuthPassport
  @RequestMapping({"/user/balance"})
  public ModelAndView getBalance(HttpServletRequest request)
  {
    Integer userId = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    return ResponseUtils.jsonView(Double.valueOf(this.userService.getBalance(userId.intValue())));
  }
  





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
  


































  @RequestMapping(value={"/wx_login"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void wx_login(HttpServletRequest request, HttpServletResponse response)
  {
    System.out.println("***************1");
    String state = UUID.randomUUID().toString();
    request.getSession().setAttribute("wx_state", state);
    
    StringBuffer url = request.getRequestURL();
    String tempContextUrl = "/";
    String callback = tempContextUrl + "wx_login_callback";
    










    getOut(response).println("<script>location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3607d2c2b18ac6ac&redirect_uri=" + URLEncoder.encode(callback) + "&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect';</script>");
  }
  


  @RequestMapping(value={"/wx_loginCheck"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView wx_loginCheck(HttpServletRequest request, HttpServletResponse response)
  {
    String user_wx_username = (String)request.getSession().getAttribute("user_wx_username");
    request.getSession().removeAttribute("user_wx_username");
    
    String ip = IPUtil.getIp(request);
    List<PubUser> ulist = this.userService.findByProperty(PubUser.class, "userId", user_wx_username);
    if ((ulist == null) || (ulist.isEmpty())) {
      return ResponseUtils.jsonView(404, "用户不存在或者密码错误");
    }
    PubUser user = (PubUser)ulist.get(0);
    if ("9".equals(user.getUserType())) {
      return ResponseUtils.jsonView(404, "不能登陆非玩家账号!");
    }
    if (("2".equals(user.getStatus())) || ("3".equals(user.getStatus()))) {
      return ResponseUtils.jsonView(404, "账号被锁定或注销,请联系客服咨询处理!");
    }
    WebUtils.setSessionAttribute(request, "$uid", user.getId());
    
    user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
    LocalDateTime expire = new LocalDateTime().plusDays(7);
    user.setTokenExpireTime(expire.toDate());
    this.userService.updateUser(user.getId().intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
    this.userService.setLoginInfo(ip, user.getId());
    




















    LoginLog l = new LoginLog();
    l.setIp(ip);
    l.setLoginTime(new Date());
    l.setUserId(user.getId());
    l.setUserName(user.getUserId());
    this.userService.save(LoginLog.class, l);
    User user1 = (User)BeanUtils.map(user, User.class);
    
    StringBuffer url = request.getRequestURL();
    String tempContextUrl = "/";
    

    user1.setUrl(tempContextUrl + "i?u=" + user1.getId());
    this.userStore.reload(user1.getId());
    return ResponseUtils.jsonView(user1);
  }
  



  @RequestMapping(value={"/wx_login_callback"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void wx_login_callback(HttpServletRequest request, HttpServletResponse response)
  {
    try
    {
      System.out.println("***************1");
      String code = request.getParameter("code");
      String state = request.getParameter("state");
      String temp_state = (String)request.getSession().getAttribute("wx_state");
      if ((StringUtils.isEmpty(temp_state)) || (StringUtils.isEmpty(state)) || (!state.equals(temp_state))) {
        System.out.println("***************2");
        return;
      }
      
      DefaultHttpClient httpclient = new DefaultHttpClient();
      httpclient = (DefaultHttpClient)HttpClientConnectionManager.getSSLInstance(httpclient);
      

      HttpPost httpost = HttpClientConnectionManager.getPostMethod("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx3607d2c2b18ac6ac&secret=e518d22c55ba2e4806cac847ad43306d&code=" + code + "&grant_type=authorization_code");
      HttpResponse response_http = httpclient.execute(httpost);
      String jsonStr = EntityUtils.toString(response_http.getEntity(), "UTF-8");
      if (jsonStr.indexOf("FAIL") != -1) {
        return;
      }
      WeixinOauth2Token tk = (WeixinOauth2Token)JsonUtil.getDTO(jsonStr, WeixinOauth2Token.class);
      System.out.println("tk.getOpenid():" + tk.getOpenid());
      List<PubUser> pulist = this.userService.findByProperty(PubUser.class, "wxOpenId", tk.getOpenid());
      




      if ((pulist == null) || (pulist.isEmpty())) {
        System.out.println("数据库中没有数据");
        
        httpost = HttpClientConnectionManager.getPostMethod("https://api.weixin.qq.com/sns/userinfo?access_token=" + tk.getAccess_token() + "&openid=" + tk.getOpenid() + "&lang=zh_CN");
        response_http = httpclient.execute(httpost);
        jsonStr = EntityUtils.toString(response_http.getEntity(), "UTF-8");
        System.out.println("jsonStr----:" + jsonStr);
        if (jsonStr.indexOf("FAIL") != -1) {
          return;
        }
        WeixinOauth2Userinfo userinfo = (WeixinOauth2Userinfo)JsonUtil.getDTO(jsonStr, WeixinOauth2Userinfo.class);
        System.out.println(userinfo.getHeadimgurl());
        System.out.println(userinfo.getNickname());
        String username = "wx_" + (int)((Math.random() * 9.0D + 1.0D) * 100000.0D);
        

        pulist = this.userService.findByProperty(PubUser.class, "userId", username);
        while ((pulist != null) && (pulist.size() > 0)) {
          username = "wx_" + (int)((Math.random() * 9.0D + 1.0D) * 100000.0D);
          pulist = this.userService.findByProperty(PubUser.class, "userId", username);
        }
        

        String password = "123456";
        String mobile = "";
        String wx = "";
        String alipay = "";
        try
        {
          Integer parentId = null;
          











          parentId = (Integer)request.getSession().getAttribute("$invitor");System.out.println("found proxy ==> " + parentId);String ip = IPUtil.getIp(request);
          




          PubUser user = this.userService.register(username, password, mobile, wx, alipay, parentId, ip);
          if (user == null) {
            return;
          }
          
          user.setWxOpenId(tk.getOpenid());
          user.setHeadImg(userinfo.getHeadimgurl());
          this.userService.update(PubUser.class, user);
          
          WebUtils.setSessionAttribute(request, "$uid", user.getId());
          
          user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
          LocalDateTime expire = new LocalDateTime().plusDays(7);
          user.setTokenExpireTime(expire.toDate());
          this.userService.updateUser(user.getId().intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
          
          User user1 = (User)BeanUtils.map(user, User.class);
          
          StringBuffer url = request.getRequestURL();
          String tempContextUrl = "/";
          

          user1.setUrl(tempContextUrl + "i?u=" + user1.getId());
          this.userStore.reload(user1.getId());
          
          request.getSession().setAttribute("user_wx_username", user.getUserId());
          getOut(response).println("<script>location.href='/wxLoginCheck.jsp';</script>");

        }
        catch (CodedBaseRuntimeException e)
        {
          e.printStackTrace();
        }
      }
      



      System.out.println("数据库中有数据1111");
      PubUser user = (PubUser)pulist.get(0);
      request.getSession().setAttribute("user_wx_username", user.getUserId());
      getOut(response).println("<script>location.href='/wxLoginCheck.jsp';</script>");















    }
    catch (Exception e)
    {















      e.printStackTrace();
    }
  }
  



























































































































  @RequestMapping(value={"/login"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView login(@RequestBody Map data, HttpServletRequest request)
  {
    String ip = IPUtil.getIp(request);
    String username = (String)data.get("username");
    String password = (String)data.get("password");
    
    PubUser user = this.userService.login(username, password);
    




    if (user == null) {
      return ResponseUtils.jsonView(404, "用户不存在或者密码错误");
    }
    if ("9".equals(user.getUserType())) {
      return ResponseUtils.jsonView(404, "不能登陆非玩家账号!");
    }
    if (("2".equals(user.getStatus())) || ("3".equals(user.getStatus()))) {
      return ResponseUtils.jsonView(404, "账号被锁定或注销,请联系客服咨询处理!");
    }
    WebUtils.setSessionAttribute(request, "$uid", user.getId());
    
    user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
    LocalDateTime expire = new LocalDateTime().plusDays(7);
    user.setTokenExpireTime(expire.toDate());
    this.userService.updateUser(user.getId().intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
    this.userService.setLoginInfo(ip, user.getId());
    























    LoginLog l = new LoginLog();
    l.setIp(ip);
    l.setLoginTime(new Date());
    l.setUserId(user.getId());
    l.setUserName(user.getUserId());
    this.userService.save(LoginLog.class, l);
    User user1 = (User)BeanUtils.map(user, User.class);
    StringBuffer url = request.getRequestURL();
    String tempContextUrl = "/";
    
    user1.setUrl(tempContextUrl + "i?u=" + user1.getId());
    this.userStore.reload(user1.getId());
    return ResponseUtils.jsonView(user1);
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/createUser"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView createUser(@RequestBody Map data, HttpServletRequest request)
  {
    Integer userId = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    
    String username = (String)data.get("username");
    String password = (String)data.get("password");
    String mobile = data.containsKey("mobile") ? (String)data.get("mobile") : "";
    String wx = data.containsKey("wx") ? (String)data.get("wx") : "";
    String alipay = data.containsKey("alipay") ? (String)data.get("alipay") : "";
    try
    {
      Integer parentId = userId;
      


      String ip = IPUtil.getIp(request);
      PubUser user = this.userService.register(username, password, mobile, wx, alipay, parentId, ip);
      if (user == null) {
        return ResponseUtils.jsonView(500, "注册失败");
      }
      user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
      LocalDateTime expire = new LocalDateTime().plusDays(7);
      user.setTokenExpireTime(expire.toDate());
      this.userService.updateUser(user.getId().intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
      
      return ResponseUtils.jsonView(200, "创建成功!");
    }
    catch (CodedBaseRuntimeException e)
    {
      return ResponseUtils.jsonView(e.getCode(), e.getMessage());
    }
  }
  
  @RequestMapping(value={"/register"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView register(@RequestBody Map data, HttpServletRequest request)
  {
    String username = (String)data.get("username");
    String password = (String)data.get("password");
    String mobile = data.containsKey("mobile") ? (String)data.get("mobile") : "";
    String wx = data.containsKey("wx") ? (String)data.get("wx") : "";
    String alipay = data.containsKey("alipay") ? (String)data.get("alipay") : "";
    try
    {
      Integer parentId = null;
      Object invitor = WebUtils.getSessionAttribute(request, "$invitor");
      parentId = invitor == null ? null : (Integer)invitor;
      if ((parentId == null) && (data.containsKey("parentId"))) {
        parentId = (Integer)data.get("parentId");
      }
      String ip = IPUtil.getIp(request);
      PubUser user = this.userService.register(username, password, mobile, wx, alipay, parentId, ip);
      if (user == null) {
        return ResponseUtils.jsonView(500, "注册失败");
      }
      WebUtils.setSessionAttribute(request, "$uid", user.getId());
      

























      user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
      LocalDateTime expire = new LocalDateTime().plusDays(7);
      user.setTokenExpireTime(expire.toDate());
      this.userService.updateUser(user.getId().intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
      
      User user1 = (User)BeanUtils.map(user, User.class);
      
      StringBuffer url = request.getRequestURL();
      String tempContextUrl = "/";
      

      user1.setUrl(tempContextUrl + "i?u=" + user1.getId());
      this.userStore.reload(user1.getId());
      return ResponseUtils.jsonView(user1);
    }
    catch (CodedBaseRuntimeException e)
    {
      return ResponseUtils.jsonView(e.getCode(), e.getMessage());
    }
  }
  
  @RequestMapping({"/user/myLottery"})
  public ModelAndView myLottery(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (uid == null) {
      return ResponseUtils.jsonView(403, "notLogin");
    }
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<GcLottery> list = this.userService.find(GcLottery.class, ImmutableMap.of("sender", uid), pageSize, pageNo, "createTime desc");
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    List<Map<String, Object>> records = new ArrayList(list.size());
    for (GcLottery gcLottery : list)
    {
      Map<String, Object> map = new HashMap();
      map.put("createTime", gcLottery.getCreateTime());
      map.put("money", gcLottery.getMoney());
      Room room = (Room)this.roomStore.get(gcLottery.getRoomId());
      map.put("roomName", room == null ? "不明" : room.getName());
      records.add(map);
    }
    return ResponseUtils.jsonView(records);
  }
  
  @AuthPassport
  @RequestMapping({"/user/transfer"})
  public ModelAndView transfer(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      Integer targetId = (Integer)params.get("userId");
      Integer money = (Integer)params.get("money");
      this.userService.transfer(uid, targetId, money);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "转账成功");
  }
  
  @AuthPassport
  @RequestMapping({"/user/prixyRecharge"})
  public ModelAndView prixyRecharge(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      Integer targetId = (Integer)params.get("userId");
      Integer money = (Integer)params.get("money");
      this.userService.prixyRecharge(uid, targetId, money);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "上分成功");
  }
  
  @AuthPassport
  @RequestMapping({"/user/prixyUnRecharge"})
  public ModelAndView prixyUnRecharge(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      Integer targetId = (Integer)params.get("userId");
      Integer money = (Integer)params.get("money");
      this.userService.prixyUnRecharge(uid, targetId, money);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "下分成功");
  }
  
  @AuthPassport
  @RequestMapping({"/user/prixyRechargeLog"})
  public ModelAndView prixyRechargeLog(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "from PubRecharge where operator=:uid and rechargeType=:rechargeType order by id desc ";
    Map map = new HashMap();
    map.put("uid", uid);
    map.put("rechargeType", "2");
    List<TransferLog> list = this.userService.findByHql(hql, map, pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/prixyUnRechargeLog"})
  public ModelAndView prixyUnRechargeLog(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "from PubRecharge where operator=:uid and rechargeType=:rechargeType order by id desc ";
    Map map = new HashMap();
    map.put("uid", uid);
    map.put("rechargeType", "3");
    List<TransferLog> list = this.userService.findByHql(hql, map, pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/getNickName"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView getNickName(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    try
    {
      Integer uid = (Integer)params.get("uid");
      PubUser u = (PubUser)this.userService.get(PubUser.class, uid);
      if (u == null) {
        return ResponseUtils.jsonView(500, "目标账号不存在!");
      }
      Map m = new HashedMap();
      m.put("nickName", u.getNickName());
      m.put("money", u.getMoney());
      m.put("code", Integer.valueOf(200));
      return ResponseUtils.jsonView(m);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/checkRecharge"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView checkRecharge(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    try
    {
      Integer uid = (Integer)params.get("uid");
      PubUser u = (PubUser)this.userService.get(PubUser.class, uid);
      if (u == null) {
        return ResponseUtils.jsonView(500, "目标账号不存在!");
      }
      Map m = new HashedMap();
      m.put("nickName", u.getNickName());
      m.put("money", u.getMoney());
      m.put("code", Integer.valueOf(200));
      return ResponseUtils.jsonView(m);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @AuthPassport
  @RequestMapping({"/user/exchange"})
  public ModelAndView exchange(@RequestBody Map<String, Object> params, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      Integer shopId = Integer.valueOf((String)params.get("shopId"));
      String name = (String)params.get("name");
      String address = (String)params.get("address");
      String mobile = (String)params.get("mobile");
      this.userService.exchange(uid, shopId, name, address, mobile);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "兑换成功,请等待管理员处理发货!");
  }
  
  @AuthPassport
  @RequestMapping({"/user/transferLogs"})
  public ModelAndView transferLogs(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "from TransferLog where fromUid =:uid or toUid=:uid order by id desc ";
    List<TransferLog> list = this.userService.findByHql(hql, ImmutableMap.of("uid", uid), pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/proxyUsers"})
  public ModelAndView proxyUsers(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "select id,userId ,nickName , registDate,money from PubUser where parent=:uid order by id desc ";
    List<PubUser> list = this.userService.findByHql(hql, ImmutableMap.of("uid", uid), pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/proxyApply"})
  public ModelAndView proxyApply(HttpServletRequest request)
  {
    try
    {
      Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
      Map<String, Object> conf = this.systemService.getProxyConfig();
      this.userService.proxyApply(uid, conf);
      
      return ResponseUtils.jsonView(200, "申请成功");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return ResponseUtils.jsonView(500, e.getMessage());
    }
  }
  
  @AuthPassport
  @RequestMapping({"/user/proxyConfig"})
  public ModelAndView proxyConfig(HttpServletRequest request)
  {
    try
    {
      Map res = new HashedMap();
      res.put("code", Integer.valueOf(200));
      res.put("body", this.systemService.getProxyConfig());
      return ResponseUtils.jsonView(res);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return ResponseUtils.jsonView(500, "配置获取失败!");
  }
  
  @AuthPassport
  @RequestMapping({"/user/proxyLogs"})
  public ModelAndView proxyLogs(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    String hql = "from ProxyVote where parentId=:uid order by id desc ";
    List<TransferLog> list = this.userService.findByHql(hql, ImmutableMap.of("uid", uid), pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/getUid"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getUid(HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    return ResponseUtils.jsonView(uid);
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/vc"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView vc(@RequestParam int uid, @RequestParam String roomId, @RequestParam double value, HttpServletRequest request)
  {
    ValueControl.setValue(roomId, Integer.valueOf(uid), new BigDecimal(value));
    return ResponseUtils.jsonView(ValueControl.getStore());
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/clean"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView clean(@RequestParam int uid, @RequestParam String roomId, HttpServletRequest request)
  {
    ValueControl.clean(roomId, Integer.valueOf(uid));
    return ResponseUtils.jsonView(ValueControl.getStore());
  }
  
  @AuthPassport
  @RequestMapping({"/user/myBonus"})
  public ModelAndView myBonus(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<GcLotteryDetail> list = this.userService.findByHql(" from GcLotteryDetail where uid=:uid and roomId is not null and roomId<>'' order by id desc ", ImmutableMap.of("uid", uid), pageSize, pageNo);
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    List<Map<String, Object>> records = new ArrayList(list.size());
    for (GcLotteryDetail gcLottery : list)
    {
      Map<String, Object> map = new HashMap();
      map.put("createTime", gcLottery.getCreateDate());
      map.put("money", gcLottery.getCoin());
      map.put("desc1", gcLottery.getDesc1());
      map.put("inoutNum", Double.valueOf(gcLottery.getInoutNum()));
      
      Room room = (Room)this.roomStore.get(gcLottery.getRoomId());
      if (room != null)
      {
        map.put("roomName", room == null ? "不明" : room.getName());
        records.add(map);
      }
    }
    return ResponseUtils.jsonView(records);
  }
  
  @AuthPassport
  @RequestMapping({"/user/myBonus03"})
  public ModelAndView myBonus03(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<PcGameLog> list = this.userService.find(PcGameLog.class, ImmutableMap.of("uid", uid), pageSize, pageNo, "betTime desc");
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    List<Map<String, Object>> records = new ArrayList(list.size());
    Map<String, PcRateConfig> rates = this.pcService.getPcRateConfig();
    for (PcGameLog gcLottery : list)
    {
      Map<String, Object> map = new HashMap();
      map.put("createTime", gcLottery.getBetTime());
      map.put("money", Double.valueOf(gcLottery.getFreeze()));
      map.put("luckyNumber", gcLottery.getLuckyNumber());
      if ("num".equals(gcLottery.getBetType())) {
        map.put("desc1", "数字" + gcLottery.getBet());
      } else {
        map.put("desc1", ((PcRateConfig)rates.get(gcLottery.getBet())).getAlias());
      }
      map.put("inoutNum", Double.valueOf(gcLottery.getUserInout()));
      
      map.put("num", gcLottery.getNum());
      records.add(map);
    }
    return ResponseUtils.jsonView(records);
  }
  
  @AuthPassport
  @RequestMapping({"/user/exchangeLogs"})
  public ModelAndView exchangeLogs(@RequestParam int pageNo, @RequestParam int pageSize, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<PubExchangeLog> list = this.userService.find(PubExchangeLog.class, ImmutableMap.of("uid", uid), pageSize, pageNo, "id desc");
    if ((list == null) || (list.isEmpty())) {
      return ResponseUtils.jsonView(null);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/roomHistory"})
  public ModelAndView roomHistory(HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    
    return ResponseUtils.jsonView(this.userService.findByProperty(GcLottery.class, "sender", uid, "createTime desc"));
  }
  
  @RequestMapping(value={"/user/logout"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView logout(HttpSession session)
  {
    if (session != null)
    {
      Integer uid = (Integer)session.getAttribute("$uid");
      if (uid != null)
      {
        Map<String, Object> map = new HashMap();
        map.put("accessToken", null);
        map.put("tokenExpireTime", null);
        this.userService.updateUser(uid.intValue(), map);
      }
      session.invalidate();
    }
    return ResponseUtils.jsonView(200, "成功退出.");
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/rechargeRecords"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getRechargeRecords(@RequestParam int pageSize, @RequestParam int pageNo, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<PubRecharge> list = this.userService.findByHql("from PubRecharge where uid=:uid and status =2 order by finishtime desc", ImmutableMap.of("uid", uid), pageSize, pageNo);
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/withdrawRecords"})
  public ModelAndView getWithdrawRecords(@RequestParam int pageSize, @RequestParam int pageNo, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    if (pageNo <= 0) {
      pageNo = 1;
    }
    if (pageSize > 20) {
      pageSize = 20;
    }
    List<PubWithdraw> list = this.userService.findByHql("from PubWithdraw where uid=:uid  order by tradetime desc", ImmutableMap.of("uid", uid), pageSize, pageNo);
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping({"/user/bankRecords"})
  public ModelAndView getBankRecords(HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    List<PubBank> list = this.userService.findByProperty(PubBank.class, "userId", uid, "createTime desc");
    return ResponseUtils.jsonView(list);
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/withdraw"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView withdraw(@RequestBody Map<String, Object> data, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      this.userService.withdraw(data, uid.intValue());
    }
    catch (Exception e)
    {
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "提现成功.");
  }
  














  @AuthPassport
  @RequestMapping(value={"/user/roomApply"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView roomApply(HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    try
    {
      this.userService.createRoom(uid.intValue());
    }
    catch (Exception e)
    {
      return ResponseUtils.jsonView(500, e.getMessage());
    }
    return ResponseUtils.jsonView(200, "success.");
  }
  

  @AuthPassport
  @RequestMapping(value={"/user/roomCount"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getUserRoomCount(HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    int count = this.roomService.getUserRoomCount(uid);
    return ResponseUtils.jsonView(Integer.valueOf(count));
  }
  
  @AuthPassport
  @RequestMapping(value={"/user/rooms"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView getUserRooms(@RequestParam int pageSize, @RequestParam int pageNo, HttpServletRequest request)
  {
    Integer uid = (Integer)WebUtils.getSessionAttribute(request, "$uid");
    List<GcRoom> list = this.roomService.getUserRooms(uid, pageSize, pageNo);
    if ((list != null) && (!list.isEmpty()))
    {
      List<Room> rooms = new ArrayList(list.size());
      for (GcRoom gcRoom : list) {
        rooms.add((Room)this.roomStore.get(gcRoom.getId()));
      }
      return ResponseUtils.jsonView(rooms);
    }
    return ResponseUtils.jsonView(list);
  }
  
  @RequestMapping(value={"/i"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void getUserRooms(@RequestParam Integer u, HttpServletRequest request, HttpServletResponse response)
  {
    try
    {
      WebUtils.setSessionAttribute(request, "$invitor", u);
      response.sendRedirect("/");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\UserController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */