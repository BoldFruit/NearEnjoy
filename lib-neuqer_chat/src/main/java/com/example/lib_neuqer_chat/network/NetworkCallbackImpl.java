package com.example.lib_neuqer_chat.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.example.lib_neuqer_chat.util.LiveDataUtil;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

/**
 * @author YangZhaoxin.
 * @since 2020/2/9 15:40.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    public static final String LOG_TAG = "network >>> ";
    private MutableLiveData<NetType> mNetTypeLiveData;

    public NetworkCallbackImpl() {
        mNetTypeLiveData = new MutableLiveData<>();
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        Log.e(LOG_TAG, "network >>> 网络已连接");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Log.e(LOG_TAG, "network >>> 网络已中断");
        LiveDataUtil.postValue(mNetTypeLiveData, NetType.NONE);
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.e(LOG_TAG, "network >>> 网络发生变更，类型为 WIFI");
                LiveDataUtil.postValue(mNetTypeLiveData, NetType.WIFI);
            } else {
                Log.e(LOG_TAG, "network >>> 网络发生变更，类型为 流量");
                LiveDataUtil.postValue(mNetTypeLiveData, NetType.CMWAP);
            }
        }
    }

    public MutableLiveData<NetType> getNetTypeLiveData() {
        return mNetTypeLiveData;
    }
}
