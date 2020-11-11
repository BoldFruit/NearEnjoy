package com.example.login.viewmodel;

import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.login.model.LoginModel;
import com.example.login.model.VerifyModel;

import androidx.lifecycle.MutableLiveData;

/**
 * @author YangZhaoxin.
 * @since 2020/2/1 13:33.
 * email yangzhaoxin@hrsoft.net.
 */

public class LoginViewModel extends MvvmNetworkViewModel {

    public enum LoginType {
        SMS,

        PASSWORD
    }

    private MutableLiveData<LoginType> mLoginType;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> mCodeOrPassword;
    private MutableLiveData<Integer> schoolId;

    @Override
    protected void initModels() {
        mLoginType = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        mCodeOrPassword = new MutableLiveData<>();
        schoolId = new MutableLiveData<>();
        //暂时先设置成1
        schoolId.setValue(1);
        mLoginType.setValue(LoginType.SMS);
        registerModel(LoginModel.tagName, new LoginModel(mLoginType, phone, mCodeOrPassword, schoolId));
        registerModel(VerifyModel.VERIFY_TAG, new VerifyModel(phone));
    }

    public void login() {
        getCachedDataAndLoad(LoginModel.tagName);
    }

    public void sendVerifyCode() {
        getCachedDataAndLoad(VerifyModel.VERIFY_TAG);
    }

    public MutableLiveData getLoginLiveData() {
        return getDataLiveData(LoginModel.tagName);
    }

    public MutableLiveData<LoginType> getLoginType() {
        return mLoginType;
    }

    public void setLoginType(LoginType type) {
        mLoginType.setValue(type);
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getCodeOrPassword() {
        return mCodeOrPassword;
    }

    public MutableLiveData<Integer> getSchoolId() {
        return schoolId;
    }
}
