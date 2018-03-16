package org.takeback.util.exp;

import java.util.HashMap;

public class ExpressionSet
{
  private String name;
  private HashMap<String, Expression> exprs = new HashMap();
  
  public void setName(String nm) {
    this.name = nm;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setExpressions(java.util.List<Expression> exprs) {
    for (Expression expr : exprs) {
      addExpression(expr.getName(), expr);
    }
  }
  
  public void addExpression(String nm, Expression expr) {
    this.exprs.put(nm, expr);
  }
  
  public void register(String nm, Expression expr) {
    this.exprs.put(nm, expr);
  }
  
  public Expression getExpression(String nm) {
    if (this.exprs.containsKey(nm)) {
      return (Expression)this.exprs.get(nm);
    }
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\ExpressionSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */