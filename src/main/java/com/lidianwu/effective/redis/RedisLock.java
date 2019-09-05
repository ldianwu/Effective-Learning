package com.lidianwu.effective.redis;

import com.lidianwu.effective.lock.Lock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * Redis分布式锁
 * <p>
 * 只适用于Redis单本模式
 * 在redis集群模式下请参考RedLock算法
 * <p>
 * redis部署方式：
 * 1.单机模式
 * 2.master-slave + sentinel选举模式
 * 3.redis cluster模式
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class RedisLock implements Lock {

    // 锁键
    private final String lockKey = "redis_lock";
    // 等待获取锁时间
    private long secondsToWait = 10;
    // 锁过期时间
    private long millisecondsToExpire = 30000;

    private JedisPool jedisPool;

    public RedisLock(String hostname) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), hostname);
    }

    public RedisLock(String hostname, int port) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), hostname, port);
    }

    public RedisLock(String hostname, int port, String password) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), hostname, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    public RedisLock(String hostname, int port, String password, int database) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), hostname, port, Protocol.DEFAULT_TIMEOUT, password, database);
    }

    public RedisLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 获取锁
     *
     * @param uniqueValue
     * @return
     */
    @Override
    public boolean lock(String uniqueValue) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            // nx: 不存在则设置成功
            SetParams params = SetParams.setParams().nx().px(millisecondsToExpire);

            long t0 = System.currentTimeMillis();
            for (; ; ) {
                // set
                String result = jedis.set(lockKey, uniqueValue, params);
                if ("OK".equals(result)) {
                    return true;
                }
                // 否则循环等待，在超时时间内仍未获取到锁，则获取失败
                long dif = System.currentTimeMillis() - t0;
                if (dif >= secondsToWait * 1000) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 释放锁
     *
     * @param uniqueValue
     * @return
     */
    @Override
    public boolean unlock(String uniqueValue) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            // lua脚本来保证原子性
            String script =
                    "if redis.call('get', KEYS[1]) == ARGV[1] then" +
                            "   return redis.call('del', KEYS[1]) " +
                            "else" +
                            "   return 0 " +
                            "end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey),
                    Collections.singletonList(uniqueValue));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void close() {
        if (this.jedisPool != null) {
            jedisPool.close();
        }
    }
}
