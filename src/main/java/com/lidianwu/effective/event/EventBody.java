package com.lidianwu.effective.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件体
 *
 * @author Created by ldianwu on 2019/9/21
 */
public class EventBody {

    // 主题
    private String topic;
    // 参数
    private Map<String, Object> params;

    public EventBody(String topic) {
        this.topic = topic;
    }

    public void setParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        params.put(key, value);
    }

    public Object removeParam(String key) {
        if (params != null) {
            return params.remove(key);
        }
        return null;
    }

    public Object getParam(String key) {
        if (params != null) {
            return params.get(key);
        }
        return null;
    }

    public Map<String, Object> getParams() {
        return params == null ? new HashMap<String, Object>() : params;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "EventBody{" +
                "topic='" + topic + '\'' +
                ", params=" + params +
                '}';
    }
}
