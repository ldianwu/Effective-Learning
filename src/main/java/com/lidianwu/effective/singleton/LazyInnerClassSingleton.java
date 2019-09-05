package com.lidianwu.effective.singleton;

/**
 * 静态内部类单例
 * <p>
 * 这种单例模式可以说是完美的单例模式了
 * <p>
 * 1.延迟加载(使用的时候才会实例化),避免项目启动内存的消耗
 * 2.内部类一定是在方法调用之前初始化，巧妙地避免了线程安全问题
 * <p>
 * TODO 结论：加载一个类时，其内部类不会同时被加载。一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class LazyInnerClassSingleton {

    // 私有的构造方法
    private LazyInnerClassSingleton() {
    }

    // 公有的获取实例方法
    public static final LazyInnerClassSingleton getInstance() {
        return LazyHolder.LAZY;
    }

    // 静态内部类
    // TODO 当LazyInnerClassSingleton实例化时，不会被同时加载，只有当调用上面getInstance方法时才会加载
    private static class LazyHolder {

        private static final LazyInnerClassSingleton LAZY = new LazyInnerClassSingleton();
    }
}
