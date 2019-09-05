package com.lidianwu.effective.volatitle;

/**
 * volatile example
 *
 * @author ldianwu
 */
public class VolatileExample extends Thread {

    // 共享变量
    private static volatile boolean flag = false;

    // 主线程
    public static void main(String[] args) throws Exception {
        new VolatileExample().start();
        // sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
        Thread.sleep(100);

        /**
         * 可见性的问题：
         *
         * 如果下面run方法里的while循环不执行任何代码块，也就是说当main线程修改了变量，
         * 线程一对flag变量的值是不可见的，线程一从内存中读入cpu高速缓存的还是falg=false这个值
         *
         * 解决办法在flag前加上volatile
         *
         * volatile: 保证可见性和有序性
         * 当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
         */
        flag = true;
    }

    // 线程一
    @Override
    public void run() {
        while (!flag) {
        }
    }
}
