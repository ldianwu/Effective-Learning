package com.lidianwu.effective.zookeeper;

import com.lidianwu.effective.lock.Lock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.TimeUnit;

/**
 * ZookeeperLock
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class ZookeeperLock implements Lock {

    // 锁键
    private final String lockPath = "/lock/share/";
    // 等待获取锁时间
    private long secondsToWait = 10;

    private CuratorFramework client;
    private InterProcessMutex interProcessMutex;

    public ZookeeperLock(String connectString) {
        this.client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3, Integer.MAX_VALUE));
        this.client.start();
    }

    @Override
    public boolean lock(String uniqueValue) {
        try {
            this.interProcessMutex = new InterProcessMutex(client, lockPath + uniqueValue);
            return this.interProcessMutex.acquire(secondsToWait, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unlock(String uniqueValue) {
        if (this.interProcessMutex != null) {
            try {
                this.interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() {
        if (client != null) {
            CloseableUtils.closeQuietly(client);
        }
    }
}
