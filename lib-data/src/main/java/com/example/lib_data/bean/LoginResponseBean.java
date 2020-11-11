package com.example.lib_data.bean;

import com.example.lib_data.data_user.NearEnjoyUser;
import com.google.gson.annotations.SerializedName;

/**
 * Time:2020/2/21 14:04
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginResponseBean {


    /**
     * token : 5055a737c62249a2a1b338a3b1861cfa
     * user : {"id":5,"schoolId":1,"name":"小橡果","avatar":"","signature":null}
     */
    @SerializedName("newUser")
    private boolean newUser;
    @SerializedName(value = "token")
    private String token;
    @SerializedName(value = "user")
    private NearEnjoyUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NearEnjoyUser getUser() {
        return user;
    }

    public void setUser(NearEnjoyUser user) {
        this.user = user;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}
