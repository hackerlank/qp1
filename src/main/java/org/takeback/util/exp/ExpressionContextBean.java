package org.takeback.util.exp;

import java.util.Map;

public class ExpressionContextBean
{
  private boolean forPreparedStatement = false;
  private Map<String, Object> statementParameters;
  
  public boolean isForPreparedStatement() {
    return this.forPreparedStatement;
  }
  
  public void setForPreparedStatement(boolean forPreparedStatement) {
    this.forPreparedStatement = forPreparedStatement;
  }
  
  public void setParameter(String nm, Object val) {
    if (this.statementParameters == null) {
      this.statementParameters = new java.util.HashMap();
    }
    this.statementParameters.put(nm, val);
  }
  
  public Map<String, Object> getStatementParameters() {
    if (this.statementParameters == null) {
      this.statementParameters = new java.util.HashMap();
    }
    return this.statementParameters;
  }
  
  public void clearPatameters() {
    if (this.statementParameters != null) {
      this.statementParameters.clear();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\exp\ExpressionContextBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */