package org.takeback.util;

import java.math.BigDecimal;





















public class ArithUtil
{
  private static final int DEF_DIV_SCALE = 10;
  
  public static double add(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.add(b2).doubleValue();
  }
  
  public static double adds(double... params) {
    BigDecimal b1 = new BigDecimal(Double.toString(params[0]));
    for (int i = 1; i < params.length; i++) {
      b1 = b1.add(new BigDecimal(Double.toString(params[i])));
    }
    return b1.doubleValue();
  }
  










  public static double sub(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.subtract(b2).doubleValue();
  }
  
  public static double subs(double... params)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(params[0]));
    for (int i = 1; i < params.length; i++) {
      b1 = b1.subtract(new BigDecimal(Double.toString(params[i])));
    }
    return b1.doubleValue();
  }
  










  public static double mul(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.multiply(b2).doubleValue();
  }
  










  public static double div(double v1, double v2)
  {
    return div(v1, v2, 10);
  }
  












  public static double div(double v1, double v2, int scale)
  {
    if (scale < 0)
    {
      throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.divide(b2, scale, 4).doubleValue();
  }
  










  public static double round(double v, int scale)
  {
    if (scale < 0)
    {
      throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
    }
    BigDecimal b = new BigDecimal(Double.toString(v));
    BigDecimal one = new BigDecimal("1");
    return b.divide(one, scale, 4).doubleValue();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\ArithUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */