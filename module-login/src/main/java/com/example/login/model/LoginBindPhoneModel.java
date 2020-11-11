package com.example.login.model;

import com.example.base.model.BaseModel;
import com.example.login.api.LoginRepository;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/25 15:23
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginBindPhoneModel extends BaseModel<String> {

    public static final String LOGIN_BIND_PHONE_TAG = "login_bind_phone";

    private MutableLiveData<String> phone;
    private MutableLiveData<String> smsCode;

    public LoginBindPhoneModel(MutableLiveData<String> phone, MutableLiveData<String> smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
    }

    @Override
    protected void load() {
        LoginRepository.getInstance().updatePhone(phone.getValue(), smsCode.getValue(), new BaseObserver<String>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                loadSuccess(s);
            }
        });
    }
}
