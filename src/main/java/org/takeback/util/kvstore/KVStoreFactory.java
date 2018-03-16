package org.takeback.util.kvstore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KVStoreFactory
{
  private static KVStoreFactory instance;
  private Map<String, KVStore> stores = new ConcurrentHashMap();
  
  private KVStoreFactory() {
    instance = this;
  }
  
  public static KVStoreFactory instance() {
    if (instance == null) {
      instance = new KVStoreFactory();
    }
    return instance;
  }
  
  public KVStore get(String name) {
    KVStore st = (KVStore)this.stores.get(name);
    if (st == null) {
      throw new IllegalStateException("kvtore " + name + " not exists");
    }
    return st;
  }
  
  public void setStores(Map<String, KVStore> stores) {
    this.stores = stores;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\kvstore\KVStoreFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */