package com.lidianwu.effective.lock;

/**
 * 并发锁
 * <p>
 * subclass:
 * LocalUniqueLock
 * RedisLock（未实现）
 * ZookeeperLock（未实现）
 *
 * @author Created by ldianwu on 2019/9/5
 */
public interface Lock {

    boolean lock(String uniqueValue);

    boolean unlock(String uniqueValue);

    void close();
}
