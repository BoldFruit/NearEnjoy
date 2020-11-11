package com.example.mvvm_simple.application;

import android.content.Context;

import com.example.arouter.ARouter;
import com.example.arouter.BuildConfig;
import com.example.base.BaseApplication;
import com.example.base.loadSir.CustomCallback;
import com.example.base.loadSir.EmptyCallback;
import com.example.base.loadSir.ErrorCallback;
import com.example.base.loadSir.LoadingCallback;
import com.example.base.loadSir.TimeoutCallback;
import com.example.lib_data.AppDataBaseManager;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.mvvm_simple.NetworkRequestInfo;
import com.example.network.HttpClient;
import com.kingja.loadsir.core.LoadSir;
import com.taobao.sophix.SophixManager;

/**
 * @author YangZhaoxin.
 * @since 2020/1/10 12:21.
 * email yangzhaoxin@hrsoft.net.
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 必须为整个应用的Debug状态
        setDebug(BuildConfig.DEBUG);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();

        TokenManager.initSPUtil(this);
        AppDataBaseManager.initDataBase(this);
        ARouter.getInstance().init(this);
        HttpClient.getInstance().init(new NetworkRequestInfo());
        //查询服务器是否有可用补丁
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

}
