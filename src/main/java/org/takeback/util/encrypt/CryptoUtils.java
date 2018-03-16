package org.takeback.util.encrypt;

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class CryptoUtils
{
  private static int saltSize = 32;
  private static int iterations = 1000;
  private static int subKeySize = 32;
  




  public static String getSalt()
  {
    return Rfc2898DeriveBytes.generateSalt(saltSize);
  }
  






  public static String getHash(String password, String salt)
  {
    Rfc2898DeriveBytes keyGenerator = null;
    try {
      keyGenerator = new Rfc2898DeriveBytes(password + salt, saltSize, iterations);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    byte[] subKey = keyGenerator.getBytes(subKeySize);
    byte[] bSalt = keyGenerator.getSalt();
    byte[] hashPassword = new byte[1 + saltSize + subKeySize];
    System.arraycopy(bSalt, 0, hashPassword, 1, saltSize);
    System.arraycopy(subKey, 0, hashPassword, saltSize + 1, subKeySize);
    return Base64.encodeBase64String(hashPassword);
  }
  







  public static boolean verify(String hashedPassword, String password, String salt)
  {
    byte[] hashedPasswordBytes = Base64.decodeBase64(hashedPassword);
    if ((hashedPasswordBytes.length != 1 + saltSize + subKeySize) || (hashedPasswordBytes[0] != 0)) {
      return false;
    }
    
    byte[] bSalt = new byte[saltSize];
    System.arraycopy(hashedPasswordBytes, 1, bSalt, 0, saltSize);
    byte[] storedSubkey = new byte[subKeySize];
    System.arraycopy(hashedPasswordBytes, 1 + saltSize, storedSubkey, 0, subKeySize);
    Rfc2898DeriveBytes deriveBytes = null;
    try {
      deriveBytes = new Rfc2898DeriveBytes(password + salt, bSalt, iterations);
    } catch (Exception e) {
      e.printStackTrace();
    }
    byte[] generatedSubkey = deriveBytes.getBytes(subKeySize);
    return byteArraysEqual(storedSubkey, generatedSubkey);
  }
  
  private static boolean byteArraysEqual(byte[] storedSubkey, byte[] generatedSubkey) {
    int size = storedSubkey.length;
    if (size != generatedSubkey.length) {
      return false;
    }
    
    for (int i = 0; i < size; i++) {
      if (storedSubkey[i] != generatedSubkey[i]) {
        return false;
      }
    }
    return true;
  }
  
  public static void main(String[] args) throws NoSuchAlgorithmException {
    String salt = getSalt();
    String password = "admin123";
    String hashPassword = getHash(password, salt);
    System.out.println("hashPassword:" + hashPassword);
    System.out.println("salt:" + salt);
    System.out.println("password:" + password);
    
    boolean result = verify(hashPassword, password, salt);
    System.out.println("Verify:" + result);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\encrypt\CryptoUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */