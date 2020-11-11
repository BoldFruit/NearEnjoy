package com.example.lib_data.room.manager;

import android.content.Context;

import com.example.lib_data.AppDataBase;
import com.example.lib_data.AppDataBaseManager;
import com.example.lib_data.data_user.UserDao;
import com.example.lib_data.room.bean.SearchHistoryBean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;

import kotlin.coroutines.EmptyCoroutineContext;

/**
 * @author DuLong
 * @since 2020/2/27
 * email 798382030@qq.com
 */

public class SearchHistoryDataManager {
    //插入
    public static void insertHistory(SearchHistoryBean data, Context mContext) {
        try {
            AppDataBaseManager.getDataBase(mContext).getSearchHistoryDao().insearchSeachHistory(data);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    //删除所有商品搜索记录
    public static void deleteAllGoodsHistory(Context mContext) {
        try {
            AppDataBase.getInstance(mContext).getSearchHistoryDao().deleteGoodsSearchHistories();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    //删除指定商品搜索记录
    public static void deleteGoodsHistory(Context mContext, String data) {
        try {
            AppDataBase.getInstance(mContext).getSearchHistoryDao().deleteGoodsSearchHistory(data);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    //删除所有的用户搜索记录
    public static void deleteAllUserHistory(Context mContext) {
        try {
            AppDataBase.getInstance(mContext).getSearchHistoryDao().deleteUserHistories();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    //删除指定的用户搜索记录
    public static void deleteUserHistory(Context mContext, String data) {
        try {
            AppDataBase.getInstance(mContext).getSearchHistoryDao().deleteUserHistory(data);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    //获取商品记录
    public static List<SearchHistoryBean> getAllGoodsHistory(Context mContext) {
        final List<SearchHistoryBean>[] data = new List[]{new ArrayList<>()};
        try {
            data[0] = AppDataBaseManager.getDataBase(mContext).getSearchHistoryDao().getGoodsHistories();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        return data[0];
    }

    //获取用户记录，由于可能会非常好事，利用线程库开新线程进行操作
    public static List<SearchHistoryBean> getAllUserHistory(Context mContext) {
        final List<SearchHistoryBean>[] data = new List[]{new ArrayList<>()};
        try {
            data[0] = AppDataBaseManager.getDataBase(mContext).getSearchHistoryDao().getUserHistories();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        return data[0];
    }

}
