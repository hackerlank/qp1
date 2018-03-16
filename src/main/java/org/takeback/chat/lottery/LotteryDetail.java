package org.takeback.chat.lottery;

import java.math.BigDecimal;

public class LotteryDetail
{
  private Integer uid;
  private BigDecimal coin;
  private java.util.Date createDate;
  
  public LotteryDetail(Integer uid, BigDecimal coin)
  {
    this.uid = uid;
    this.coin = coin;
    this.createDate = new java.util.Date();
  }
  
  public java.util.Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }
  
  public Integer getUid() {
    return this.uid;
  }
  
  public void setUid(Integer uid) {
    this.uid = uid;
  }
  
  public BigDecimal getCoin() {
    return this.coin.setScale(2, 4);
  }
  
  public void setCoin(BigDecimal coin) {
    this.coin = coin;
  }
  
  public String toString() {
    return this.uid + "-" + this.coin;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\LotteryDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */