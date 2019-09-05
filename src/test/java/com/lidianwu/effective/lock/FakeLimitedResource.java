package com.lidianwu.effective.lock;

/**
 * FakeLimitedResource
 *
 * @author Created by ldianwu on 2019/9/5
 */
public class FakeLimitedResource {

    // 总共250张火车票
    private Integer ticket = 249;

    public void use() throws InterruptedException {
        try {
            if (ticket > 0) {
                System.out.println("火车票还剩" + (--ticket) + "张！");
            } else {
                System.out.println("没有火车票啦！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
