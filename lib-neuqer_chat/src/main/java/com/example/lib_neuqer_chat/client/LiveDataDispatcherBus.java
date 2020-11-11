package com.example.lib_neuqer_chat.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Time:2020/3/16 10:13
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LiveDataDispatcherBus {

    public static final String PACKET_KEY = "chat_packet";
    public static final String MESSAGE_KEY = "chat_message";
    private final Map<String, BusMutableLiveData<Object>> busMap;
    private static final Object lockObject = new Object();
    private static LiveDataDispatcherBus instance = null;

    private LiveDataDispatcherBus() {
        busMap = new HashMap<>();
    }

    public static LiveDataDispatcherBus getInstance() {
        if (instance == null) {
            synchronized (lockObject) {
                if (instance == null) {
                    instance = new LiveDataDispatcherBus();
                }
            }
        }
        return instance;
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!busMap.containsKey(key)) {
            busMap.put(key, new BusMutableLiveData<>());
        }
        return (MutableLiveData<T>) busMap.get(key);
    }

    public MutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

    private static class ObserveWrapper<T> implements androidx.lifecycle.Observer<T> {

        private Observer<T> observer;
        public ObserveWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(T t) {

            if (observer != null) {
                if (isCallOnObserve()) {
                    return;
                }
                observer.onChanged(t);
            }

        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace) {
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) &&
                            "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class BusMutableLiveData<T> extends MutableLiveData<T> {
        private Map<Observer, Observer> observerMap = new HashMap<>();

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull androidx.lifecycle.Observer<? super T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void observeForever(@NonNull androidx.lifecycle.Observer<? super T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserveWrapper(observer));
            }
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<? super T> observer) {
            Observer realObserver = null;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }

            super.removeObserver(realObserver);
        }

        private void hook(androidx.lifecycle.Observer<? super T> observer)  {
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = null;
            try {
                fieldObservers = classLiveData.getDeclaredField("mObservers");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            assert fieldObservers != null;
            fieldObservers.setAccessible(true);
            Object objectObservers = null;
            try {
                objectObservers = fieldObservers.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assert objectObservers != null;
            Class<?> classObservers = objectObservers.getClass();
            try {
                Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
                methodGet.setAccessible(true);
                try {
                    Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
                    Object objectWrapper = null;
                    if (objectWrapperEntry instanceof Map.Entry) {
                        objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
                    }

                    if (objectWrapper == null) {
                        throw new NullPointerException("Wrapper can not be null");
                    }

                    Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
                    try {
                        assert classObserverWrapper != null;
                        //get the last version of the observer
                        Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
                        fieldLastVersion.setAccessible(true);
                        //get the version of LiveData
                        Field fieldVersion = classLiveData.getDeclaredField("mVersion");
                        fieldVersion.setAccessible(true);

                        Object objectVersion = fieldVersion.get(this);
                        //set wrapper's version
                        fieldLastVersion.set(objectWrapper, objectVersion);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }


            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
