package org.takeback.util;

import java.math.RoundingMode;
import java.text.NumberFormat;
import org.takeback.util.converter.ConversionUtils;

public class ConvertUtil
{
  public static String toFixed(double f, int digists)
  {
    return String.format("%." + digists + "f", new Object[] { Double.valueOf(f) });
  }
  
  public static String toMoney(double f)
  {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(java.util.Locale.CHINA);
    formatter.setRoundingMode(RoundingMode.HALF_UP);
    return formatter.format(f);
  }
  
  public static String dateToString(Object source) {
    return (String)ConversionUtils.convert(source, String.class);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\ConvertUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */