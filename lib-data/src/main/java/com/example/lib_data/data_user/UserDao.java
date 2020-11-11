package com.example.lib_data.data_user;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Time:2020/2/21 12:30
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@Dao
public interface UserDao {

    //按照日期对用户进行排序，最后一个是最近登陆过的用户
    @Query("SELECT * FROM user ORDER BY datetime(login_date)")
    List<NearEnjoyUser> getUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(NearEnjoyUser user);

    @Delete
    void deleteUser(NearEnjoyUser user);

}
