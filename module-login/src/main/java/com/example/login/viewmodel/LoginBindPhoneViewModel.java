package com.example.login.viewmodel;

import com.example.base.livedata.UnPeekLiveData;
import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.login.model.LoginBindPhoneModel;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/24 10:40
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:绑定新的手机号
 */
public class LoginBindPhoneViewModel extends MvvmNetworkViewModel {

    private LoginBindPhoneModel bindPhoneModel;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> smsCode;
    private UnPeekLiveData<String> bindResult;

    @Override
    protected void initModels() {
        phone = new MutableLiveData<>();
        smsCode = new MutableLiveData<>();
        bindResult = new UnPeekLiveData<>();
        bindPhoneModel = new LoginBindPhoneModel(phone, smsCode);
        bindResult = bindPhoneModel.getUnPeekLiveData();
        registerModel(LoginBindPhoneModel.LOGIN_BIND_PHONE_TAG, bindPhoneModel);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getSmsCode() {
        return smsCode;
    }

    public UnPeekLiveData<String> getBindResult() {
        return bindResult;
    }

    public void bindNewPhone() {
        getCachedDataAndLoad(LoginBindPhoneModel.LOGIN_BIND_PHONE_TAG);
    }
}
