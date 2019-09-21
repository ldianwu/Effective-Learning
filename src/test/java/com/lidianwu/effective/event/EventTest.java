package com.lidianwu.effective.event;

import com.lidianwu.effective.event.injvm.JvmInnerEventCenter;

/**
 * EventTest
 *
 * @author Created by ldianwu on 2019/9/21
 */
public class EventTest {

    private static final String topic = "test_event";

    // 订阅方
    private static void sub(EventCenter eventCenter) {
        eventCenter.subscribe(new EventSubscriber("sub1", new EventObserver() {

            @Override
            public void onObserved(EventBody eventBody) {
                System.out.println(eventBody);
            }

        }), topic);
    }

    public static void main(String[] args) {
        EventCenter eventCenter = new JvmInnerEventCenter();
        // 订阅
        sub(eventCenter);

        // 事件消秘
        EventBody eventBody = new EventBody(topic);
        eventBody.setParam("aa", "bb");

        // 发送方
        eventCenter.publishAsync(eventBody);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
