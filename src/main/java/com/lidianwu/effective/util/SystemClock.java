package com.lidianwu.effective.util;

import com.lidianwu.effective.thread.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SystemClock
 * <p>
 * 高并发场景下System.currentTimeMillis()的性能问题的优化
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）
 * <p>
 * 解决办法：
 * 单个调度线程来按毫秒更新时间戳，相当于维护一个全局缓存。其他线程取时间戳时相当于从内存取，不会再造成时钟资源的争用，
 * 代价就是牺牲了一些精确度。具体代码如下
 *
 * @author Created by ldianwu on 2019/9/6
 */
public class SystemClock {

    /**
     * 多长时间读取一次系统时间
     */
    private final long period;
    private final AtomicLong now;

    /**
     * 构造函数
     *
     * @param period 多长时间读取一次系统时间
     */
    private SystemClock(long period) {
        this.period = period;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static final SystemClock getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static long now() {
        return getInstance().currentTimeMillis();
    }

    /**
     * 周期性读取系统时间修改
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("System-Clock", true));
        scheduler.scheduleAtFixedRate(() -> {
            now.set(System.currentTimeMillis());
        }, period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    // 内部类
    private static class InstanceHolder {

        public static final SystemClock INSTANCE = new SystemClock(1);
    }
}
