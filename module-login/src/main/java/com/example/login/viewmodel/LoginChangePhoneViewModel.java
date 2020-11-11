package com.example.login.viewmodel;

import com.example.base.livedata.UnPeekLiveData;
import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.model.LoginModel;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/24 8:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:更换手机号，验证密码
 */
public class LoginChangePhoneViewModel extends MvvmNetworkViewModel {

    private LoginModel mLoginModel;
    private MutableLiveData<LoginViewModel.LoginType> type;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> password;
    private MutableLiveData<Integer> schoolId;

    @Override
    protected void initModels() {
        phone = new MutableLiveData<>();
        password = new MutableLiveData<>();
        type = new MutableLiveData<>(LoginViewModel.LoginType.PASSWORD);
        schoolId = new MutableLiveData<>(1);
        mLoginModel = new LoginModel(type, phone, password, schoolId);
        registerModel(LoginModel.tagName, mLoginModel);
    }

    public void loginByPwd() {
        getCachedDataAndLoad(LoginModel.tagName);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public UnPeekLiveData<LoginResponseBean> getResponseData() {
        return mLoginModel.getUnPeekLiveData();
    }
}
