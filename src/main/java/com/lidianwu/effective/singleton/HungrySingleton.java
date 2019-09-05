package com.lidianwu.effective.singleton;

/**
 * 饿汉式单例
 * <p>
 * 1.优点：没有加任何的锁、执行效率比较高，在用户体验上来说，比懒汉式更好。
 * 2.缺点：类加载的时候就初始化，不管用与不用都占着空间，浪费了内存。
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class HungrySingleton {

    // 自己创建自己的实例
    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    // 私有的构造函数
    private HungrySingleton() {
    }

    // 向其他对象提供这一实例
    public static HungrySingleton getInstance() {
        return hungrySingleton;
    }
}
