package org.takeback.util.context.beans;

import java.util.HashMap;

public class JVMStatBean
{
  public HashMap<String, Long> getMemoryUsageInfo()
  {
    HashMap<String, Long> result = new HashMap();
    
    java.lang.management.MemoryMXBean m = java.lang.management.ManagementFactory.getMemoryMXBean();
    java.lang.management.MemoryUsage usage = m.getHeapMemoryUsage();
    
    result.put("heapCommitted", Long.valueOf(usage.getCommitted()));
    result.put("heapInit", Long.valueOf(usage.getInit()));
    result.put("heapMax", Long.valueOf(usage.getMax()));
    result.put("heapUsed", Long.valueOf(usage.getUsed()));
    
    usage = m.getNonHeapMemoryUsage();
    
    result.put("noHeapCommitted", Long.valueOf(usage.getCommitted()));
    result.put("noHeapInit", Long.valueOf(usage.getInit()));
    result.put("noHeapMax", Long.valueOf(usage.getMax()));
    result.put("noHeapUsed", Long.valueOf(usage.getUsed()));
    
    return result;
  }
  
  public HashMap<String, Long> gc() {
    java.lang.management.MemoryMXBean m = java.lang.management.ManagementFactory.getMemoryMXBean();
    m.gc();
    return getMemoryUsageInfo();
  }
  
  public HashMap<String, Object> getOpSystemInfo() {
    HashMap<String, Object> result = new HashMap();
    java.lang.management.OperatingSystemMXBean m = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
    
    result.put("arch", m.getArch());
    result.put("processors", Integer.valueOf(m.getAvailableProcessors()));
    result.put("name", m.getName());
    result.put("version", m.getVersion());
    result.put("loadAverage", Double.valueOf(m.getSystemLoadAverage()));
    
    return result;
  }
  
  public HashMap<String, Object> getThreadsInfo() {
    HashMap<String, Object> result = new HashMap();
    java.lang.management.ThreadMXBean m = java.lang.management.ManagementFactory.getThreadMXBean();
    
    result.put("deamonTheadCount", Integer.valueOf(m.getDaemonThreadCount()));
    result.put("peakThreadCount", Integer.valueOf(m.getPeakThreadCount()));
    result.put("threadCount", Integer.valueOf(m.getThreadCount()));
    result.put("totalStartedThreadCount", Long.valueOf(m.getTotalStartedThreadCount()));
    
    return result;
  }
  
  public long[] getThreadIds() {
    java.lang.management.ThreadMXBean m = java.lang.management.ManagementFactory.getThreadMXBean();
    
    return m.getAllThreadIds();
  }
  
  public HashMap<String, Object> getRuntimeInfo() {
    HashMap<String, Object> result = new HashMap();
    
    java.lang.management.RuntimeMXBean m = java.lang.management.ManagementFactory.getRuntimeMXBean();
    
    result.put("name", m.getName());
    result.put("startTime", Long.valueOf(m.getStartTime()));
    result.put("upTime", Long.valueOf(m.getUptime()));
    
    result.put("specName", m.getSpecName());
    result.put("specVendor", m.getSpecVendor());
    result.put("specVersion", m.getSpecVersion());
    result.put("vmName", m.getVmName());
    result.put("vmVendor", m.getVmVendor());
    result.put("vmVersion", m.getVmVersion());
    result.put("managementSpecVersion", m.getManagementSpecVersion());
    
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\context\beans\JVMStatBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */