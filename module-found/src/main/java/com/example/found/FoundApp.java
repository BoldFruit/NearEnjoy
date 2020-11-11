package com.example.found;

import com.example.base.BaseApplication;
import com.example.lib_data.AppDataBaseManager;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.network.HttpClient;

/**
 * @author DuLong
 * @since 2020/2/23
 * email 798382030@qq.com
 */
public class FoundApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpClient.getInstance().init(new NetworkRequestInfo());
        TokenManager.initSPUtil(this);
        AppDataBaseManager.initDataBase(this);
    }

}
