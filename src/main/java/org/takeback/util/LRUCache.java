package org.takeback.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class LRUCache<K, V>
  extends LinkedHashMap<K, V>
{
  private static final long serialVersionUID = -5167631809472116969L;
  private static final float DEFAULT_LOAD_FACTOR = 0.75F;
  private static final int DEFAULT_MAX_CAPACITY = 1000;
  private volatile int maxCapacity;
  private final Lock lock = new ReentrantLock();
  
  public LRUCache() {
    this(1000);
  }
  
  public LRUCache(int maxCapacity) {
    super(16, 0.75F, true);
    this.maxCapacity = maxCapacity;
  }
  
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
  {
    return size() > this.maxCapacity;
  }
  
  public boolean containsKey(Object key)
  {
    try {
      this.lock.lock();
      return super.containsKey(key);
    } finally {
      this.lock.unlock();
    }
  }
  
  public V get(Object key)
  {
    try {
      this.lock.lock();
      return (V)super.get(key);
    } finally {
      this.lock.unlock();
    }
  }
  
  public V put(K key, V value)
  {
    try {
      this.lock.lock();
      return (V)super.put(key, value);
    } finally {
      this.lock.unlock();
    }
  }
  
  public V remove(Object key)
  {
    try {
      this.lock.lock();
      return (V)super.remove(key);
    } finally {
      this.lock.unlock();
    }
  }
  
  public int size()
  {
    try {
      this.lock.lock();
      return super.size();
    } finally {
      this.lock.unlock();
    }
  }
  
  /* Error */
  public void clear()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 7	org/takeback/util/LRUCache:lock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 10 1 0
    //   9: aload_0
    //   10: invokespecial 17	java/util/LinkedHashMap:clear	()V
    //   13: aload_0
    //   14: getfield 7	org/takeback/util/LRUCache:lock	Ljava/util/concurrent/locks/Lock;
    //   17: invokeinterface 12 1 0
    //   22: goto +15 -> 37
    //   25: astore_1
    //   26: aload_0
    //   27: getfield 7	org/takeback/util/LRUCache:lock	Ljava/util/concurrent/locks/Lock;
    //   30: invokeinterface 12 1 0
    //   35: aload_1
    //   36: athrow
    //   37: return
    // Line number table:
    //   Java source line #86	-> byte code offset #0
    //   Java source line #87	-> byte code offset #9
    //   Java source line #89	-> byte code offset #13
    //   Java source line #90	-> byte code offset #22
    //   Java source line #89	-> byte code offset #25
    //   Java source line #91	-> byte code offset #37
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	38	0	this	LRUCache<K, V>
    //   25	11	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	13	25	finally
  }
  
  public int getMaxCapacity()
  {
    return this.maxCapacity;
  }
  
  public void setMaxCapacity(int maxCapacity) {
    this.maxCapacity = maxCapacity;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\LRUCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */