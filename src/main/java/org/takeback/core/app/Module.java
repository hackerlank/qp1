package org.takeback.core.app;

import org.takeback.util.converter.ConversionUtils;

public class Module extends ApplicationNode
{
  private static final long serialVersionUID = -5866496056614584396L;
  private String script;
  private String implement;
  private Boolean runAsWindow;
  
  public java.util.List<Action> getActions()
  {
    if (this.deep >= getRequestDeep()) {
      return null;
    }
    return super.getItems();
  }
  
  public String getScript() {
    return this.script;
  }
  
  public void setScript(String script) {
    this.script = script;
  }
  
  public <T> T getProperty(String nm, Class<T> targetType) {
    return (T)ConversionUtils.convert(getProperty(nm), targetType);
  }
  
  public String getImplement() {
    return this.implement;
  }
  
  public void setImplement(String implement) {
    this.implement = implement;
  }
  
  public Boolean isRunAsWindow() {
    return this.runAsWindow;
  }
  
  public void setRunAsWindow(boolean runAsWindow) {
    this.runAsWindow = Boolean.valueOf(runAsWindow);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\app\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */