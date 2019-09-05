package com.lidianwu.effective.lock;

import com.lidianwu.effective.redis.RedisLock;
import com.lidianwu.effective.util.IdUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * RedisLockingExample
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class RedisLockingExample {

    private static final int QTY = 5;
    private static final int REPETITIONS = QTY * 10;
    private static final String UNIQUE_VALUE = IdUtil.nextId().toString();

    // FakeLimitedResource模拟某些外部资源，这些外部资源一次只能由一个进程访问
    private static final FakeLimitedResource resource = new FakeLimitedResource();

    public static void doWork(Lock lock, String clientName) throws Exception {
        if (!lock.lock(UNIQUE_VALUE)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try {
            System.out.println(clientName + " has the lock");
            // 操作资源
            resource.use();
        } finally {
            System.out.println(clientName + " releasing the lock");
            // 总是在Final块中释放锁
            lock.unlock(UNIQUE_VALUE);
        }
    }

    public static Lock getLock() {
        return new RedisLock("traykai.xicp.net", 6379, "traykai2015", 10);
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        try {
            for (int i = 0; i < QTY; ++i) {
                final int index = i;
                Callable<Void> task = () -> {
                    Lock lock = getLock();
                    try {
                        for (int j = 0; j < REPETITIONS; ++j) {
                            doWork(lock, "Client " + index + "-" + j);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.close();
                    }
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
