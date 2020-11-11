package com.example.lib_data.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Time:2020/2/26 11:40
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@Entity(tableName = "his")
public class SearchHistoryBean {

    @PrimaryKey
    String content;

    public SearchHistoryBean(String content) {
        this.content = content;
    }
}
