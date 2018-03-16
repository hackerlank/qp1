package org.takeback.core.accredit.condition;

import java.util.HashMap;
import java.util.List;
import org.dom4j.Element;
import org.takeback.core.accredit.list.AccreditList;
import org.takeback.core.accredit.list.WhiteList;
import org.takeback.core.accredit.result.AuthorizeResult;
import org.takeback.util.context.Context;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class LimitCondition implements Condition
{
  Element define;
  AccreditList list;
  String value;
  List<?> exp;
  String listType;
  
  public void setDefine(Element define)
  {
    this.define = define;
    if (define == null) {
      return;
    }
    this.listType = define.attributeValue("list", "white");
    this.value = define.attributeValue("value");
    if (this.value != null) {
      if (this.listType.equals("white")) {
        this.list = new WhiteList();
      }
      else {
        this.list = new org.takeback.core.accredit.list.BlackList();
      }
      if (this.value.indexOf(",") > -1) {
        String[] items = this.value.split(",");
        for (String i : items) {
          this.list.add(i, null);
        }
      }
      else {
        this.list.add(this.value, null);
      }
    }
    else {
      try {
        String s = define.attributeValue("exp", define.getText());
        this.exp = ((List)org.takeback.util.JSONUtils.parse(s, List.class));
      }
      catch (Exception localException) {}
    }
  }
  

  public Element getDefine()
  {
    return this.define;
  }
  
  public String getMessage() {
    return "";
  }
  
  public Object run(Context ctx) {
    if (this.list != null) {
      String v = (String)ctx.get("value");
      return Boolean.valueOf(this.list.authorize(v).getResult() != 0);
    }
    if (this.exp != null) {
      try {
        return (Boolean)ExpressionProcessor.instance().run(this.exp);
      } catch (ExprException e) {
        e.printStackTrace();
      }
    }
    return Boolean.valueOf(true);
  }
  
  public HashMap<String, Object> data() {
    HashMap<String, Object> h = new HashMap();
    h.put("type", "limit");
    h.put("value", this.value);
    h.put("list", this.listType);
    h.put("exp", this.exp);
    return h;
  }
  
  public String getQueryType()
  {
    return "limit";
  }
  
  public int type()
  {
    return 3;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\condition\LimitCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */