package com.example.login.model;

import com.example.base.model.BaseModel;
import com.example.login.api.LoginRepository;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/21 16:01
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class VerifyModel extends BaseModel<String> {

    public static final String VERIFY_TAG = "verify_model";

    private MutableLiveData<String> phone;

    public VerifyModel(MutableLiveData<String> phone) {
        this.phone = phone;
    }


    @Override
    protected void load() {

        LoginRepository.getInstance().sendVerifyCode(phone.getValue(), new BaseObserver<String>(this) {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onNext(String s) {
                loadSuccess(s);
            }
        });

    }
}
