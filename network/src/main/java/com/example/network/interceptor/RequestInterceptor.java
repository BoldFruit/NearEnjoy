package com.example.network.interceptor;

import android.text.TextUtils;

import com.example.network.INetworkRequestInfo;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author YangZhaoxin.
 * @since 2020/2/7 17:16.
 * email yangzhaoxin@hrsoft.net.
 * Function 请求拦截器
 */

public class RequestInterceptor implements Interceptor {

    private INetworkRequestInfo mNetworkRequestInfo;

    public RequestInterceptor(INetworkRequestInfo networkRequestInfo) {
        mNetworkRequestInfo = networkRequestInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (mNetworkRequestInfo != null) {
            HashMap<String, String> requestHeaderMap = mNetworkRequestInfo.getRequestHeadersMap();
            if (requestHeaderMap != null) {
                for (String key : requestHeaderMap.keySet()) {
                    String value = requestHeaderMap.get(key);
                    if (!TextUtils.isEmpty(value)) {
                        builder.addHeader(key, value);
                    }
                }
            }
        }
        return chain.proceed(builder.build());
    }
}
