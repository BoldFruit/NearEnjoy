package com.example.login.viewmodel;

import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.login.model.UpdatePWDModel;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/21 22:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:是新用户
 */
public class LoginNewUserViewModel extends MvvmNetworkViewModel {

    private MutableLiveData<String> pwd;
    private UpdatePWDModel updatePWDModel;

    @Override
    protected void initModels() {
        pwd = new MutableLiveData<>();
        updatePWDModel = new UpdatePWDModel(pwd);
        registerModel(UpdatePWDModel.UPDATE_PWD_TAG, updatePWDModel);
    }

    public void setNewPwd() {
        getCachedDataAndLoad(UpdatePWDModel.UPDATE_PWD_TAG);
    }

    public MutableLiveData<String> upDateResult() {
        return getDataLiveData(UpdatePWDModel.UPDATE_PWD_TAG);
    }

    public MutableLiveData<String> getPwd() {
        return pwd;
    }
}
