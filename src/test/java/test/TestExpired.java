package test;

import com.google.common.base.Ticker;
import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestExpired {

    static LoadingCache<Integer, String> store = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .ticker(Ticker.systemTicker())
//            .recordStats()
            .removalListener(new RemovalListener<Integer, String>() {

                @Override
                public void onRemoval(RemovalNotification<Integer, String> removalNotification) {
                    System.out.println(removalNotification.getKey());
                }
            })
            .build(new CacheLoader<Integer, String>(){

                @Override
                public String load(Integer integer) throws Exception {
                    return null;
                }
            });

    public static void main(String[] args) {
        store.put(1, "1");
        store.put(2, "2");
        store.put(3, "3");
        store.put(4, "4");
        store.put(5, "5");
        while(true){
            try {
                Thread.sleep(1000);
                try {
                    store.get(2);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
