package com.example.lib_data.data_user;

import android.content.Context;

import com.example.lib_data.AppDataBase;
import com.example.lib_data.AppDataBaseManager;

import java.util.List;

/**
 * Time:2020/2/21 13:01
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class UserInfoIO {

    public static void insertUser(NearEnjoyUser user, Context context) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            try {
                AppDataBaseManager.getDataBase(context).getUserDao().insertUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void insertUser(NearEnjoyUser user) {
        insertUser(user, null);
    }

    public static NearEnjoyUser getLastUser(Context context) throws Exception {
        List<NearEnjoyUser> userList = AppDataBaseManager.getDataBase(context).getUserDao().getUsers();
        if (!userList.isEmpty()){
        return userList.get(userList.size() - 1);}
        else {
            return null;
        }

    }

    public static NearEnjoyUser getLasterUser() throws Exception {
       return getLastUser(null);
    }

}
