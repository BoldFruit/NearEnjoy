package com.example.lib_neuqer_chat.network;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

/**
 * @author YangZhaoxin.
 * @since 2020/2/9 10:08.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class NetworkManager {

    private Application mApplication;

    private MutableLiveData<NetType> mNetTypeLiveData;
    private NetworkCallbackImpl mNetworkCallback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private NetworkManager() {
        mNetworkCallback = new NetworkCallbackImpl();
        mNetTypeLiveData = mNetworkCallback.getNetTypeLiveData();
    }

    public Application getApplication() {
        return mApplication;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class InstanceHolder {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 初始化
     * @param application
     */
    @SuppressWarnings("MissingPermission")
    public void init(Application application) {
        if (application != null) {
            mApplication = application;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                NetworkRequest request = new NetworkRequest.Builder().build();
                ConnectivityManager manager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (manager != null) {
                    manager.registerNetworkCallback(request, mNetworkCallback);
                }
            }
        } else {
            throw new NullPointerException("NetworkManager init(application) 不能接受一个空参数");
        }
    }

    public MutableLiveData<NetType> getNetTypeLiveData() {
        return mNetTypeLiveData;
    }
}
