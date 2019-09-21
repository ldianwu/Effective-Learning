package com.lidianwu.effective.event.injvm;

import com.alibaba.fastjson.JSON;
import com.lidianwu.effective.Constants;
import com.lidianwu.effective.commons.concurrent.ConcurrentHashSet;
import com.lidianwu.effective.event.EventBody;
import com.lidianwu.effective.event.EventCenter;
import com.lidianwu.effective.event.EventSubscriber;
import com.lidianwu.effective.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在一个jvm中的pub sub简易实现
 *
 * @author Created by ldianwu on 2019/9/21
 */
public class JvmInnerEventCenter implements EventCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmInnerEventCenter.class.getName());

    private final ConcurrentHashMap<String, Set<EventSubscriber>> ecMap = new ConcurrentHashMap<String, Set<EventSubscriber>>();

    /**
     * 线程池
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(Math.min(Constants.AVAILABLE_PROCESSOR >= 8 ? Constants.AVAILABLE_PROCESSOR : Constants.AVAILABLE_PROCESSOR * 2, 12), new NamedThreadFactory("App-JvmInnerEventCenter" +
            "-Executor", true));

    @Override
    public void subscribe(EventSubscriber subscriber, String... topics) {
        for (String topic : topics) {
            Set<EventSubscriber> subscribers = ecMap.get(topic);
            if (subscribers == null) {
                subscribers = new ConcurrentHashSet<EventSubscriber>();
                Set<EventSubscriber> oldSubscribers = ecMap.putIfAbsent(topic, subscribers);
                if (oldSubscribers != null) {
                    subscribers = oldSubscribers;
                }
            }
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unSubscribe(String topic, EventSubscriber subscriber) {
        Set<EventSubscriber> subscribers = ecMap.get(topic);
        if (subscribers != null) {
            for (EventSubscriber eventSubscriber : subscribers) {
                if (eventSubscriber.getId().equals(subscriber.getId())) {
                    subscribers.remove(eventSubscriber);
                }
            }
        }
    }

    @Override
    public void publishSync(EventBody eventBody) {
        Set<EventSubscriber> subscribers = ecMap.get(eventBody.getTopic());
        if (subscribers != null) {
            for (EventSubscriber subscriber : subscribers) {
                eventBody.setTopic(eventBody.getTopic());
                try {
                    subscriber.getObserver().onObserved(eventBody);
                } catch (Throwable t) {
                    // 防御性容错
                    LOGGER.error(" eventBody:{}, subscriber:{}", JSON.toJSONString(eventBody), JSON.toJSONString(subscriber), t);
                }
            }
        }
    }

    @Override
    public void publishAsync(final EventBody eventBody) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                String topic = eventBody.getTopic();

                Set<EventSubscriber> subscribers = ecMap.get(topic);
                if (subscribers != null) {
                    for (EventSubscriber subscriber : subscribers) {
                        try {
                            eventBody.setTopic(topic);
                            subscriber.getObserver().onObserved(eventBody);
                        } catch (Throwable t) {
                            // 防御性容错
                            LOGGER.error(" eventBody:{}, subscriber:{}", JSON.toJSONString(eventBody), JSON.toJSONString(subscriber), t);
                        }
                    }
                }
            }
        });
    }

}
