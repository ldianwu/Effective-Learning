package com.lidianwu.effective.singleton;

/**
 * 懒汉式单例
 * <p>
 * 懒汉式单列是线程不安全的
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class LazySimpleSingleton {

    private static LazySimpleSingleton lazy = null;

    // 私有的构造函数
    private LazySimpleSingleton() {
    }

    // 实例化对象并提供这一实例
    public static LazySimpleSingleton getInstance() {
        /**
         * TODO 在高并发的场景下，可能创建多个实例
         */
        if (lazy == null) {
            lazy = new LazySimpleSingleton();
        }
        return lazy;
    }
}
