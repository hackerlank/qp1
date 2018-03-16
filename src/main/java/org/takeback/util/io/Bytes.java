package org.takeback.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.InflaterInputStream;





public class Bytes
{
  private static final String C64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
  private static final char[] BASE16 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  private static final char[] BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
  private static final int MASK4 = 15;
  private static final int MASK6 = 63;
  private static final int MASK8 = 255;
  private static final Map<Integer, byte[]> DECODE_TABLE_MAP = new ConcurrentHashMap();
  
  private static ThreadLocal<MessageDigest> MD = new ThreadLocal();
  








  public static byte[] copyOf(byte[] src, int length)
  {
    byte[] dest = new byte[length];
    System.arraycopy(src, 0, dest, 0, Math.min(src.length, length));
    return dest;
  }
  






  public static byte[] short2bytes(short v)
  {
    byte[] ret = { 0, 0 };
    short2bytes(v, ret);
    return ret;
  }
  







  public static void short2bytes(short v, byte[] b)
  {
    short2bytes(v, b, 0);
  }
  







  public static void short2bytes(short v, byte[] b, int off)
  {
    b[(off + 1)] = ((byte)v);
    b[(off + 0)] = ((byte)(v >>> 8));
  }
  






  public static byte[] int2bytes(int v)
  {
    byte[] ret = { 0, 0, 0, 0 };
    int2bytes(v, ret);
    return ret;
  }
  







  public static void int2bytes(int v, byte[] b)
  {
    int2bytes(v, b, 0);
  }
  









  public static void int2bytes(int v, byte[] b, int off)
  {
    b[(off + 3)] = ((byte)v);
    b[(off + 2)] = ((byte)(v >>> 8));
    b[(off + 1)] = ((byte)(v >>> 16));
    b[(off + 0)] = ((byte)(v >>> 24));
  }
  






  public static byte[] float2bytes(float v)
  {
    byte[] ret = { 0, 0, 0, 0 };
    float2bytes(v, ret);
    return ret;
  }
  







  public static void float2bytes(float v, byte[] b)
  {
    float2bytes(v, b, 0);
  }
  









  public static void float2bytes(float v, byte[] b, int off)
  {
    int i = Float.floatToIntBits(v);
    b[(off + 3)] = ((byte)i);
    b[(off + 2)] = ((byte)(i >>> 8));
    b[(off + 1)] = ((byte)(i >>> 16));
    b[(off + 0)] = ((byte)(i >>> 24));
  }
  






  public static byte[] long2bytes(long v)
  {
    byte[] ret = { 0, 0, 0, 0, 0, 0, 0, 0 };
    long2bytes(v, ret);
    return ret;
  }
  







  public static void long2bytes(long v, byte[] b)
  {
    long2bytes(v, b, 0);
  }
  









  public static void long2bytes(long v, byte[] b, int off)
  {
    b[(off + 7)] = ((byte)(int)v);
    b[(off + 6)] = ((byte)(int)(v >>> 8));
    b[(off + 5)] = ((byte)(int)(v >>> 16));
    b[(off + 4)] = ((byte)(int)(v >>> 24));
    b[(off + 3)] = ((byte)(int)(v >>> 32));
    b[(off + 2)] = ((byte)(int)(v >>> 40));
    b[(off + 1)] = ((byte)(int)(v >>> 48));
    b[(off + 0)] = ((byte)(int)(v >>> 56));
  }
  






  public static byte[] double2bytes(double v)
  {
    byte[] ret = { 0, 0, 0, 0, 0, 0, 0, 0 };
    double2bytes(v, ret);
    return ret;
  }
  







  public static void double2bytes(double v, byte[] b)
  {
    double2bytes(v, b, 0);
  }
  









  public static void double2bytes(double v, byte[] b, int off)
  {
    long j = Double.doubleToLongBits(v);
    b[(off + 7)] = ((byte)(int)j);
    b[(off + 6)] = ((byte)(int)(j >>> 8));
    b[(off + 5)] = ((byte)(int)(j >>> 16));
    b[(off + 4)] = ((byte)(int)(j >>> 24));
    b[(off + 3)] = ((byte)(int)(j >>> 32));
    b[(off + 2)] = ((byte)(int)(j >>> 40));
    b[(off + 1)] = ((byte)(int)(j >>> 48));
    b[(off + 0)] = ((byte)(int)(j >>> 56));
  }
  






  public static short bytes2short(byte[] b)
  {
    return bytes2short(b, 0);
  }
  








  public static short bytes2short(byte[] b, int off)
  {
    return (short)(((b[(off + 1)] & 0xFF) << 0) + (b[(off + 0)] << 8));
  }
  






  public static int bytes2int(byte[] b)
  {
    return bytes2int(b, 0);
  }
  








  public static int bytes2int(byte[] b, int off)
  {
    return ((b[(off + 3)] & 0xFF) << 0) + ((b[(off + 2)] & 0xFF) << 8) + ((b[(off + 1)] & 0xFF) << 16) + (b[(off + 0)] << 24);
  }
  







  public static float bytes2float(byte[] b)
  {
    return bytes2float(b, 0);
  }
  








  public static float bytes2float(byte[] b, int off)
  {
    int i = ((b[(off + 3)] & 0xFF) << 0) + ((b[(off + 2)] & 0xFF) << 8) + ((b[(off + 1)] & 0xFF) << 16) + (b[(off + 0)] << 24);
    
    return Float.intBitsToFloat(i);
  }
  






  public static long bytes2long(byte[] b)
  {
    return bytes2long(b, 0);
  }
  








  public static long bytes2long(byte[] b, int off)
  {
    return ((b[(off + 7)] & 0xFF) << 0) + ((b[(off + 6)] & 0xFF) << 8) + ((b[(off + 5)] & 0xFF) << 16) + ((b[(off + 4)] & 0xFF) << 24) + ((b[(off + 3)] & 0xFF) << 32) + ((b[(off + 2)] & 0xFF) << 40) + ((b[(off + 1)] & 0xFF) << 48) + (b[(off + 0)] << 56);
  }
  








  public static double bytes2double(byte[] b)
  {
    return bytes2double(b, 0);
  }
  








  public static double bytes2double(byte[] b, int off)
  {
    long j = ((b[(off + 7)] & 0xFF) << 0) + ((b[(off + 6)] & 0xFF) << 8) + ((b[(off + 5)] & 0xFF) << 16) + ((b[(off + 4)] & 0xFF) << 24) + ((b[(off + 3)] & 0xFF) << 32) + ((b[(off + 2)] & 0xFF) << 40) + ((b[(off + 1)] & 0xFF) << 48) + (b[(off + 0)] << 56);
    

    return Double.longBitsToDouble(j);
  }
  






  public static String bytes2hex(byte[] bs)
  {
    return bytes2hex(bs, 0, bs.length);
  }
  










  public static String bytes2hex(byte[] bs, int off, int len)
  {
    if (off < 0)
      throw new IndexOutOfBoundsException("bytes2hex: offset < 0, offset is " + off);
    if (len < 0)
      throw new IndexOutOfBoundsException("bytes2hex: length < 0, length is " + len);
    if (off + len > bs.length) {
      throw new IndexOutOfBoundsException("bytes2hex: offset + length > array length.");
    }
    
    int r = off;int w = 0;
    char[] cs = new char[len * 2];
    for (int i = 0; i < len; i++) {
      byte b = bs[(r++)];
      cs[(w++)] = BASE16[(b >> 4 & 0xF)];
      cs[(w++)] = BASE16[(b & 0xF)];
    }
    return new String(cs);
  }
  






  public static byte[] hex2bytes(String str)
  {
    return hex2bytes(str, 0, str.length());
  }
  










  public static byte[] hex2bytes(String str, int off, int len)
  {
    if ((len & 0x1) == 1) {
      throw new IllegalArgumentException("hex2bytes: ( len & 1 ) == 1.");
    }
    if (off < 0)
      throw new IndexOutOfBoundsException("hex2bytes: offset < 0, offset is " + off);
    if (len < 0)
      throw new IndexOutOfBoundsException("hex2bytes: length < 0, length is " + len);
    if (off + len > str.length()) {
      throw new IndexOutOfBoundsException("hex2bytes: offset + length > array length.");
    }
    int num = len / 2;int r = off;int w = 0;
    byte[] b = new byte[num];
    for (int i = 0; i < num; i++)
      b[(w++)] = ((byte)(hex(str.charAt(r++)) << 4 | hex(str.charAt(r++))));
    return b;
  }
  






  public static String bytes2base64(byte[] b)
  {
    return bytes2base64(b, 0, b.length, BASE64);
  }
  






  public static String bytes2base64(byte[] b, int offset, int length)
  {
    return bytes2base64(b, offset, length, BASE64);
  }
  








  public static String bytes2base64(byte[] b, String code)
  {
    return bytes2base64(b, 0, b.length, code);
  }
  








  public static String bytes2base64(byte[] b, int offset, int length, String code)
  {
    if (code.length() < 64) {
      throw new IllegalArgumentException("Base64 code length < 64.");
    }
    return bytes2base64(b, offset, length, code.toCharArray());
  }
  








  public static String bytes2base64(byte[] b, char[] code)
  {
    return bytes2base64(b, 0, b.length, code);
  }
  












  public static String bytes2base64(byte[] bs, int off, int len, char[] code)
  {
    if (off < 0)
      throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off);
    if (len < 0)
      throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len);
    if (off + len > bs.length) {
      throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");
    }
    if (code.length < 64) {
      throw new IllegalArgumentException("Base64 code length < 64.");
    }
    boolean pad = code.length > 64;
    int num = len / 3;int rem = len % 3;int r = off;int w = 0;
    char[] cs = new char[num * 4 + (pad ? 4 : rem == 0 ? 0 : rem + 1)];
    
    for (int i = 0; i < num; i++) {
      int b1 = bs[(r++)] & 0xFF;int b2 = bs[(r++)] & 0xFF;int b3 = bs[(r++)] & 0xFF;
      
      cs[(w++)] = code[(b1 >> 2)];
      cs[(w++)] = code[(b1 << 4 & 0x3F | b2 >> 4)];
      cs[(w++)] = code[(b2 << 2 & 0x3F | b3 >> 6)];
      cs[(w++)] = code[(b3 & 0x3F)];
    }
    
    if (rem == 1) {
      int b1 = bs[(r++)] & 0xFF;
      cs[(w++)] = code[(b1 >> 2)];
      cs[(w++)] = code[(b1 << 4 & 0x3F)];
      if (pad) {
        cs[(w++)] = code[64];
        cs[(w++)] = code[64];
      }
    } else if (rem == 2) {
      int b1 = bs[(r++)] & 0xFF;int b2 = bs[(r++)] & 0xFF;
      cs[(w++)] = code[(b1 >> 2)];
      cs[(w++)] = code[(b1 << 4 & 0x3F | b2 >> 4)];
      cs[(w++)] = code[(b2 << 2 & 0x3F)];
      if (pad)
        cs[(w++)] = code[64];
    }
    return new String(cs);
  }
  






  public static byte[] base642bytes(String str)
  {
    return base642bytes(str, 0, str.length());
  }
  










  public static byte[] base642bytes(String str, int offset, int length)
  {
    return base642bytes(str, offset, length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
  }
  








  public static byte[] base642bytes(String str, String code)
  {
    return base642bytes(str, 0, str.length(), code);
  }
  












  public static byte[] base642bytes(String str, int off, int len, String code)
  {
    if (off < 0)
      throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
    if (len < 0)
      throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
    if (off + len > str.length()) {
      throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
    }
    if (code.length() < 64) {
      throw new IllegalArgumentException("Base64 code length < 64.");
    }
    int rem = len % 4;
    if (rem == 1) {
      throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
    }
    int num = len / 4;int size = num * 3;
    if (code.length() > 64) {
      if (rem != 0) {
        throw new IllegalArgumentException("base642bytes: base64 string length error.");
      }
      char pc = code.charAt(64);
      if (str.charAt(off + len - 2) == pc) {
        size -= 2;
        num--;
        rem = 2;
      } else if (str.charAt(off + len - 1) == pc) {
        size--;
        num--;
        rem = 3;
      }
    }
    else if (rem == 2) {
      size++;
    } else if (rem == 3) {
      size += 2;
    }
    
    int r = off;int w = 0;
    byte[] b = new byte[size];byte[] t = decodeTable(code);
    for (int i = 0; i < num; i++) {
      int c1 = t[str.charAt(r++)];int c2 = t[str.charAt(r++)];
      int c3 = t[str.charAt(r++)];int c4 = t[str.charAt(r++)];
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
      b[(w++)] = ((byte)(c2 << 4 | c3 >> 2));
      b[(w++)] = ((byte)(c3 << 6 | c4));
    }
    
    if (rem == 2) {
      int c1 = t[str.charAt(r++)];int c2 = t[str.charAt(r++)];
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
    } else if (rem == 3) {
      int c1 = t[str.charAt(r++)];int c2 = t[str.charAt(r++)];int c3 = t[str.charAt(r++)];
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
      b[(w++)] = ((byte)(c2 << 4 | c3 >> 2));
    }
    return b;
  }
  








  public static byte[] base642bytes(String str, char[] code)
  {
    return base642bytes(str, 0, str.length(), code);
  }
  












  public static byte[] base642bytes(String str, int off, int len, char[] code)
  {
    if (off < 0)
      throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
    if (len < 0)
      throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
    if (off + len > str.length()) {
      throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
    }
    if (code.length < 64) {
      throw new IllegalArgumentException("Base64 code length < 64.");
    }
    int rem = len % 4;
    if (rem == 1) {
      throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
    }
    int num = len / 4;int size = num * 3;
    if (code.length > 64) {
      if (rem != 0) {
        throw new IllegalArgumentException("base642bytes: base64 string length error.");
      }
      char pc = code[64];
      if (str.charAt(off + len - 2) == pc) {
        size -= 2;
      } else if (str.charAt(off + len - 1) == pc) {
        size--;
      }
    } else if (rem == 2) {
      size++;
    } else if (rem == 3) {
      size += 2;
    }
    
    int r = off;int w = 0;
    byte[] b = new byte[size];
    for (int i = 0; i < num; i++) {
      int c1 = indexOf(code, str.charAt(r++));int c2 = indexOf(code, str.charAt(r++));
      int c3 = indexOf(code, str.charAt(r++));int c4 = indexOf(code, str.charAt(r++));
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
      b[(w++)] = ((byte)(c2 << 4 | c3 >> 2));
      b[(w++)] = ((byte)(c3 << 6 | c4));
    }
    
    if (rem == 2) {
      int c1 = indexOf(code, str.charAt(r++));int c2 = indexOf(code, str.charAt(r++));
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
    } else if (rem == 3) {
      int c1 = indexOf(code, str.charAt(r++));int c2 = indexOf(code, str.charAt(r++));int c3 = indexOf(code, str
        .charAt(r++));
      
      b[(w++)] = ((byte)(c1 << 2 | c2 >> 4));
      b[(w++)] = ((byte)(c2 << 4 | c3 >> 2));
    }
    return b;
  }
  
  /* Error */
  public static byte[] zip(byte[] bytes)
    throws IOException
  {
    // Byte code:
    //   0: new 68	org/takeback/util/io/UnsafeByteArrayOutputStream
    //   3: dup
    //   4: invokespecial 69	org/takeback/util/io/UnsafeByteArrayOutputStream:<init>	()V
    //   7: astore_1
    //   8: new 70	java/util/zip/DeflaterOutputStream
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 71	java/util/zip/DeflaterOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   16: astore_2
    //   17: aload_2
    //   18: aload_0
    //   19: invokevirtual 72	java/io/OutputStream:write	([B)V
    //   22: aload_2
    //   23: invokevirtual 73	java/io/OutputStream:close	()V
    //   26: aload_1
    //   27: invokevirtual 74	org/takeback/util/io/UnsafeByteArrayOutputStream:close	()V
    //   30: goto +14 -> 44
    //   33: astore_3
    //   34: aload_2
    //   35: invokevirtual 73	java/io/OutputStream:close	()V
    //   38: aload_1
    //   39: invokevirtual 74	org/takeback/util/io/UnsafeByteArrayOutputStream:close	()V
    //   42: aload_3
    //   43: athrow
    //   44: aload_1
    //   45: invokevirtual 75	org/takeback/util/io/UnsafeByteArrayOutputStream:toByteArray	()[B
    //   48: areturn
    // Line number table:
    //   Java source line #795	-> byte code offset #0
    //   Java source line #796	-> byte code offset #8
    //   Java source line #798	-> byte code offset #17
    //   Java source line #800	-> byte code offset #22
    //   Java source line #801	-> byte code offset #26
    //   Java source line #802	-> byte code offset #30
    //   Java source line #800	-> byte code offset #33
    //   Java source line #801	-> byte code offset #38
    //   Java source line #803	-> byte code offset #44
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	49	0	bytes	byte[]
    //   7	38	1	bos	UnsafeByteArrayOutputStream
    //   16	19	2	os	java.io.OutputStream
    //   33	10	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   17	22	33	finally
  }
  
  public static byte[] unzip(byte[] bytes)
    throws IOException
  {
    UnsafeByteArrayInputStream bis = new UnsafeByteArrayInputStream(bytes);
    UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
    InputStream is = new InflaterInputStream(bis);
    try {
      IOUtils.write(is, bos);
      return bos.toByteArray();
    } finally {
      is.close();
      bis.close();
      bos.close();
    }
  }
  






  public static byte[] getMD5(String str)
  {
    return getMD5(str.getBytes());
  }
  






  public static byte[] getMD5(byte[] source)
  {
    MessageDigest md = getMessageDigest();
    return md.digest(source);
  }
  





  public static byte[] getMD5(File file)
    throws IOException
  {
    InputStream is = new FileInputStream(file);
    try {
      return getMD5(is);
    } finally {
      is.close();
    }
  }
  





  public static byte[] getMD5(InputStream is)
    throws IOException
  {
    return getMD5(is, 8192);
  }
  
  private static byte hex(char c) {
    if (c <= '9')
      return (byte)(c - '0');
    if ((c >= 'a') && (c <= 'f'))
      return (byte)(c - 'a' + 10);
    if ((c >= 'A') && (c <= 'F'))
      return (byte)(c - 'A' + 10);
    throw new IllegalArgumentException("hex string format error [" + c + "].");
  }
  
  private static int indexOf(char[] cs, char c) {
    int i = 0; for (int len = cs.length; i < len; i++)
      if (cs[i] == c)
        return i;
    return -1;
  }
  
  private static byte[] decodeTable(String code) {
    int hash = code.hashCode();
    byte[] ret = (byte[])DECODE_TABLE_MAP.get(Integer.valueOf(hash));
    if (ret == null) {
      if (code.length() < 64) {
        throw new IllegalArgumentException("Base64 code length < 64.");
      }
      ret = new byte[''];
      for (int i = 0; i < 128; i++)
      {
        ret[i] = -1; }
      for (int i = 0; i < 64; i++)
        ret[code.charAt(i)] = ((byte)i);
      DECODE_TABLE_MAP.put(Integer.valueOf(hash), ret);
    }
    return ret;
  }
  
  private static byte[] getMD5(InputStream is, int bs) throws IOException {
    MessageDigest md = getMessageDigest();
    byte[] buf = new byte[bs];
    while (is.available() > 0) {
      int total = 0;
      do { int read;
        if ((read = is.read(buf, total, bs - total)) <= 0)
          break;
        total += read;
      } while (total < bs);
      md.update(buf);
    }
    return md.digest();
  }
  
  private static MessageDigest getMessageDigest() {
    MessageDigest ret = (MessageDigest)MD.get();
    if (ret == null) {
      try {
        ret = MessageDigest.getInstance("MD5");
        MD.set(ret);
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }
  
  public static void main(String[] args)
    throws UnsupportedEncodingException, IOException
  {}
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\io\Bytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */