package org.takeback.chat.utils;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.takeback.chat.entity.Message;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.user.AnonymousUser;
import org.takeback.chat.store.user.RobotUser;
import org.takeback.chat.store.user.User;
import org.takeback.util.JSONUtils;





public class MessageUtils
{
  public static List<FailedResult> broadcast(Room room, Message message)
  {
    return broadcast(room, message, false);
  }
  
  public static void broadcastDelay(Room room, Message message, long delay) {
    threadPoolExecutor.execute(new DelayDeliver(delay, room, message));
  }
  
  public static void broadcastDelay(Room room, Message message) {
    threadPoolExecutor.execute(new DelayDeliver(room, message));
  }
  




  public static List<FailedResult> broadcastWithoutGuests(Room room, Message message)
  {
    return broadcast(room, message, true);
  }
  
  private static List<FailedResult> broadcast(Room room, Message message, boolean isSigned) {
    Map<Integer, User> users = room.getUsers();
    List<FailedResult> results = broadcast(users.values().iterator(), message);
    if (!isSigned) {
      Map<String, AnonymousUser> guests = room.getGuests();
      List<FailedResult> resultsOfGuests = broadcast(guests.values().iterator(), message);
      if (resultsOfGuests.size() > 0) {
        results.addAll(resultsOfGuests);
      }
    }
    return results;
  }
  
  private static List<FailedResult> broadcast(Iterator<? extends User> iterator, Message message) {
    List<FailedResult> results = Lists.newArrayList();
    while (iterator.hasNext()) {
      User user = (User)iterator.next();
      if (!(user instanceof RobotUser))
      {

        WebSocketSession session = user.getWebSocketSession();
        if (session != null)
        {

          if (!session.isOpen()) {
            iterator.remove();
          }
          else {
            FailedResult result = send(session, message);
            if (result != null)
              results.add(result);
          } }
      } }
    return results;
  }
  
  public static FailedResult send(WebSocketSession session, Message message) {
    try {
      session.sendMessage(new TextMessage(JSONUtils.toString(message)));
      return null;
    } catch (IOException e) {
      return new FailedResult(session, message, e);
    } catch (IllegalStateException e) {
      return new FailedResult(session, message, e);
    }
  }
  
  public static FailedResult send(Integer uid, Room room, Message message) {
    User user = (User)room.getUsers().get(uid);
    if ((user != null) && (!(user instanceof RobotUser))) {
      return send(user.getWebSocketSession(), message);
    }
    return null;
  }
  
  public static FailedResult sendCMD(WebSocketSession session, String cmd, Object cmdContent) {
    Message message = new Message(cmd, cmdContent);
    return send(session, message);
  }
  
  public static FailedResult sendCMD(User user, String cmd, Object cmdContent) {
    Message message = new Message(cmd, cmdContent);
    if ((user != null) && (!(user instanceof RobotUser))) {
      return send(user.getWebSocketSession(), message);
    }
    return null;
  }
  
  private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 3L, TimeUnit.SECONDS, new ArrayBlockingQueue(5), new ThreadPoolExecutor.AbortPolicy());
  



  static class DelayDeliver
    implements Runnable
  {
    private long delay = 5L;
    private Room room;
    private Message message;
    
    DelayDeliver(long delay, Room room, Message message) {
      this(room, message);
    }
    
    DelayDeliver(Room room, Message message)
    {
      this.room = room;
      this.message = message;
    }
    
    public void run()
    {
      try {
        TimeUnit.SECONDS.sleep(this.delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      MessageUtils.broadcast(this.room, this.message);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\utils\MessageUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */