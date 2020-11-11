package com.example.network;

import com.example.network.global.GlobalRetrofit;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 16:12.
 * email yangzhaoxin@hrsoft.net.
 * Function Client,对外暴露的入口
 */

public class HttpClient {

    private GlobalRetrofit mGlobalRetrofit;

    private HttpClient() {
        mGlobalRetrofit = new GlobalRetrofit();
    }

    private static class InstanceHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    public static HttpClient getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void init(INetworkRequestInfo networkRequestInfo) {
        mGlobalRetrofit.setNetworkRequestInfo(networkRequestInfo);
    }

    public void apiSubscribe(Observable observable, Observer observer) {
        mGlobalRetrofit.apiSubscribe(observable, observer);
    }

    /**
     * 使用全局参数创建请求
     */
    public <K> K createService(Class<K> service) {
        return mGlobalRetrofit.createService(service);
    }
}
