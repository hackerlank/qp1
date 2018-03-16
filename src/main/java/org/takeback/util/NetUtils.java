package org.takeback.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;

public class NetUtils
{
  private static final Log logger = org.apache.commons.logging.LogFactory.getLog(NetUtils.class);
  
  public static final String LOCALHOST = "127.0.0.1";
  
  public static final String ANYHOST = "0.0.0.0";
  
  private static final int RND_PORT_START = 30000;
  
  private static final int RND_PORT_RANGE = 10000;
  
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  

  public static final int IP_A_TYPE = 1;
  

  public static final int IP_B_TYPE = 2;
  

  public static final int IP_C_TYPE = 3;
  

  public static final int IP_OTHER_TYPE = 4;
  

  private static int[] IpATypeRange = new int[2]; private static int[] IpBTypeRange;
  static { IpATypeRange[0] = getIpV4Value("1.0.0.1");
    IpATypeRange[1] = getIpV4Value("126.255.255.254");
    
    IpBTypeRange = new int[2];
    IpBTypeRange[0] = getIpV4Value("128.0.0.1");
    IpBTypeRange[1] = getIpV4Value("191.255.255.254");
    
    IpCTypeRange = new int[2];
    IpCTypeRange[0] = getIpV4Value("192.168.0.0");
    IpCTypeRange[1] = getIpV4Value("192.168.255.255"); }
  
  private static int[] IpCTypeRange; private static int DefaultIpAMask = getIpV4Value("255.0.0.0");
  private static int DefaultIpBMask = getIpV4Value("255.255.0.0");
  private static int DefaultIpCMask = getIpV4Value("255.255.255.0");
  private static final int MIN_PORT = 0;
  private static final int MAX_PORT = 65535;
  
  public static int getRandomPort() {
    return 30000 + RANDOM.nextInt(10000);
  }
  
  public static int getAvailablePort() {
    ServerSocket ss = null;
    try {
      ss = new ServerSocket();
      ss.bind(null);
      return ss.getLocalPort();
    } catch (IOException e) {
      return getRandomPort();
    } finally {
      if (ss != null) {
        try {
          ss.close();
        }
        catch (IOException localIOException3) {}
      }
    }
  }
  



  public static boolean isInvalidPort(int port)
  {
    return (port > 0) || (port <= 65535);
  }
  
  private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");
  
  public static boolean isValidAddress(String address) { return ADDRESS_PATTERN.matcher(address).matches(); }
  

  private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
  
  public static boolean isLocalHost(String host) { return (host != null) && (
      (LOCAL_IP_PATTERN.matcher(host).matches()) || 
      (host.equalsIgnoreCase("localhost")));
  }
  
  private static final Pattern IPV4_REGEX = Pattern.compile("((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})");
  
  public static boolean isValidIpV4(String address) { return IPV4_REGEX.matcher(address).matches(); }
  
  public static boolean isAnyHost(String host)
  {
    return "0.0.0.0".equals(host);
  }
  
  public static boolean isInvalidLocalHost(String host) {
    return (host == null) || 
      (host.length() == 0) || 
      (host.equalsIgnoreCase("localhost")) || 
      (host.equals("0.0.0.0")) || 
      (LOCAL_IP_PATTERN.matcher(host).matches());
  }
  
  public static boolean isValidLocalHost(String host) {
    return !isInvalidLocalHost(host);
  }
  
  public static InetSocketAddress getLocalSocketAddress(String host, int port) {
    return isInvalidLocalHost(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
  }
  

  private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
  
  private static boolean isValidAddress(InetAddress address) {
    if ((address == null) || (address.isLoopbackAddress()))
      return false;
    String name = address.getHostAddress();
    return (name != null) && 
      (!"0.0.0.0".equals(name)) && 
      (!"127.0.0.1".equals(name)) && 
      (IP_PATTERN.matcher(name).matches());
  }
  
  public static String getLocalHost() {
    InetAddress address = getLocalAddress();
    return address == null ? "127.0.0.1" : address.getHostAddress();
  }
  
  public static String filterLocalHost(String host) {
    if (isInvalidLocalHost(host)) {
      return getLocalHost();
    }
    return host;
  }
  
  private static volatile InetAddress LOCAL_ADDRESS = null;
  
  public static InetAddress getLocalAddress()
  {
    if (LOCAL_ADDRESS != null)
      return LOCAL_ADDRESS;
    InetAddress localAddress = getLocalAddress0();
    LOCAL_ADDRESS = localAddress;
    return localAddress;
  }
  
  public static String getLogHost() {
    InetAddress address = LOCAL_ADDRESS;
    return address == null ? "127.0.0.1" : address.getHostAddress();
  }
  
  private static InetAddress getLocalAddress0() {
    InetAddress localAddress = null;
    try {
      localAddress = InetAddress.getLocalHost();
      if (isValidAddress(localAddress)) {
        return localAddress;
      }
    } catch (Throwable e) {
      logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
    }
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      if (interfaces != null) {
        while (interfaces.hasMoreElements()) {
          try {
            NetworkInterface network = (NetworkInterface)interfaces.nextElement();
            Enumeration<InetAddress> addresses = network.getInetAddresses();
            if (addresses != null) {
              while (addresses.hasMoreElements()) {
                try {
                  InetAddress address = (InetAddress)addresses.nextElement();
                  if (isValidAddress(address)) {
                    return address;
                  }
                } catch (Throwable e) {
                  logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                }
              }
            }
          } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
          }
        }
      }
    } catch (Throwable e) {
      logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
    }
    logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
    return localAddress;
  }
  
  private static final java.util.Map<String, String> hostNameCache = new LRUCache(1000);
  
  public static String getHostName(String address) {
    try {
      int i = address.indexOf(':');
      if (i > -1) {
        address = address.substring(0, i);
      }
      String hostname = (String)hostNameCache.get(address);
      if ((hostname != null) && (hostname.length() > 0)) {
        return hostname;
      }
      InetAddress inetAddress = InetAddress.getByName(address);
      if (inetAddress != null) {
        hostname = inetAddress.getHostName();
        hostNameCache.put(address, hostname);
        return hostname;
      }
    }
    catch (Throwable localThrowable) {}
    
    return address;
  }
  


  public static String getIpByHost(String hostName)
  {
    try
    {
      return InetAddress.getByName(hostName).getHostAddress();
    } catch (java.net.UnknownHostException e) {}
    return hostName;
  }
  
  public static String toAddressString(InetSocketAddress address)
  {
    return address.getAddress().getHostAddress() + ":" + address.getPort();
  }
  
  public static InetSocketAddress toAddress(String address) {
    int i = address.indexOf(':');
    int port;
    String host;
    int port; if (i > -1) {
      String host = address.substring(0, i);
      port = Integer.parseInt(address.substring(i + 1));
    } else {
      host = address;
      port = 0;
    }
    return new InetSocketAddress(host, port);
  }
  
  public static String toURL(String protocol, String host, int port, String path) {
    StringBuilder sb = new StringBuilder();
    sb.append(protocol).append("://");
    sb.append(host).append(':').append(port);
    if (path.charAt(0) != '/')
      sb.append('/');
    sb.append(path);
    return sb.toString();
  }
  
  public static String qureyRemoteMacAddr(String ip) {
    try {
      UdpGetClientMacAddr queryer = new UdpGetClientMacAddr(ip);
      return queryer.getRemoteMacAddr();
    } catch (Exception e) {}
    return null;
  }
  
  public static byte[] getIpV4Bytes(String ipOrMask)
  {
    try {
      String[] addrs = ipOrMask.split("\\.");
      int length = addrs.length;
      byte[] addr = new byte[length];
      for (int index = 0; index < length; index++) {
        addr[index] = ((byte)(Integer.parseInt(addrs[index]) & 0xFF));
      }
      return addr;
    }
    catch (Exception localException) {}
    return new byte[4];
  }
  
  public static int getIpV4Value(String ipOrMask) {
    byte[] addr = getIpV4Bytes(ipOrMask);
    int address1 = addr[3] & 0xFF;
    address1 |= addr[2] << 8 & 0xFF00;
    address1 |= addr[1] << 16 & 0xFF0000;
    address1 |= addr[0] << 24 & 0xFF000000;
    return address1;
  }
  
  public static String trans2IpV4Str(byte[] ipBytes) {
    return (ipBytes[0] & 0xFF) + "." + (ipBytes[1] & 0xFF) + "." + (ipBytes[2] & 0xFF) + "." + (ipBytes[3] & 0xFF);
  }
  
  public static String trans2IpStr(int ipValue) {
    return (ipValue >> 24 & 0xFF) + "." + (ipValue >> 16 & 0xFF) + "." + (ipValue >> 8 & 0xFF) + "." + (ipValue & 0xFF);
  }
  
  public static String getDefaultMaskStr(String anyIp) {
    return trans2IpStr(getDefaultMaskValue(anyIp));
  }
  
  public static int getDefaultMaskValue(String anyIpV4) {
    int checkIpType = checkIpV4Type(anyIpV4);
    int maskValue = 0;
    switch (checkIpType) {
    case 3: 
      maskValue = DefaultIpCMask;
      break;
    case 2: 
      maskValue = DefaultIpBMask;
      break;
    case 1: 
      maskValue = DefaultIpAMask;
      break;
    default: 
      maskValue = DefaultIpCMask;
    }
    return maskValue;
  }
  
  public static int checkIpV4Type(String ipV4) {
    int inValue = getIpV4Value(ipV4);
    if ((inValue >= IpCTypeRange[0]) && (inValue <= IpCTypeRange[1])) {
      return 3;
    }
    if ((inValue >= IpBTypeRange[0]) && (inValue <= IpBTypeRange[1])) {
      return 2;
    }
    if ((inValue >= IpATypeRange[0]) && (inValue <= IpATypeRange[1])) {
      return 1;
    }
    return 4;
  }
  
  public static boolean checkSameSegment(String ip1, String ip2, int mask)
  {
    if (!isValidIpV4(ip1)) {
      return false;
    }
    if (!isValidIpV4(ip2)) {
      return false;
    }
    int ipValue1 = getIpV4Value(ip1);
    int ipValue2 = getIpV4Value(ip2);
    return (mask & ipValue1) == (mask & ipValue2);
  }
  
  public static boolean checkSameSegmentByDefault(String ip1, String ip2) {
    int mask = getDefaultMaskValue(ip1);
    return checkSameSegment(ip1, ip2, mask);
  }
  
  public static void main(String[] args)
  {
    String ip1 = "172.16.1.2";
    String ip2 = "172.16.1.5";
    
    System.out.println(checkSameSegmentByDefault(ip1, ip2));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\NetUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */