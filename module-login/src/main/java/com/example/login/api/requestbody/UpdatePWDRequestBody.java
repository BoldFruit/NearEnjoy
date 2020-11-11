package com.example.login.api.requestbody;

import com.google.gson.annotations.SerializedName;

/**
 * Time:2020/2/22 17:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class UpdatePWDRequestBody {

    @SerializedName("newPwd")
    private String pwd;

    public UpdatePWDRequestBody(String pwd) {
        this.pwd = pwd;
    }
}
