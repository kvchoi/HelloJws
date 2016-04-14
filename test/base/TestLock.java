package base;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public class TestLock {
    // 可考虑切换为LRUMap
    // private static ConcurrentMap parallelLockMap = Maps.newConcurrentMap();
    private static ConcurrentLinkedHashMap<String, Object> parallelLockMap = new ConcurrentLinkedHashMap.Builder<String, Object>()
            .maximumWeightedCapacity(5000).concurrencyLevel(300).build();

    /**
     * 为指定的key，获取一把唯一锁
     * 
     * @param key
     * @return
     */
    protected static Object getLock(String key) {
        Object lock = TestLock.class; // // 向后兼容
        if (parallelLockMap != null) {
            Object newLock = new Object();
            lock = parallelLockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }
        return lock;
    }

    /**
     * 细粒度同步块示例
     * 
     * @param key
     */
    public static void doSomething(String key) {
        synchronized (getLock(key)) {
            System.out.println("do something...");
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for (int i = 0; i < 10; i++) {
            doSomething("key");
        }
    }

}
