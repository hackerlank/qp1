package org.takeback.util.valid;

import java.io.PrintStream;




public class BankCardUtils
{
  public static boolean checkBankCard(String cardId)
  {
    char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
    if (bit == 'N') {
      return false;
    }
    return cardId.charAt(cardId.length() - 1) == bit;
  }
  





  public static char getBankCardCheckCode(String nonCheckCodeCardId)
  {
    if ((nonCheckCodeCardId == null) || (nonCheckCodeCardId.trim().length() == 0) || 
      (!nonCheckCodeCardId.matches("\\d+")))
    {
      return 'N';
    }
    char[] chs = nonCheckCodeCardId.trim().toCharArray();
    int luhmSum = 0;
    int i = chs.length - 1; for (int j = 0; i >= 0; j++) {
      int k = chs[i] - '0';
      if (j % 2 == 0) {
        k *= 2;
        k = k / 10 + k % 10;
      }
      luhmSum += k;i--;
    }
    return luhmSum % 10 == 0 ? '0' : (char)(10 - luhmSum % 10 + 48);
  }
  
  public static void main(String[] args) {
    System.out.println(checkBankCard("6225881414207430"));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\valid\BankCardUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */