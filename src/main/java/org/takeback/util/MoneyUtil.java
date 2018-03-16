package org.takeback.util;

import java.io.PrintStream;



public class MoneyUtil
{
  private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
  

  private static final String[] IUNIT = { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };
  

  private static final String[] DUNIT = { "角", "分", "厘" };
  


  public static String toChinese(String str)
  {
    str = str.replaceAll(",", "");
    
    String decimalStr;
    String integerStr;
    String decimalStr;
    if (str.indexOf(".") > 0) {
      String integerStr = str.substring(0, str.indexOf("."));
      decimalStr = str.substring(str.indexOf(".") + 1); } else { String decimalStr;
      if (str.indexOf(".") == 0) {
        String integerStr = "";
        decimalStr = str.substring(1);
      } else {
        integerStr = str;
        decimalStr = "";
      }
    }
    if (!integerStr.equals("")) {
      integerStr = Long.toString(Long.parseLong(integerStr));
      if (integerStr.equals("0")) {
        integerStr = "";
      }
    }
    
    if (integerStr.length() > IUNIT.length) {
      System.out.println(str + ":超出处理能力");
      return str;
    }
    
    int[] integers = toArray(integerStr);
    boolean isMust5 = isMust5(integerStr);
    int[] decimals = toArray(decimalStr);
    return getChineseInteger(integers, isMust5) + getChineseDecimal(decimals);
  }
  


  private static int[] toArray(String number)
  {
    int[] array = new int[number.length()];
    for (int i = 0; i < number.length(); i++) {
      array[i] = Integer.parseInt(number.substring(i, i + 1));
    }
    return array;
  }
  


  private static String getChineseInteger(int[] integers, boolean isMust5)
  {
    StringBuffer chineseInteger = new StringBuffer("");
    int length = integers.length;
    for (int i = 0; i < length; i++)
    {

      String key = "";
      if (integers[i] == 0) {
        if (length - i == 13) {
          key = IUNIT[4];
        } else if (length - i == 9) {
          key = IUNIT[8];
        } else if ((length - i == 5) && (isMust5)) {
          key = IUNIT[4];
        } else if (length - i == 1) {
          key = IUNIT[0];
        }
        if ((length - i > 1) && (integers[(i + 1)] != 0))
          key = key + NUMBERS[0];
      }
      chineseInteger.append(NUMBERS[integers[i]] + IUNIT[(length - i - 1)]);
    }
    
    return chineseInteger.toString();
  }
  


  private static String getChineseDecimal(int[] decimals)
  {
    StringBuffer chineseDecimal = new StringBuffer("");
    for (int i = 0; i < decimals.length; i++)
    {
      if (i == 3)
        break;
      chineseDecimal.append(NUMBERS[decimals[i]] + DUNIT[i]);
    }
    
    return chineseDecimal.toString();
  }
  


  private static boolean isMust5(String integerStr)
  {
    int length = integerStr.length();
    if (length > 4) {
      String subInteger = "";
      if (length > 8)
      {
        subInteger = integerStr.substring(length - 8, length - 4);
      } else {
        subInteger = integerStr.substring(0, length - 4);
      }
      return Integer.parseInt(subInteger) > 0;
    }
    return false;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\MoneyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */