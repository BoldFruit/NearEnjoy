package com.example.lib_data.data_user.token;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.util.Log;
import android.view.textclassifier.TextClassificationManager;
import android.widget.ScrollView;

import com.example.lib_common.util.SPUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import androidx.annotation.RequiresApi;


/**
 * Time:2020/2/21 13:47
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

/**
 * 用于统一管理sp中的数据
 */
public class TokenManager {


    public static final String LABELS_NAME = "labels";
    public static final String TOKEN_NAME = "token";
    public static final String CLASSIFICATION = "classification";
    public static final String FIRST_CLASSIFICATION = "firstClassification";
    public static final String SECOND_CLASSIFICATION = "secondClassification";
    public static final String IS_FIRST_LOGIN = "isFirstLogin";
    public static final String CLASSIFICATIONRELATION = "classificationRelation";

    public static void saveToken(String token) {
        SPUtils.put(TOKEN_NAME, token);
    }
    public static void saveLabels(HashMap<Integer, String> map) {
        SPUtils.put(LABELS_NAME, map);
    }


    public static void initSPUtil(Application application) {
        SPUtils.init(application);
    }

    public static String getToken() {

        String token = SPUtils.get(TOKEN_NAME, "");

        if ("".equals(token) || token == null) {
            Log.d("NULL TOKEN", "you don't have token");
        }

        return token;
    }

    public static Boolean getIsFirstLogin() {
        return SPUtils.get(IS_FIRST_LOGIN, true);
    }

    public static HashMap<Integer, String> getLabels() {
        HashMap<String, String> labels = SPUtils.get(LABELS_NAME, new HashMap<>());
        HashMap<Integer, String> trueLabels = new HashMap<>();
        if (labels != null && !labels.isEmpty()) {
            for (Map.Entry<String, String> mEntry : labels.entrySet()) {
                trueLabels.put(Integer.valueOf(mEntry.getKey()), mEntry.getValue());
            }
        } else {
            Log.d("NULL LABELS", "you don't have labels");
        }
        return trueLabels;
    }

    public static HashMap<String, String> getLabelMap() {
        return SPUtils.get(SPUtils.LABELS, new HashMap<>());
    }

    /**
     * 获取一级分类和二级分类的关系
     * eg:key:1,value:"2,3,4"
     * object : TypeToken<List<ClassificationBean>>() {}.type
     * 取HashMap时必须要传入token否则返回的总是HashMap<String,String>
     * @return
     */
    public static HashMap<Integer, String> getClassificationRelationShip(Context mContext) {
        return SPUtils.get(CLASSIFICATIONRELATION, new HashMap<Integer, String>(), (new TypeToken<HashMap<Integer, String>>(){}).getType());

    }

    /**
     * 获取所有一级分类id与名字的映射
     * @return
     */
    public static HashMap<Integer, String> getFirstClassification() {
        HashMap<String, String> firstClassification = SPUtils.get(FIRST_CLASSIFICATION, new HashMap<>());
        HashMap<Integer, String> trueClassification = new HashMap<>();
        if (firstClassification != null) {
            for (Map.Entry<String, String> mEntry : firstClassification.entrySet()) {
                trueClassification.put(Integer.parseInt(mEntry.getKey()), mEntry.getValue());
            }
            if (firstClassification.isEmpty()) {
                Log.d("NULL LABELS", "you don't have firstClassification");
            }
        }

        return trueClassification;
    }

    /**
     * 获取所有二级分类的映射关系
     * @return
     */
    public static HashMap<Integer, String> getSecondClassification() {
        HashMap<String, String> firstClassification = SPUtils.get(SECOND_CLASSIFICATION, new HashMap<>());
        HashMap<Integer, String> trueClassification = new HashMap<>();
        for (Map.Entry<String, String> mEntry : firstClassification.entrySet()) {
            trueClassification.put(Integer.getInteger(mEntry.getKey()), mEntry.getValue());
        }
        if (firstClassification.isEmpty()) {
            Log.d("NULL LABELS", "you don't have secondClassification");
        }

        return trueClassification;
    }


}
