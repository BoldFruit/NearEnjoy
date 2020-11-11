package com.example.module_splash;

import android.media.session.MediaSession;

import com.example.base.BaseApplication;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.network.HttpClient;

/**
 * @author DuLong
 * @since 2020/5/4
 * email 798382030@qq.com
 */
public class SplashApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.initSPUtil(this);
        HttpClient.getInstance().init(new NetworkRequestInfo());
    }
}
