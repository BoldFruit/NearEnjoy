package com.example.login.api.requestbody;

/**
 * Time:2020/2/24 17:43
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class UpdatePhoneRequestBody {


    /**
     * phone : 13081859497
     * smsCode : 123456
     */

    private String phone;
    private String smsCode;

    public UpdatePhoneRequestBody(String phone, String smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
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
