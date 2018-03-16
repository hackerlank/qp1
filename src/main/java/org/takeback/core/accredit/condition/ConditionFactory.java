package org.takeback.core.accredit.condition;

import com.google.common.collect.Maps;
import java.util.HashMap;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionFactory
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ConditionFactory.class);
  private static HashMap<String, String> cls = Maps.newHashMap();
  
  static { cls.put("exp", "org.takeback.core.accredit.condition.JsonExpCondition");
    cls.put("filter", "org.takeback.core.accredit.condition.JsonExpCondition");
    cls.put("notify", "org.takeback.core.accredit.condition.JsonExpCondition");
    cls.put("override", "org.takeback.core.accredit.condition.OverrideCondition");
    cls.put("limit", "org.takeback.core.accredit.condition.LimitCondition");
  }
  
  public static Condition createCondition(Element define) { if (define == null) {
      return null;
    }
    try
    {
      Condition cnd = (Condition)Class.forName((String)cls.get(define.attributeValue("type", "exp"))).newInstance();
      cnd.setDefine(define);
      return cnd;
    }
    catch (Exception e) {
      LOGGER.error("condition init failed:", e);
    }
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\condition\ConditionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */