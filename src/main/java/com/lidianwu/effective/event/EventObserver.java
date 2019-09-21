package com.lidianwu.effective.event;

/**
 * 事件观察者
 *
 * @author Created by ldianwu on 2019/9/21
 */
public interface EventObserver {

    public void onObserved(EventBody eventBody);
}
