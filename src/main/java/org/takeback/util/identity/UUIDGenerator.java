package org.takeback.util.identity;

import java.io.PrintStream;

public class UUIDGenerator
{
  public static String get() {
    String uuid = java.util.UUID.randomUUID().toString();
    return uuid.replace("-", "");
  }
  
  public static String[] gets(int length) {
    String[] uuids = new String[length];
    for (int i = 0; i < length; i++) {
      uuids[i] = get();
    }
    return uuids;
  }
  
  public static void main(String[] args) {
    System.out.println(gets(3)[0]);
    System.out.println(gets(3)[1]);
    System.out.println(gets(3)[2]);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\identity\UUIDGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */