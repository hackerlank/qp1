package org.takeback.core.accredit.condition;

import java.util.HashMap;
import java.util.List;
import org.dom4j.Element;
import org.takeback.util.context.Context;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class JsonExpCondition implements Condition
{
  Element define;
  HashMap<String, Object> data;
  String queryType;
  
  public void setDefine(Element define)
  {
    this.define = define;
    if (define == null) {
      return;
    }
    Element exp = define.element("exp");
    Element msg = define.element("errMsg");
    this.queryType = define.attributeValue("type");
    String expText = null;
    if (exp == null) {
      expText = define.attributeValue("exp", define.getText());
    }
    else {
      expText = exp.getText();
    }
    try
    {
      expText = expText.trim().replaceAll("'", "\"");
      List<?> lsExp = (List)org.takeback.util.JSONUtils.parse(expText, List.class);
      this.data = new HashMap();
      this.data.put("exp", lsExp);
      if (msg != null) {
        this.data.put("errMsg", msg.getText());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public Element getDefine() {
    return this.define;
  }
  
  public String getMessage() {
    return (String)this.data.get("errMsg");
  }
  
  public Object run(Context ctx) {
    try {
      return ExpressionProcessor.instance().run((List)this.data.get("exp"));
    } catch (ExprException e) {}
    return null;
  }
  
  public HashMap<String, Object> data()
  {
    return this.data;
  }
  
  public String getQueryType() {
    return this.queryType;
  }
  
  public int type() {
    return 1;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\condition\JsonExpCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */