package org.takeback.util.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil
{
  private static final int DEFAULT_SIZE = 50;
  private static final int DEFAULT_MAX_SIZE = 150;
  private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 150, 5L, TimeUnit.SECONDS, new ArrayBlockingQueue(5), new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
  




  public static void execute(Runnable task)
  {
    threadPoolExecutor.execute(task);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\thread\ThreadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */