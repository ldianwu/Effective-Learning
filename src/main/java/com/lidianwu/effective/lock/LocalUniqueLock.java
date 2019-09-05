package com.lidianwu.effective.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地唯一性并发锁（利用concurrenthashmap的分段锁来提高并发性能）
 * <p>
 * TODO 1.在分布式下不能保证唯一性，只适用于单体服务
 * TODO 2.分布式部署下可使用redis、zookeeper来实现分布式锁
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class LocalUniqueLock implements Lock {

    private Map<String, String> _map = new ConcurrentHashMap<String, String>();

    @Override
    public boolean lock(String key) {
        if (_map.containsKey(key)) {
            return false;
        } else {
            _map.put(key, key);
            return true;
        }
    }

    @Override
    public boolean unlock(String key) {
        _map.remove(key);
        return true;
    }

    @Override
    public void close() {
    }
}