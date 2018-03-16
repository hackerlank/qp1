package org.takeback.chat.controller;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.chat.entity.LoginLog;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.UserService;
import org.takeback.chat.service.admin.SystemConfigService;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.IPUtil;
import org.takeback.mvc.ResponseUtils;
import org.takeback.thirdparty.support.HttpHelper;
import org.takeback.thirdparty.support.WxConfig;
import org.takeback.util.BeanUtils;
import org.takeback.util.encrypt.CryptoUtils;









@Controller
@RequestMapping({"/thirdparty/login"})
public class ThirdPartyLoginController
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyLoginController.class);
  
  @Autowired
  private WxConfig wxConfig;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private UserStore userStore;
  
  private static final String BEFORE_LOGIN_STATE = "$beforeLoginState";
  

  @RequestMapping(value={"/apply"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView apply(@RequestBody Map<String, Object> params, HttpSession session)
  {
    String type = (String)params.get("type");
    String url;
    switch (type) {
    case "wx": 
      url = getWeixinAuthorUrl(true);
      break;
    default: 
      return ResponseUtils.jsonView(400, "登录方式不支持.");
    }
    String url;
    Object extras = (Map)params.get("extras");
    if ((extras != null) && (((Map)extras).get("fromUrl") != null)) {
      session.setAttribute("$beforeLoginState", extras);
    }
    return ResponseUtils.jsonView(url);
  }
  




  @RequestMapping(value={"/auto"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView autoLogin(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session)
  {
    Integer uid = (Integer)params.get("uid");
    if (uid == null) {
      return ResponseUtils.jsonView(500, "用户id为空。");
    }
    PubUser user = (PubUser)this.userService.get(PubUser.class, uid);
    if (user == null) {
      return ResponseUtils.jsonView(404, "用户不存在。");
    }
    if (("2".equals(user.getStatus())) || ("3".equals(user.getStatus()))) {
      return ResponseUtils.jsonView(404, "账号被锁定或注销,请联系客服咨询处理!");
    }
    Boolean inWeixin = (Boolean)params.get("inWeixin");
    if (user.getWbOpenId() != null) {
      if (inWeixin.booleanValue()) {
        ModelAndView mav = doWxLogin(user.getWxRefreshToken(), user.getWxOpenId(), false, request, session);
        if (mav != null) {
          return mav;
        }
      } else {
        return ResponseUtils.jsonView(500, "只能在微信浏览器中登录。");
      }
    } else {
      String token = (String)params.get("accessToken");
      if ((token == null) || (!token.equals(user.getAccessToken()))) {
        return ResponseUtils.jsonView(402, "用户授权失败, 请重新登录。");
      }
      if (user.getTokenExpireTime().compareTo(new Date()) < 0) {
        return ResponseUtils.jsonView(401, "登录已过期请重新登录。");
      }
    }
    




    session.setAttribute("$uid", user.getId());
    User user1 = (User)BeanUtils.map(user, User.class);
    user1.setUrl(this.wxConfig.getGameServerBaseUrl() + "i?u=" + user1.getId());
    this.userStore.reload(user1.getId());
    return ResponseUtils.jsonView(user1);
  }
  




  @RequestMapping(value={"/wx/code"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView onCodeReturn(@RequestParam("code") String code, @RequestParam(name="isApp", value="isApp", required=false) boolean isApp, HttpServletRequest request, HttpSession session)
  {
    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + this.wxConfig.getWxJSAPIAppId() + "&secret=" + this.wxConfig.getWxJSAPISecret() + "&code=" + code + "&grant_type=authorization_code";
    try
    {
      resultObject = HttpHelper.getJson(url);
    } catch (IOException e) { JSONObject resultObject;
      throw new IllegalStateException("Failed to get open id.", e); }
    JSONObject resultObject;
    if (resultObject.containsKey("errcode")) {
      LOGGER.error("Failed to get open id, error code: {}, caused by: {}", Integer.valueOf(resultObject.getInt("errcode")), resultObject.get("errmsg"));
      if (isApp) {
        return ResponseUtils.jsonView(500, "登录失败");
      }
      return ResponseUtils.modelView("jump", ImmutableMap.of("url", "/#/tab/login", "message", "登录失败。"));
    }
    

    ModelAndView mav = doWxLogin(resultObject.getString("refresh_token"), resultObject.getString("openid"), isApp, request, session);
    if (mav != null) {
      return mav;
    }
    
    String redirectUrl = getRedirectUrl(session);
    Integer uid = (Integer)session.getAttribute("$uid");
    User user = (User)this.userStore.get(uid);
    
    user.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
    LocalDateTime expire = new LocalDateTime();
    expire = expire.plusDays(7);
    user.setTokenExpireTime(expire.toDate());
    this.userService.updateUser(uid.intValue(), ImmutableMap.of("accessToken", user.getAccessToken(), "tokenExpireTime", user.getTokenExpireTime()));
    
    if (isApp) {
      return ResponseUtils.jsonView(ImmutableMap.of("uid", uid, "username", user.getUserId(), "accessToken", user.getAccessToken()));
    }
    return ResponseUtils.modelView("jump", ImmutableMap.of("url", redirectUrl, "uid", uid, "username", user.getUserId(), "accessToken", user.getAccessToken()));
  }
  






  private ModelAndView doWxLogin(String refreshToken, String openId, boolean isApp, HttpServletRequest request, HttpSession session)
  {
    String url2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + this.wxConfig.getWxJSAPIAppId() + "&grant_type=refresh_token&refresh_token=" + refreshToken;
    
    try
    {
      refreshResult = HttpHelper.getJson(url2);
    } catch (IOException e) { JSONObject refreshResult;
      throw new IllegalStateException("Failed to refresh access token.", e); }
    JSONObject refreshResult;
    if (refreshResult.has("errcode")) {
      LOGGER.error("Cannot refresh token, error: {}, message: {}", Integer.valueOf(refreshResult.getInt("errcode")), refreshResult.getString("errmsg"));
      if (isApp) {
        return ResponseUtils.jsonView(500, "登录失败。");
      }
      return ResponseUtils.modelView("jump", ImmutableMap.of("url", "/#/tab/login", "message", "登录失败。"));
    }
    


    String token = refreshResult.getString("access_token");
    String url3 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
    try
    {
      result = HttpHelper.getJson(url3);
    } catch (IOException e) { JSONObject result;
      throw new IllegalStateException("Failed to get access_token.", e); }
    JSONObject result;
    PubUser user = this.userService.getByWxOpenId(openId);
    String headImgUrl = result.getString("headimgurl");
    String nickName = result.getString("nickname");
    String ip = IPUtil.getIp(request);
    if (user == null) {
      Object invitor = WebUtils.getSessionAttribute(request, "$invitor");
      Integer parentId = invitor == null ? null : (Integer)invitor;
      user = createUser(headImgUrl, nickName);
      if (parentId != null) {
        user.setParent(parentId);
      }
      
      user.setWxOpenId(openId);
      user.setRegistIp(ip);
      user.setRegistDate(new Date());
      
      this.userService.save(PubUser.class, user);
    } else {
      Map<String, Object> data = new HashMap();
      if (!Objects.equals(user.getHeadImg(), headImgUrl)) {
        user.setHeadImg(headImgUrl);
        data.put("headImg", headImgUrl);
      }
      if (!Objects.equals(user.getNickName(), nickName)) {
        user.setNickName(nickName);
        data.put("nickName", nickName);
      }
      if (!data.isEmpty()) {
        this.userService.updateUser(user.getId().intValue(), data);
      }
    }
    this.userService.setLoginInfo(ip, user.getId());
    
    LoginLog l = new LoginLog();
    l.setLoginTime(new Date());
    l.setIp(ip);
    l.setUserId(user.getId());
    l.setUserName(user.getUserId());
    






    this.userService.save(LoginLog.class, l);
    
    session.setAttribute("$uid", user.getId());
    return null;
  }
  



  private String getRedirectUrl(HttpSession session)
  {
    String redirectUrl = "/#/tab/rooms";
    
    Map<String, Object> extras = (Map)session.getAttribute("$beforeLoginState");
    session.removeAttribute("$beforeLoginState");
    if (extras != null) {
      redirectUrl = (String)extras.get("fromUrl");
      
      Map<String, String> params = (Map)extras.get("fromParams");
      int idx = redirectUrl.indexOf(":");
      String pvs; if (idx > 0) {
        pvs = redirectUrl.substring(idx + 1);
        redirectUrl = redirectUrl.substring(0, idx);
        String[] tmp = pvs.split("/:");
        if ((tmp.length > 0) && (MapUtils.isNotEmpty(params))) {
          for (String pv : tmp) {
            redirectUrl = redirectUrl + (String)params.get(pv) + "/";
          }
          redirectUrl = redirectUrl.substring(0, redirectUrl.length() - 1);
        }
      }
      idx = redirectUrl.indexOf("?");
      if (idx > 0) {
        redirectUrl = redirectUrl.substring(0, idx + 1);
        if (params != null) {
          for (Map.Entry<String, String> en : params.entrySet()) {
            redirectUrl = redirectUrl + (String)en.getKey() + "=" + (String)en.getValue() + "&";
          }
          if (redirectUrl.endsWith("&")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.length() - 1);
          }
        }
      }
    }
    return "/#/tab" + redirectUrl;
  }
  




  private PubUser createUser(String avatar, String nickName)
  {
    PubUser user = new PubUser();
    user.setHeadImg(avatar);
    user.setMoney(Double.valueOf(0.0D));
    Object conf = SystemConfigService.getInstance().getValue("conf_init_money");
    if (conf != null) {
      try {
        user.setMoney(Double.valueOf(conf.toString()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    user.setNickName(nickName);
    user.setRegistDate(new Date());
    user.setExp(Double.valueOf(0.0D));
    user.setUserType("1");
    user.setSalt(CryptoUtils.getSalt());
    user.setUserId(UUID.randomUUID().toString().replace("-", ""));
    user.setPwd("");
    user.setMoneyCode(user.getPwd());
    user.setUserType("1");
    return user;
  }
  


  private String getWeixinAuthorUrl(boolean needUserInfo)
  {
    String redirectUrl = this.wxConfig.getGameServerBaseUrl() + "thirdparty/login/wx/code";
    try {
      redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + this.wxConfig.getWxJSAPIAppId() + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=" + (needUserInfo ? "snsapi_userinfo" : "snsapi_base") + "&state=STATE#wechat_redirect";
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\ThirdPartyLoginController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */