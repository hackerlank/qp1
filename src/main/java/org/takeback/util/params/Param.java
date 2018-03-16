package org.takeback.util.params;

public class Param
{
  private Long id;
  private String paramname;
  private String paramvalue;
  private String paramalias;
  private String remark;
  
  public Param() {}
  
  public Param(String name, String value) {
    this.paramname = name;
    this.paramvalue = value;
  }
  
  public Param(String name, String value, String description) {
    this.paramname = name;
    this.paramvalue = value;
    this.remark = description;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getParamname() {
    return this.paramname;
  }
  
  public void setParamname(String paramname) {
    this.paramname = paramname;
  }
  
  public String getParamvalue() {
    return this.paramvalue;
  }
  
  public void setParamvalue(String paramvalue) {
    this.paramvalue = paramvalue;
  }
  
  public String getParamalias() {
    return this.paramalias;
  }
  
  public void setParamalias(String paramalias) {
    this.paramalias = paramalias;
  }
  
  public String getRemark() {
    return this.remark;
  }
  
  public void setRemark(String remark) {
    this.remark = remark;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\params\Param.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */