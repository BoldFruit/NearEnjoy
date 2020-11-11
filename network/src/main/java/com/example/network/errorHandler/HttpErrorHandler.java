package com.example.network.errorHandler;

import com.example.network.response.ApiResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author YangZhaoxin.
 * @since 2020/2/7 17:13.
 * email yangzhaoxin@hrsoft.net.
 * Function
 * HttpErrorHandler 处理一下两类网络错误：
 * 1. http请求相关的错误：403,404...
 * 2. 处理非服务器产生的错误，如本地无网络，JSON解析错误等
 */

public class HttpErrorHandler<T> implements Function<Throwable, ObservableSource<? extends ApiResponse<T>>> {

    @Override
    public ObservableSource<? extends ApiResponse<T>> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
