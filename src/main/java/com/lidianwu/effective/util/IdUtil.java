package com.lidianwu.effective.util;

/**
 * IdUtil
 * 
 * @author Created by ldianwu on 2019/9/5
 */
public final class IdUtil {

    private static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);

    private IdUtil() {
    }

    public static Long nextId() {
        return snowflakeIdWorker.nextId();
    }
}
