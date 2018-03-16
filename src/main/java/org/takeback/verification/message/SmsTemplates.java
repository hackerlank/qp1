package org.takeback.verification.message;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.Resource;
import org.takeback.core.resource.ResourceCenter;
import org.takeback.util.params.ParamUtils;
import org.takeback.util.xml.XMLHelper;






public class SmsTemplates
{
  public static final String TEMPLATE_REGISTER_CODE = "1";
  public static final String TEMPLATE_FIND_PSW_CODE = "2";
  public static final String TEMPLATE_FIND_PAYPSW_CODE = "3";
  private static Map<String, String> smsStore;
  
  public static String getTemplate(String tpl)
  {
    return getTemplate(tpl, null);
  }
  
  public static String getTemplate(String tpl, Map<String, String> params) {
    if (smsStore.containsKey(tpl)) {
      String t = (String)smsStore.get(tpl);
      if (params != null) {
        for (String p : params.keySet()) {
          t = t.replace("#" + p + "#", String.valueOf(params.get(p)));
        }
      }
      return 
        t.replace("#app#", ParamUtils.getParamSafe("NAME")).replace("#company#", ParamUtils.getParamSafe("NAME")).replace("#telphone#", ParamUtils.getParamSafe("TELPHONE"));
    }
    throw new IllegalArgumentException(String.format("sms template %s not exists.", new Object[] { tpl }));
  }
  
  public static void reload() {
    smsStore = Maps.newHashMap();
    try {
      Resource resource = ResourceCenter.load("sms.xml");
      Document doc = XMLHelper.getDocument(resource.getInputStream());
      Element root = doc.getRootElement();
      List<Element> tpls = root.elements("tpl");
      for (Element tpl : tpls) {
        smsStore.put(tpl.attributeValue("id"), tpl.getText());
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("load sms.xml failed.", e);
    }
  }
  
  static {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\verification\message\SmsTemplates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */