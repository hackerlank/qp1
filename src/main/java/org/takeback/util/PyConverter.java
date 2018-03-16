package org.takeback.util;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

public class PyConverter
{
  public static String getFirstLetter(String s)
  {
    if (StringUtils.isEmpty(s)) {
      return null;
    }
    return PinyinHelper.getShortPinyin(s);
  }
  
  public static String getPinYinWithoutTone(String s) {
    return PinyinHelper.convertToPinyinString(s, "", com.github.stuxuhai.jpinyin.PinyinFormat.WITHOUT_TONE);
  }
  
  public static String getPinYin(String s) {
    return PinyinHelper.convertToPinyinString(s, "");
  }
  
  public static void main(String[] args) {
    System.out.println(getPinYinWithoutTone("中华人民共和国"));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\PyConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */