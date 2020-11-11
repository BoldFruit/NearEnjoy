package com.example.lib_data.data_user;


import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Time:2020/2/21 12:16
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@Entity(tableName = "user")
public class NearEnjoyUser {

    /**
     * id : 5
     * schoolId : 1
     * name : 小橡果
     * avatar :
     * signature : null
     */

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private int id;
    @ColumnInfo(name = "school_id")
    private int schoolId;
    @ColumnInfo(name = "user_name")
    private String name;
    @ColumnInfo(name = "user_avatar")
    private String avatar;
    @ColumnInfo(name = "user_signature")
    private String signature;
    @ColumnInfo(name = "login_date")
    private Date loginDate;

    //必须要有构造方法
    public NearEnjoyUser() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}
