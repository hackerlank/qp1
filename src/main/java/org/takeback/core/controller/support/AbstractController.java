package org.takeback.core.controller.support;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.takeback.core.controller.Configurable;
import org.takeback.core.controller.ConfigurableLoader;
import org.takeback.core.controller.Controller;

public abstract class AbstractController<T extends Configurable> implements Controller<T>
{
  protected final Map<String, T> store = Maps.newHashMap();
  protected ConfigurableLoader<T> loader;
  private final Lock lock = new ReentrantLock();
  
  public void setLoader(ConfigurableLoader<T> loader)
  {
    this.loader = loader;
  }
  
  public ConfigurableLoader<T> getLoader()
  {
    return this.loader;
  }
  
  public boolean isLoaded(String id)
  {
    try {
      this.lock.lock();
      return this.store.containsKey(id);
    } finally {
      this.lock.unlock();
    }
  }
  
  /* Error */
  public void reload(String id)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 8 1 0
    //   9: aload_0
    //   10: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   13: aload_1
    //   14: invokeinterface 11 2 0
    //   19: pop
    //   20: aload_0
    //   21: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   24: invokeinterface 10 1 0
    //   29: goto +15 -> 44
    //   32: astore_2
    //   33: aload_0
    //   34: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   37: invokeinterface 10 1 0
    //   42: aload_2
    //   43: athrow
    //   44: return
    // Line number table:
    //   Java source line #41	-> byte code offset #0
    //   Java source line #42	-> byte code offset #9
    //   Java source line #44	-> byte code offset #20
    //   Java source line #45	-> byte code offset #29
    //   Java source line #44	-> byte code offset #32
    //   Java source line #46	-> byte code offset #44
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	AbstractController<T>
    //   0	45	1	id	String
    //   32	11	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	20	32	finally
  }
  
  public void reloadAll()
  {
    this.store.clear();
  }
  
  /* Error */
  public T get(String id)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 8 1 0
    //   9: aload_0
    //   10: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   13: aload_1
    //   14: invokeinterface 9 2 0
    //   19: ifeq +28 -> 47
    //   22: aload_0
    //   23: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   26: aload_1
    //   27: invokeinterface 13 2 0
    //   32: checkcast 14	org/takeback/core/controller/Configurable
    //   35: astore_2
    //   36: aload_0
    //   37: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   40: invokeinterface 10 1 0
    //   45: aload_2
    //   46: areturn
    //   47: aload_0
    //   48: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   51: invokeinterface 10 1 0
    //   56: goto +15 -> 71
    //   59: astore_3
    //   60: aload_0
    //   61: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   64: invokeinterface 10 1 0
    //   69: aload_3
    //   70: athrow
    //   71: aload_0
    //   72: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   75: invokeinterface 8 1 0
    //   80: aload_0
    //   81: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   84: aload_1
    //   85: invokeinterface 9 2 0
    //   90: ifeq +28 -> 118
    //   93: aload_0
    //   94: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   97: aload_1
    //   98: invokeinterface 13 2 0
    //   103: checkcast 14	org/takeback/core/controller/Configurable
    //   106: astore_2
    //   107: aload_0
    //   108: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   111: invokeinterface 10 1 0
    //   116: aload_2
    //   117: areturn
    //   118: aload_0
    //   119: getfield 7	org/takeback/core/controller/support/AbstractController:loader	Lorg/takeback/core/controller/ConfigurableLoader;
    //   122: aload_1
    //   123: invokeinterface 15 2 0
    //   128: astore_2
    //   129: aload_2
    //   130: ifnull +28 -> 158
    //   133: aload_0
    //   134: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   137: aload_1
    //   138: aload_2
    //   139: invokeinterface 16 3 0
    //   144: pop
    //   145: aload_2
    //   146: astore_3
    //   147: aload_0
    //   148: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   151: invokeinterface 10 1 0
    //   156: aload_3
    //   157: areturn
    //   158: aconst_null
    //   159: astore_3
    //   160: aload_0
    //   161: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   164: invokeinterface 10 1 0
    //   169: aload_3
    //   170: areturn
    //   171: astore 4
    //   173: aload_0
    //   174: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   177: invokeinterface 10 1 0
    //   182: aload 4
    //   184: athrow
    // Line number table:
    //   Java source line #56	-> byte code offset #0
    //   Java source line #57	-> byte code offset #9
    //   Java source line #58	-> byte code offset #22
    //   Java source line #61	-> byte code offset #36
    //   Java source line #58	-> byte code offset #45
    //   Java source line #61	-> byte code offset #47
    //   Java source line #62	-> byte code offset #56
    //   Java source line #61	-> byte code offset #59
    //   Java source line #65	-> byte code offset #71
    //   Java source line #66	-> byte code offset #80
    //   Java source line #67	-> byte code offset #93
    //   Java source line #78	-> byte code offset #107
    //   Java source line #67	-> byte code offset #116
    //   Java source line #69	-> byte code offset #118
    //   Java source line #70	-> byte code offset #129
    //   Java source line #71	-> byte code offset #133
    //   Java source line #72	-> byte code offset #145
    //   Java source line #78	-> byte code offset #147
    //   Java source line #72	-> byte code offset #156
    //   Java source line #74	-> byte code offset #158
    //   Java source line #78	-> byte code offset #160
    //   Java source line #74	-> byte code offset #169
    //   Java source line #78	-> byte code offset #171
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	185	0	this	AbstractController<T>
    //   0	185	1	id	String
    //   35	82	2	localConfigurable	Configurable
    //   128	18	2	t	Object
    //   59	11	3	localObject1	Object
    //   146	24	3	localObject2	Object
    //   171	12	4	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   0	36	59	finally
    //   71	107	171	finally
    //   118	147	171	finally
    //   158	160	171	finally
    //   171	173	171	finally
  }
  
  /* Error */
  public void add(T t)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   4: invokeinterface 8 1 0
    //   9: aload_0
    //   10: getfield 3	org/takeback/core/controller/support/AbstractController:store	Ljava/util/Map;
    //   13: aload_1
    //   14: invokeinterface 17 1 0
    //   19: aload_1
    //   20: invokeinterface 16 3 0
    //   25: pop
    //   26: aload_0
    //   27: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   30: invokeinterface 10 1 0
    //   35: goto +15 -> 50
    //   38: astore_2
    //   39: aload_0
    //   40: getfield 6	org/takeback/core/controller/support/AbstractController:lock	Ljava/util/concurrent/locks/Lock;
    //   43: invokeinterface 10 1 0
    //   48: aload_2
    //   49: athrow
    //   50: return
    // Line number table:
    //   Java source line #84	-> byte code offset #0
    //   Java source line #85	-> byte code offset #9
    //   Java source line #87	-> byte code offset #26
    //   Java source line #88	-> byte code offset #35
    //   Java source line #87	-> byte code offset #38
    //   Java source line #89	-> byte code offset #50
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	51	0	this	AbstractController<T>
    //   0	51	1	t	T
    //   38	11	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	26	38	finally
  }
  
  public void setInitList(List<T> ls)
  {
    for (T t : ls) {
      add(t);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\controller\support\AbstractController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */