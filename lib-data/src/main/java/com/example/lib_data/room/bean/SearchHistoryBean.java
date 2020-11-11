package com.example.lib_data.room.bean;

import com.example.lib_data.room.manager.SearchHistoryDataManager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author DuLong
 * @since 2020/2/27
 * email 798382030@qq.com
 */
//搜索的历史记录
@Entity(tableName = "SearchHistory")
public class SearchHistoryBean {

    public SearchHistoryBean(String mSearchContent, long mTime, boolean mIsGoods) {
        searchContent = mSearchContent;
        time = mTime;
        isGoods = mIsGoods;
    }

    public SearchHistoryBean() {}
    @PrimaryKey
    @NonNull
    private String searchContent;

    private long time;

    //判断是否是商品或是用户
    private boolean isGoods;

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String mSearchContent) {
        searchContent = mSearchContent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long mTime) {
        time = mTime;
    }

    public boolean isGoods() {
        return isGoods;
    }

    public void setGoods(boolean mGoods) {
        isGoods = mGoods;
    }
}
