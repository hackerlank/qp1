package org.takeback.chat.service.lotteryGame;


public class How
{
  private Double num;
  
  private String luckyNum;
  

  public How(Double num)
  {
    this.num = num;
  }
  
  public How(String luckyNum) {
    this.luckyNum = luckyNum;
  }
  
  public How(Double num, String luckyNum) {
    this.luckyNum = luckyNum;
    this.num = num;
  }
  
  public String getLuckyNum() {
    return this.luckyNum;
  }
  
  public void setLuckyNum(String luckyNum) {
    this.luckyNum = luckyNum;
  }
  
  public Double getNum() {
    return this.num;
  }
  
  public void setNum(Double num) {
    this.num = num;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\lotteryGame\How.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */