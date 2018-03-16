package org.takeback.util.params.support;

import org.takeback.util.kvstore.KVStore;
import org.takeback.util.kvstore.KVStoreFactory;
import org.takeback.util.params.Param;

public class KVStoreParamLoader extends MemeryParamLoader
{
  private String storeName = "paramsStore";
  private KVStore kvStore;
  
  public KVStoreParamLoader() {
    this.kvStore = KVStoreFactory.instance().get(this.storeName);
  }
  
  public KVStoreParamLoader(String storeName) {
    this.kvStore = KVStoreFactory.instance().get(storeName);
  }
  
  public String getParam(String parName, String defaultValue, String paramalias)
  {
    Param p = (Param)this.params.get(parName);
    if (p != null) {
      return p.getParamvalue();
    }
    String value = this.kvStore.get(parName);
    if (value != null) {
      this.params.put(parName, new Param(parName, value, paramalias));
      return value;
    }
    if (defaultValue == null) {
      return null;
    }
    p = new Param(parName, defaultValue, paramalias);
    this.params.put(parName, p);
    this.kvStore.put(parName, defaultValue);
    return defaultValue;
  }
  
  public void setParam(String parName, String value)
  {
    super.setParam(parName, value);
    this.kvStore.put(parName, value);
  }
  
  public void removeParam(String parName)
  {
    super.removeParam(parName);
    this.kvStore.remove(parName);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\params\support\KVStoreParamLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */