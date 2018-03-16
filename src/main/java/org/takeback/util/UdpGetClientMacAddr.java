package org.takeback.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
















































































































































































































































































































































































class UdpGetClientMacAddr
{
  private String remoteAddr;
  private int remotePort = 137;
  private byte[] buffer = new byte['Ѐ'];
  private DatagramSocket ds = null;
  
  public UdpGetClientMacAddr(String strAddr) throws Exception {
    this.remoteAddr = strAddr;
    this.ds = new DatagramSocket();
  }
  
  protected final DatagramPacket send(byte[] bytes) throws IOException {
    DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(this.remoteAddr), this.remotePort);
    this.ds.send(dp);
    return dp;
  }
  
  protected final DatagramPacket receive() throws Exception {
    DatagramPacket dp = new DatagramPacket(this.buffer, this.buffer.length);
    this.ds.receive(dp);
    return dp;
  }
  
  protected byte[] GetQueryCmd() throws Exception {
    byte[] t_ns = new byte[50];
    t_ns[0] = 0;
    t_ns[1] = 0;
    t_ns[2] = 0;
    t_ns[3] = 16;
    t_ns[4] = 0;
    t_ns[5] = 1;
    t_ns[6] = 0;
    t_ns[7] = 0;
    t_ns[8] = 0;
    t_ns[9] = 0;
    t_ns[10] = 0;
    t_ns[11] = 0;
    t_ns[12] = 32;
    t_ns[13] = 67;
    t_ns[14] = 75;
    
    for (int i = 15; i < 45; i++) {
      t_ns[i] = 65;
    }
    
    t_ns[45] = 0;
    t_ns[46] = 0;
    t_ns[47] = 33;
    t_ns[48] = 0;
    t_ns[49] = 1;
    return t_ns;
  }
  
  protected final String getMacAddr(byte[] brevdata) throws Exception
  {
    int i = brevdata[56] * 18 + 56;
    String sAddr = "";
    StringBuffer sb = new StringBuffer(17);
    for (int j = 1; j < 7; j++) {
      sAddr = Integer.toHexString(0xFF & brevdata[(i + j)]);
      if (sAddr.length() < 2) {
        sb.append(0);
      }
      sb.append(sAddr.toUpperCase());
      if (j < 6) sb.append(':');
    }
    return sb.toString();
  }
  
  public final void close() {
    try {
      this.ds.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public final String getRemoteMacAddr() throws Exception {
    byte[] bqcmd = GetQueryCmd();
    send(bqcmd);
    DatagramPacket dp = receive();
    String smac = getMacAddr(dp.getData());
    close();
    return smac;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\UdpGetClientMacAddr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */