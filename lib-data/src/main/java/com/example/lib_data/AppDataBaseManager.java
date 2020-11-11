package com.example.lib_data;

import android.app.Application;
import android.content.Context;


/**
 * Time:2020/2/21 12:44
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class AppDataBaseManager {

    public static void initDataBase(Application application) {
        AppDataBase.setApplication(application);
    }

    public static AppDataBase getDataBase() throws Exception {
        return getDataBase(null);
    }

    public static AppDataBase getDataBase(Context context) throws Exception {
        return AppDataBase.getInstance(context);
    }

}
