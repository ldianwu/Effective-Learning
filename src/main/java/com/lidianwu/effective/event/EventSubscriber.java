package com.lidianwu.effective.event;

/**
 * 事件订阅者
 *
 * @author Created by ldianwu on 2019/9/21
 */
public class EventSubscriber {

    private String id;
    private EventObserver observer;

    public EventSubscriber(String id, EventObserver observer) {
        this.id = id;
        this.observer = observer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventObserver getObserver() {
        return observer;
    }

    public void setObserver(EventObserver observer) {
        this.observer = observer;
    }
}
