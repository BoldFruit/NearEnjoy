package com.example.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author YangZhaoxin.
 * @since 2020/2/7 17:17.
 * email yangzhaoxin@hrsoft.net.
 * Function 响应拦截器
 */
// TODO: 有时间可参考网上案例做补充
public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        String rawJson = response.body() == null ? "" : response.body().string();
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), rawJson)).build();
    }
}
