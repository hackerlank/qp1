package org.takeback.chat.lottery.listeners;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.Message;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.service.LotteryService;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;
import org.takeback.chat.store.room.RoomStore;
import org.takeback.chat.store.user.User;
import org.takeback.chat.store.user.UserStore;
import org.takeback.chat.utils.MessageUtils;






public class DefaultGameListener
  implements RoomAndLotteryListener
{
  @Autowired
  protected RoomStore roomStore;
  @Autowired
  protected UserStore userStore;
  @Autowired
  protected LotteryService lotteryService;
  private ReentrantReadWriteLock roomStatusLock = new ReentrantReadWriteLock();
  
  public boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException
  {
    if (!"2".equals(builder.getType())) {
      int affected = this.lotteryService.moneyDown(Integer.valueOf(builder.getSender()), Double.valueOf(builder.getMoney().doubleValue()));
      if (affected == 0) {
        throw new GameException(500, "余额不足!");
      }
    }
    return true;
  }
  
  public void onRed(LotteryFactory.DefaultLotteryBuilder builder) throws GameException
  {
    if ("2".equals(builder.getType())) {
      Integer expired = Integer.valueOf(getConifg(builder.getRoomId(), "conf_expired"));
      builder.setExpiredSeconds(expired);
    } else {
      builder.setExpiredSeconds(Integer.valueOf(30));
    }
    builder.build();
  }
  
  public void onFinished(Lottery lottery)
    throws GameException
  {
    if ("1".equals(lottery.getType())) {
      Room room = (Room)this.roomStore.get(lottery.getRoomId());
      User sender = (User)this.userStore.get(lottery.getSender());
      String msg = "<span style='color:#B22222'>" + sender.getNickName() + " 的红包已被领完.</span>";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast(room, notice);
    }
  }
  
  /* Error */
  public void onExpired(Lottery lottery)
    throws GameException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 27	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStore	Lorg/takeback/chat/store/room/RoomStore;
    //   4: aload_1
    //   5: invokeinterface 28 1 0
    //   10: invokeinterface 29 2 0
    //   15: checkcast 30	org/takeback/chat/store/room/Room
    //   18: astore_2
    //   19: aload_0
    //   20: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   23: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   26: invokevirtual 47	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: invokevirtual 48	org/takeback/chat/lottery/listeners/DefaultGameListener:onBeforeExpire	(Lorg/takeback/chat/lottery/Lottery;Lorg/takeback/chat/store/room/Room;)Z
    //   35: ifne +14 -> 49
    //   38: aload_0
    //   39: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   42: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   45: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   48: return
    //   49: aload_0
    //   50: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   53: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   56: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   59: goto +16 -> 75
    //   62: astore_3
    //   63: aload_0
    //   64: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   67: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   70: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   73: aload_3
    //   74: athrow
    //   75: aload_0
    //   76: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   79: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   82: invokevirtual 51	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   85: aload_0
    //   86: aload_1
    //   87: aload_2
    //   88: invokevirtual 48	org/takeback/chat/lottery/listeners/DefaultGameListener:onBeforeExpire	(Lorg/takeback/chat/lottery/Lottery;Lorg/takeback/chat/store/room/Room;)Z
    //   91: ifne +14 -> 105
    //   94: aload_0
    //   95: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   98: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   101: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   104: return
    //   105: aload_2
    //   106: new 42	org/takeback/chat/entity/Message
    //   109: dup
    //   110: ldc 53
    //   112: aconst_null
    //   113: invokespecial 54	org/takeback/chat/entity/Message:<init>	(Ljava/lang/String;Ljava/lang/Object;)V
    //   116: invokestatic 45	org/takeback/chat/utils/MessageUtils:broadcast	(Lorg/takeback/chat/store/room/Room;Lorg/takeback/chat/entity/Message;)Ljava/util/List;
    //   119: pop
    //   120: aload_0
    //   121: aload_1
    //   122: invokevirtual 55	org/takeback/chat/lottery/listeners/DefaultGameListener:processExpireEvent	(Lorg/takeback/chat/lottery/Lottery;)V
    //   125: aload_0
    //   126: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   129: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   132: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   135: goto +18 -> 153
    //   138: astore 4
    //   140: aload_0
    //   141: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   144: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   147: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   150: aload 4
    //   152: athrow
    //   153: return
    // Line number table:
    //   Java source line #70	-> byte code offset #0
    //   Java source line #71	-> byte code offset #19
    //   Java source line #73	-> byte code offset #29
    //   Java source line #77	-> byte code offset #38
    //   Java source line #74	-> byte code offset #48
    //   Java source line #77	-> byte code offset #49
    //   Java source line #78	-> byte code offset #59
    //   Java source line #77	-> byte code offset #62
    //   Java source line #79	-> byte code offset #75
    //   Java source line #81	-> byte code offset #85
    //   Java source line #87	-> byte code offset #94
    //   Java source line #82	-> byte code offset #104
    //   Java source line #84	-> byte code offset #105
    //   Java source line #85	-> byte code offset #120
    //   Java source line #87	-> byte code offset #125
    //   Java source line #88	-> byte code offset #135
    //   Java source line #87	-> byte code offset #138
    //   Java source line #89	-> byte code offset #153
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	this	DefaultGameListener
    //   0	154	1	lottery	Lottery
    //   18	88	2	room	Room
    //   62	12	3	localObject1	Object
    //   138	13	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   29	38	62	finally
    //   85	94	138	finally
    //   105	125	138	finally
    //   138	140	138	finally
  }
  
  protected boolean onBeforeExpire(Lottery lottery, Room room)
  {
    if ((lottery.getStatus().equals("2")) || ("9".equals(room.getStatus()))) {
      return false;
    }
    
    if ("1".equals(lottery.getType())) {
      BigDecimal bd = this.lotteryService.giftLotteryExpired(lottery);
      Message msg = new Message("TXT_SYS", Integer.valueOf(0), "<span style='color:#B22222'>您发出的红包未被抢完," + bd + "金额已经退到您的账户!<span>");
      MessageUtils.send(lottery.getSender(), (Room)this.roomStore.get(lottery.getRoomId()), msg);
      return false;
    }
    return true;
  }
  
  protected void processExpireEvent(Lottery lottery)
    throws GameException
  {}
  
  public boolean onBeforeOpen(Integer uid, Lottery lottery) throws GameException
  {
    return false;
  }
  
  /* Error */
  public void onStart(Room room)
    throws GameException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   4: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   7: invokevirtual 47	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   10: ldc 25
    //   12: aload_1
    //   13: invokevirtual 58	org/takeback/chat/store/room/Room:getStatus	()Ljava/lang/String;
    //   16: invokevirtual 8	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   19: ifne +15 -> 34
    //   22: ldc 57
    //   24: aload_1
    //   25: invokevirtual 58	org/takeback/chat/store/room/Room:getStatus	()Ljava/lang/String;
    //   28: invokevirtual 8	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   31: ifeq +14 -> 45
    //   34: aload_0
    //   35: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   38: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   41: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   44: return
    //   45: aload_0
    //   46: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   49: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   52: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   55: goto +16 -> 71
    //   58: astore_2
    //   59: aload_0
    //   60: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   63: invokevirtual 46	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   66: invokevirtual 49	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   69: aload_2
    //   70: athrow
    //   71: aload_0
    //   72: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   75: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   78: invokevirtual 51	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   81: ldc 25
    //   83: aload_1
    //   84: invokevirtual 58	org/takeback/chat/store/room/Room:getStatus	()Ljava/lang/String;
    //   87: invokevirtual 8	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   90: ifne +15 -> 105
    //   93: ldc 57
    //   95: aload_1
    //   96: invokevirtual 58	org/takeback/chat/store/room/Room:getStatus	()Ljava/lang/String;
    //   99: invokevirtual 8	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   102: ifeq +14 -> 116
    //   105: aload_0
    //   106: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   109: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   112: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   115: return
    //   116: aload_1
    //   117: new 42	org/takeback/chat/entity/Message
    //   120: dup
    //   121: ldc 64
    //   123: aconst_null
    //   124: invokespecial 54	org/takeback/chat/entity/Message:<init>	(Ljava/lang/String;Ljava/lang/Object;)V
    //   127: invokestatic 45	org/takeback/chat/utils/MessageUtils:broadcast	(Lorg/takeback/chat/store/room/Room;Lorg/takeback/chat/entity/Message;)Ljava/util/List;
    //   130: pop
    //   131: aload_0
    //   132: aload_1
    //   133: invokevirtual 65	org/takeback/chat/lottery/listeners/DefaultGameListener:processStartEvent	(Lorg/takeback/chat/store/room/Room;)V
    //   136: aload_0
    //   137: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   140: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   143: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   146: goto +16 -> 162
    //   149: astore_3
    //   150: aload_0
    //   151: getfield 4	org/takeback/chat/lottery/listeners/DefaultGameListener:roomStatusLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   154: invokevirtual 50	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   157: invokevirtual 52	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   160: aload_3
    //   161: athrow
    //   162: return
    // Line number table:
    //   Java source line #123	-> byte code offset #0
    //   Java source line #125	-> byte code offset #10
    //   Java source line #129	-> byte code offset #34
    //   Java source line #126	-> byte code offset #44
    //   Java source line #129	-> byte code offset #45
    //   Java source line #130	-> byte code offset #55
    //   Java source line #129	-> byte code offset #58
    //   Java source line #131	-> byte code offset #71
    //   Java source line #133	-> byte code offset #81
    //   Java source line #139	-> byte code offset #105
    //   Java source line #134	-> byte code offset #115
    //   Java source line #136	-> byte code offset #116
    //   Java source line #137	-> byte code offset #131
    //   Java source line #139	-> byte code offset #136
    //   Java source line #140	-> byte code offset #146
    //   Java source line #139	-> byte code offset #149
    //   Java source line #141	-> byte code offset #162
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	163	0	this	DefaultGameListener
    //   0	163	1	room	Room
    //   58	12	2	localObject1	Object
    //   149	12	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   10	34	58	finally
    //   81	105	149	finally
    //   116	136	149	finally
  }
  
  protected void processStartEvent(Room room)
    throws GameException
  {}
  
  public boolean onBeforeStart(Room room)
    throws GameException
  {
    return false;
  }
  





  public void onOpen(Lottery lottery, LotteryDetail lotteryDetail)
    throws GameException
  {
    if ("1".equals(lottery.getType())) {
      this.lotteryService.saveLotteryDetail(lottery, lotteryDetail);
      
      String sendNickName = null;
      User opener = (User)this.userStore.get(lotteryDetail.getUid());
      if (0 == lottery.getSender().intValue()) {
        sendNickName = "系统";
      } else {
        User sender = (User)this.userStore.get(lottery.getSender());
        if (opener.getId().equals(sender.getId())) {
          sendNickName = "自己";
        } else {
          sendNickName = sender.getNickName();
        }
      }
      String msg = opener.getNickName() + " 领取了" + sendNickName + "发的红包";
      Message notice = new Message("TXT_SYS", Integer.valueOf(0), msg);
      MessageUtils.broadcast((Room)this.roomStore.get(lottery.getRoomId()), notice);
    }
  }
  




  public String getConifg(String roomId, String key)
    throws GameException
  {
    Room room = (Room)this.roomStore.get(roomId);
    Map<String, Object> properties = room.getProperties();
    if (properties.containsKey(key)) {
      return properties.get(key).toString();
    }
    throw new GameException(500, "缺少配置项[" + key + "]");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\DefaultGameListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */