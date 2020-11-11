package com.example.login.api.requestbody;

import com.google.gson.annotations.SerializedName;

/**
 * Time:2020/2/21 18:30
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginBySMSRequestBody {


    /**
     * schoolId : 1
     * phone : 13081859497
     * smsCode : 123456
     */

    @SerializedName(value = "schoolId")
    private int schoolId;
    @SerializedName(value = "phone")
    private String phone;
    @SerializedName(value = "smsCode")
    private String smsCode;

    public LoginBySMSRequestBody(int schoolId, String phone, String smsCode) {
        this.schoolId = schoolId;
        this.phone = phone;
        this.smsCode = smsCode;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
