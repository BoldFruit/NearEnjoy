package com.example.login.viewmodel;

import android.util.Log;

import com.example.base.livedata.UnPeekLiveData;
import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.model.LoginModel;
import com.example.login.model.UpdatePWDModel;
import com.example.login.model.VerifyModel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Time:2020/2/23 9:36
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:用于找回密码时验证手机号
 */
public class LoginFindPwdViewModel extends MvvmNetworkViewModel {

    private MutableLiveData<LoginViewModel.LoginType> type;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> smsCode;
    private MutableLiveData<Integer> schoolId;
    private MutableLiveData<String> password;
    private UnPeekLiveData<LoginResponseBean> response;
    private LoginModel loginModel;
    private VerifyModel verifyModel;
    private MediatorLiveData<LoginResponseBean> mediatorLiveData;

    @Override
    protected void initModels() {
        type = new MutableLiveData<>(LoginViewModel.LoginType.SMS);
        phone = new MutableLiveData<>();
        schoolId = new MutableLiveData<>(1);
        smsCode = new MutableLiveData<>();
        password = new MutableLiveData<>();
        loginModel = new LoginModel(type, phone, smsCode, schoolId);
        verifyModel = new VerifyModel(phone);

        response = loginModel.getUnPeekLiveData();
        registerModel(LoginModel.tagName, loginModel);
        registerModel(VerifyModel.VERIFY_TAG, verifyModel);
    }

    public void getVerifyCode() {
        getCachedDataAndLoad(VerifyModel.VERIFY_TAG);
    }

    public void loginBySms() {
        getCachedDataAndLoad(LoginModel.tagName);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getSmsCode() {
        return smsCode;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public UnPeekLiveData<LoginResponseBean> getResponse() {
        return response;

    }

}
