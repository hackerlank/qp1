package org.takeback.mvc.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.valid.ValidateUtil;
import org.takeback.verification.image.VerifyCodeUtils;
import org.takeback.verification.message.MessageProcessor;




@Controller
@RequestMapping({"code"})
public class CodeController
{
  private static final Logger log = LoggerFactory.getLogger(CodeController.class);
  public static final String SMS_CODE = "smscode";
  public static final String VERIFY_CODE = "verifycode";
  private LoadingCache<String, Integer> cache;
  private static final int maxCodeRequest = 10;
  @Autowired
  private MessageProcessor messageProcessor;
  
  public CodeController() { this.cache = CacheBuilder.newBuilder().maximumSize(500L).expireAfterWrite(12L, TimeUnit.HOURS).build(new CacheLoader()
    {
      public Integer load(String k) throws Exception {
        return Integer.valueOf(0);
      }
    }); }
  



  @RequestMapping(value={"phone"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView shortMessage(HttpServletRequest request, String phonenumb, String verifycode, @RequestParam(required=false) String type)
  {
    if (!verifycode.equalsIgnoreCase((String)WebUtils.getSessionAttribute(request, "verifycode"))) {
      return ResponseUtils.jsonView(909, "图片验证码不正确");
    }
    WebUtils.setSessionAttribute(request, "verifycode", null);
    if (!ValidateUtil.instance().validatePhone(phonenumb)) {
      return ResponseUtils.jsonView(502, "手机号码不正确");
    }
    int c = ((Integer)this.cache.getUnchecked(phonenumb)).intValue();
    if (c >= 10) {
      return ResponseUtils.jsonView(501, "对不起，为了安全起见，您的手机号码12小时内只能获得10次短信验证码。");
    }
    c++;
    this.cache.put(phonenumb, Integer.valueOf(c));
    
    String code = null;
    if (StringUtils.isEmpty(type)) {
      code = this.messageProcessor.sendCode(phonenumb, "1");
    } else {
      code = this.messageProcessor.sendCode(phonenumb, type);
    }
    
    String host = request.getHeader("host");
    if (!StringUtils.isEmpty(host)) {
      host = host.toLowerCase();
      if ((host.startsWith("127.0.0.1")) || (host.startsWith("localhost")) || (host.startsWith("wengshankj.oicp.net")) || (host.startsWith("192.168.3.200"))) {
        code = "8888";
      }
    }
    

    log.info("send code [{}] to {} at {}", new Object[] { code, phonenumb, new DateTime().toString("yyyy-MM-dd HH:mm:ss") });
    WebUtils.setSessionAttribute(request, "smscode", code);
    return ResponseUtils.jsonView(200, "手机验证码已发送");
  }
  
  @RequestMapping(value={"image"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void imageVerifyCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(required=false, defaultValue="150") int w, @RequestParam(required=false, defaultValue="60") int h)
    throws IOException
  {
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0L);
    response.setContentType("image/jpeg");
    

    String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
    
    WebUtils.setSessionAttribute(request, "verifycode", verifyCode.toLowerCase());
    

    VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
  }
  
  @RequestMapping({"ckSmsCode"})
  public ModelAndView checkSMSCode(HttpServletRequest request, String smsCode) {
    if (smsCode.equals(WebUtils.getSessionAttribute(request, "smscode"))) {
      return ResponseUtils.jsonView(200, "手机验证码正确");
    }
    return ResponseUtils.jsonView(500, "手机验证码不正确");
  }
  
  @RequestMapping({"ckImageCode"})
  public ModelAndView checkIMAGECode(HttpServletRequest request, String imageCode)
  {
    if ((imageCode == null) || ("".equals(imageCode))) {
      return ResponseUtils.jsonView(500, "图片验证码不正确");
    }
    if (imageCode.equalsIgnoreCase(String.valueOf(WebUtils.getSessionAttribute(request, "verifycode")))) {
      return ResponseUtils.jsonView(200, "图片验证码正确");
    }
    return ResponseUtils.jsonView(500, "图片验证码不正确");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\CodeController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */