package com.example.lib_data.sp;

import android.app.Application;
import android.util.Log;

import com.example.lib_common.util.SPUtils;

import java.util.HashMap;

/**
 * @author DuLong
 * @since 2020/4/4
 * email 798382030@qq.com
 */
public class LabelsManager {

    public static final String LABELS_NAME = "labels";
    public static void saveLabels(HashMap<Integer, String> map) {
        SPUtils.put(LABELS_NAME, map);
    }

    public static void initSPUtil(Application application) {
        SPUtils.init(application);
    }

    public static String getToken() {

        String token = SPUtils.get(LABELS_NAME, "");

        if ("".equals(token) || token == null) {
            Log.d("NULL TOKEN", "you don't have token");
        }

        return token;
    }
}
