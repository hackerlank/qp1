package org.takeback.core.accredit.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.dom4j.Element;
import org.takeback.util.StringValueParser;
import org.takeback.util.context.Context;

public class OverrideCondition implements Condition
{
  Element define;
  HashMap<String, String> overrides = new HashMap();
  
  public void setDefine(Element define) {
    this.define = define;
    if (define == null)
      return;
    String value;
    if (define.elements().size() == 0) {
      String target = define.attributeValue("target");
      value = define.attributeValue("value", define.getText());
      this.overrides.put(target, value);
    }
    else {
      List<Element> list = define.elements();
      for (Element o : list) {
        this.overrides.put(o.attributeValue("target"), o.attributeValue("value", o.getText()));
      }
    }
  }
  
  public Element getDefine() {
    return this.define;
  }
  
  public String getMessage() {
    return "";
  }
  
  public String run(Context ctx) {
    Set<String> keys = this.overrides.keySet();
    for (String target : keys) {
      String v = (String)StringValueParser.parse((String)this.overrides.get(target), String.class);
      ctx.put("cfg." + target, v);
    }
    return null;
  }
  
  public HashMap<String, Object> data()
  {
    HashMap<String, Object> h = new HashMap();
    h.put("type", "override");
    return h;
  }
  
  public String getQueryType()
  {
    return "override";
  }
  
  public int type()
  {
    return 2;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\condition\OverrideCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */