package com.example.login.api.requestbody;

import com.google.gson.annotations.SerializedName;

/**
 * Time:2020/2/21 20:03
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginByPWDRequestBody {


    /**
     * phone : 13081859497
     * pwd : 12345678
     */

    @SerializedName("phone")
    private String phone;
    private String pwd;

    public LoginByPWDRequestBody(String phone, String pwd) {
        this.phone = phone;
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
