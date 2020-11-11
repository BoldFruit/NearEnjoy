package com.example.lib_data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.example.lib_data.convert.DateTypeConverter;
import com.example.lib_data.data_user.NearEnjoyUser;
import com.example.lib_data.data_user.UserDao;

import com.example.lib_data.room.bean.SearchHistoryBean;
import com.example.lib_data.room.dao.SearchHistoryDao;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Time:2020/2/21 12:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

@Database(entities = {NearEnjoyUser.class, SearchHistoryBean.class}, version = 1,  exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDataBase extends RoomDatabase {
    private static Application application;
    private static AppDataBase INSTANCE;
    private static final Object o = new Object();
    private static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    public abstract UserDao getUserDao();

    public abstract SearchHistoryDao getSearchHistoryDao();

    public static AppDataBase getInstance(Context context) throws Exception {
        if (INSTANCE == null) {
            synchronized (o) {
                if (INSTANCE == null) {
                    if (context == null && application == null) {
                        throw new Exception("cant init database because you didn't provide any application or context");
                    } else {
                        INSTANCE = Room.databaseBuilder(context == null ? application : context,
                                AppDataBase.class, "near_enjoy_db")
                                .allowMainThreadQueries()
                                .build();
                    }
                }
            }
        }
        return INSTANCE;
    }

    public static AppDataBase getInstance() throws Exception {
        return getInstance(null);
    }

    public static void setApplication(Application app) {
        if (app == null) {
            Log.d("NULL APPLICATION", "the application you set to database is null, " +
                    "you'd better check the function you call");
        }
       application = app;
    }
}
