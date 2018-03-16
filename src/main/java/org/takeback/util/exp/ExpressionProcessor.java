package org.takeback.util.exp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.takeback.util.JSONUtils;
import org.takeback.util.context.ContextUtils;
import org.takeback.util.exp.exception.ExprException;


public class ExpressionProcessor
{
  private static final String BASE_LANG = "base";
  private static ConcurrentHashMap<String, ExpressionSet> languages = new ConcurrentHashMap();
  private static ConcurrentHashMap<String, ExpressionProcessor> instances = new ConcurrentHashMap();
  private String language;
  
  public ExpressionProcessor()
  {
    this("base");
  }
  
  private ExpressionProcessor(String lang) {
    this.language = lang;
    instances.put(this.language, this);
  }
  
  public static ExpressionProcessor instance(String lang) throws ExprException {
    if (lang == null) {
      return instance();
    }
    if (!languages.containsKey(lang)) {
      throw new ExprException("expr language[" + lang + "] is not found.");
    }
    ExpressionProcessor o = null;
    if (!instances.containsKey(lang)) {
      o = new ExpressionProcessor(lang);
    } else {
      o = (ExpressionProcessor)instances.get(lang);
    }
    return o;
  }
  
  public static ExpressionProcessor instance() throws ExprException {
    return instance("base");
  }
  
  public void setExpressionSets(List<ExpressionSet> langs) {
    for (ExpressionSet lang : langs) {
      addExpressionSet(lang.getName(), lang);
    }
  }
  
  public void addExpressionSet(String nm, ExpressionSet es) {
    languages.put(nm, es);
  }
  
  public void addExpressionSet(ExpressionSet es) {
    addExpressionSet("base", es);
  }
  
  private Expression getExpression(String nm) {
    Expression expr = null;
    if (languages.containsKey(this.language)) {
      expr = ((ExpressionSet)languages.get(this.language)).getExpression(nm);
    }
    if (expr == null) {
      expr = ((ExpressionSet)languages.get("base")).getExpression(nm);
    }
    return expr;
  }
  
  private Expression lookup(List<?> ls) throws ExprException {
    if ((ls == null) || (ls.isEmpty())) {
      throw new ExprException("expr list is empty.");
    }
    String nm = (String)ls.get(0);
    Expression expr = getExpression(nm);
    if (expr == null) {
      throw new ExprException("expr[" + nm + "] not found.");
    }
    return expr;
  }
  
  private List<?> parseStr(String exp) throws ExprException {
    try {
      return (List)JSONUtils.parse(exp, List.class);
    }
    catch (Exception e) {
      throw new ExprException(e);
    }
  }
  
  public Object run(String exp) throws ExprException {
    return run(parseStr(exp));
  }
  
  public String toString(String exp) throws ExprException {
    return toString(parseStr(exp));
  }
  
  public Object run(List<?> ls) throws ExprException {
    return lookup(ls).run(ls, this);
  }
  
  public String toString(List<?> ls) throws ExprException {
    return lookup(ls).toString(ls, this);
  }
  
  public String toString(String exp, boolean forPreparedStatement) throws ExprException {
    configExpressionContext(forPreparedStatement);
    return toString(exp);
  }
  
  public String toString(List<?> ls, boolean forPreparedStatement) throws ExprException {
    configExpressionContext(forPreparedStatement);
    return toString(ls);
  }
  
  private void configExpressionContext(boolean forPreparedStatement) {
    ExpressionContextBean bean;
    if (ContextUtils.hasKey("$exp")) {
      ExpressionContextBean bean = (ExpressionContextBean)ContextUtils.get("$exp", ExpressionContextBean.class);
      bean.clearPatameters();
    } else {
      bean = new ExpressionContextBean();
      ContextUtils.put("$exp", bean);
    }
    bean.setForPreparedStatement(forPreparedStatement);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\ExpressionProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */