package com.example.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YangZhaoxin.
 * @since 2020/1/20 10:18.
 * email yangzhaoxin@hrsoft.net.
 */

public class LiveDataBus {

    // 装载所有的订阅者
    private Map<String, BusMutableLiveData<Object>> bus;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    private static class InstanceHolder {
        private static final LiveDataBus INSTANCE = new LiveDataBus();
    }

    public static LiveDataBus getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     *  注册订阅者
     * @param key
     * @param type  传递类型的类对象
     * @param <T>
     * @return  返回LiveData进行观察确认
     */
    public synchronized <T> BusMutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<Object>());
        }
        return (BusMutableLiveData<T>) bus.get(key);
    }
}
