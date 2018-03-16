package org.takeback.chat.service.lotteryGame;

import org.takeback.util.params.ParamUtils;



public class Game100
  implements IGame
{
  private String luckyNumber;
  private String[] arr;
  String pos4_5;
  Double rate3 = Double.valueOf(ParamUtils.getParam("game_100_6"));
  Double rate2 = Double.valueOf(ParamUtils.getParam("game_100_5"));
  Double rate1 = Double.valueOf(ParamUtils.getParam("game_100_1"));
  
  public Game100(String luckyNumber) { this.luckyNumber = luckyNumber;
    this.arr = luckyNumber.split(",");
    this.pos4_5 = (this.arr[3] + this.arr[4]);
  }
  
  public How how(String realBet)
  {
    if (!"6".equals(realBet))
    {
      if (!"5".equals(realBet))
      {
        if ("4".equals(realBet)) {
          Integer dx = Integer.valueOf(this.pos4_5);
          if (dx.intValue() < 50) {
            return new How(this.rate1, realBet);
          }
          return new How(Double.valueOf(-1.0D)); }
        if ("3".equals(realBet)) {
          Integer dx = Integer.valueOf(this.pos4_5);
          if (dx.intValue() > 49) {
            return new How(this.rate1, realBet);
          }
          return new How(Double.valueOf(-1.0D)); }
        if ("2".equals(realBet)) {
          Integer ds = Integer.valueOf(this.arr[4]);
          if (ds.intValue() % 2 == 0) {
            return new How(this.rate1, realBet);
          }
          return new How(Double.valueOf(-1.0D)); }
        if ("1".equals(realBet)) {
          Integer ds = Integer.valueOf(this.arr[4]);
          if (ds.intValue() % 2 != 0) {
            return new How(this.rate1, realBet);
          }
          return new How(Double.valueOf(-1.0D));
        } } }
    return new How(Double.valueOf(-1.0D));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\lotteryGame\Game100.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */