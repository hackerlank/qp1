package org.takeback.chat.lottery.listeners;

import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.LotteryDetail;
import org.takeback.chat.store.room.LotteryFactory.DefaultLotteryBuilder;
import org.takeback.chat.store.room.Room;

public abstract interface RoomAndLotteryListener
{
  public abstract boolean onBeforeRed(LotteryFactory.DefaultLotteryBuilder paramDefaultLotteryBuilder)
    throws GameException;
  
  public abstract void onRed(LotteryFactory.DefaultLotteryBuilder paramDefaultLotteryBuilder)
    throws GameException;
  
  public abstract void onFinished(Lottery paramLottery)
    throws GameException;
  
  public abstract void onExpired(Lottery paramLottery)
    throws GameException;
  
  public abstract boolean onBeforeOpen(Integer paramInteger, Lottery paramLottery)
    throws GameException;
  
  public abstract void onOpen(Lottery paramLottery, LotteryDetail paramLotteryDetail)
    throws GameException;
  
  public abstract void onStart(Room paramRoom)
    throws GameException;
  
  public abstract boolean onBeforeStart(Room paramRoom)
    throws GameException;
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\lottery\listeners\RoomAndLotteryListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */