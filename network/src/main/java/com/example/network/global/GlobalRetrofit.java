package com.example.network.global;

import com.example.network.INetworkRequestInfo;
import com.example.network.constant.Constants;
import com.example.network.errorHandler.AppDataErrorHandler;
import com.example.network.errorHandler.HttpErrorHandler;
import com.example.network.interceptor.RequestInterceptor;
import com.example.network.interceptor.ResponseInterceptor;
import com.example.network.response.ApiResponse;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.Scope;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 16:16.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class GlobalRetrofit {

    private Retrofit mRetrofit;
    private INetworkRequestInfo mNetworkRequestInfo;
    private ErrorTransformer mErrorTransformer;
    private RequestInterceptor mHttpsRequestInterceptor;
    private ResponseInterceptor mHttpResponseInterceptor;

    private HashMap<String, Object> mServiceCache;


    public GlobalRetrofit() {
        mErrorTransformer = new ErrorTransformer();
        mServiceCache = new HashMap<>();
    }

    public void setNetworkRequestInfo(INetworkRequestInfo networkRequestInfo) {
        mNetworkRequestInfo = networkRequestInfo;
        mHttpsRequestInterceptor = new RequestInterceptor(networkRequestInfo);
        mHttpResponseInterceptor = new ResponseInterceptor();
    }

    private Retrofit getRetrofit() {
        if (mNetworkRequestInfo == null) {
            throw new RuntimeException("本地未初始化 network");
        }
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(mNetworkRequestInfo.getBaseUrl())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Constants.APP_SERVER_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(mHttpsRequestInterceptor)
                .addInterceptor(mHttpResponseInterceptor);
        setLoggingLevel(builder);
        OkHttpClient okHttpClient = builder.build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(Constants.MAX_REQUESTS_PER_HOST);
        return okHttpClient;
    }

    private void setLoggingLevel(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // DeBug 模式下开启打印
        logging.setLevel(mNetworkRequestInfo.isDebug() ? HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(logging);
    }

    public void apiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mErrorTransformer)
                .subscribe(observer);
    }

    /**
     * 使用全局参数创建请求 api实例
     * @param service
     * @param <K>
     * @return
     */
    public <K> K createService(Class<K> service) {
        K retrofitService = (K) mServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = getRetrofit().create(service);
            mServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 处理错误的变换：
     * 网络请求的错误处理，其中网络错误有以下两类：
     * 1、http请求相关的错误，例如：404，403，socket timeout等等；
     * 2. 处理非服务器产生的错误，如本地无网络，JSON解析错误等
     * 3、http请求正常，但是返回的应用数据里提示发生了异常，表明服务器已经接收到了来自客户端的请求，但是由于
     *    某些原因，服务器没有正常处理完请求。
     * @param <T>
     */
    private class ErrorTransformer<T> implements ObservableTransformer<ApiResponse<T>, T> {

        @Override
        public ObservableSource<T> apply(Observable<ApiResponse<T>> upstream) {
            return upstream.onErrorResumeNext(new HttpErrorHandler<>())
                    .flatMap(new AppDataErrorHandler(mNetworkRequestInfo.getNetCorrectCode()));
        }
    }
}
