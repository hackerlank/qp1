package org.takeback.util.kvstore;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract interface KVStore
{
  public abstract void start();
  
  public abstract void close();
  
  public abstract void put(String paramString1, String paramString2);
  
  public abstract void put(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  
  public abstract String get(String paramString);
  
  public abstract byte[] get(byte[] paramArrayOfByte);
  
  public abstract Map.Entry<byte[], byte[]> seekFirst();
  
  public abstract void remove(String paramString);
  
  public abstract void remove(byte[] paramArrayOfByte);
  
  public abstract void removes(List<String> paramList);
  
  public abstract void removesByByte(List<byte[]> paramList);
  
  public abstract void puts(Map<String, String> paramMap);
  
  public abstract void putsByByte(Map<byte[], byte[]> paramMap);
  
  public abstract Map<String, String> gets();
  
  public abstract Map<byte[], byte[]> getsByByte();
  
  public abstract boolean containsKey(String paramString);
  
  public abstract boolean containsKey(byte[] paramArrayOfByte);
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\kvstore\KVStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */