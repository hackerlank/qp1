package org.takeback.dao;


public class Po
{
  private int pageSize;
  private int pageNo;
  private String order;
  
  public Po(int pageNo)
  {
    this.pageNo = pageNo;
  }
  
  public Po(int pageSize, int pageNo) {
    this.pageSize = pageSize;
    this.pageNo = pageNo;
  }
  
  public Po(int pageSize, int pageNo, String order) {
    this.pageSize = pageSize;
    this.pageNo = pageNo;
    this.order = order;
  }
  
  public Po(String order) {
    this.order = order;
  }
  
  public int getPageSize() {
    return this.pageSize;
  }
  
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
  public int getPageNo() {
    return this.pageNo;
  }
  
  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }
  
  public String getOrder() {
    return this.order;
  }
  
  public void setOrder(String order) {
    this.order = order;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\dao\Po.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */