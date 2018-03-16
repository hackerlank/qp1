package test;

import org.takeback.chat.lottery.DefaultLottery;
import org.takeback.chat.lottery.Lottery;
import org.takeback.chat.lottery.listeners.GameException;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

public class TestLottery implements Runnable {

    private Lottery lottery;
    private int uid;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public TestLottery(Lottery lottery, int uid) {
        this.lottery = lottery;
        this.uid = uid;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        lottery.finished();
        try {
            System.out.println(lottery.open(uid));
            if (lottery.getRestNumber() == 0) {
                lottery.finished();
            }
        } catch (GameException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        DefaultLottery lottery = new DefaultLottery(BigDecimal.valueOf(100), 6);
        for (int i = 0; i < 10; i++) {
            TestLottery tl = new TestLottery(lottery, i + 1);
            Thread t = new Thread(tl);
            t.start();
        }
        countDownLatch.countDown();
    }
}
