package com.lidianwu.effective.event;

/**
 * 事件中心
 *
 * @author Created by ldianwu on 2019/9/21
 */
public interface EventCenter {

    /**
     * 订阅主题
     */
    public void subscribe(EventSubscriber subscriber, String... topics);

    /**
     * 取消订阅主题
     */
    public void unSubscribe(String topic, EventSubscriber subscriber);

    /**
     * 同步发布主题消息
     */
    public void publishSync(EventBody eventBody);

    /**
     * 异步发送主题消息
     */
    public void publishAsync(EventBody eventBody);

}
