package com.example.lib_data.room.dao;

import com.example.lib_data.room.bean.SearchHistoryBean;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * @author DuLong
 * @since 2020/2/27
 * email 798382030@qq.com
 */
@Dao
public interface SearchHistoryDao {
    //按照时间顺序获得最近前十个商品记录
    @Query("SELECT * FROM SearchHistory WHERE isGoods = 1 ORDER BY time DESC LIMIT 10")
    List<SearchHistoryBean> getGoodsHistories();

    //按照时间顺序获得最近前十个用户记录
    @Query("SELECT * FROM SearchHistory WHERE isGoods = 0 ORDER BY time DESC LIMIT 10")
    List<SearchHistoryBean> getUserHistories();

    //删除所有商品数据
    @Query("DELETE FROM SearchHistory WHERE isGoods = 1")
    void deleteGoodsSearchHistories();

    @Query("DELETE FROM SearchHistory WHERE isGoods = 1 AND searchContent = :data")
    void deleteGoodsSearchHistory(String data);

    //删除所有用户数据
    @Query("DELETE FROM SearchHistory WHERE isGoods = 0")
    void deleteUserHistories();

    @Query("DELETE FROM SearchHistory WHERE isGoods = 0 AND searchContent = :data")
    void deleteUserHistory(String data);

    //添加数据，并且替换相同的数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insearchSeachHistory(SearchHistoryBean bean);
}
