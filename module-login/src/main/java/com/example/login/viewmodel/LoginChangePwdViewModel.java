package com.example.login.viewmodel;

import com.example.base.livedata.UnPeekLiveData;
import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.login.model.UpdatePWDModel;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/24 9:45
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:更换密码
 */
public class LoginChangePwdViewModel extends MvvmNetworkViewModel {

    private MutableLiveData<String> pwd;
    private UpdatePWDModel updatePWDModel;
    private UnPeekLiveData<String> changeResult;

    @Override
    protected void initModels() {
        pwd = new MutableLiveData<>();
        updatePWDModel = new UpdatePWDModel(pwd);
        changeResult = new UnPeekLiveData<>();
        changeResult = updatePWDModel.getUnPeekLiveData();
        registerModel(UpdatePWDModel.UPDATE_PWD_TAG, updatePWDModel);
    }

    public void updatePWD() {
        getCachedDataAndLoad(UpdatePWDModel.UPDATE_PWD_TAG);
    }

    public MutableLiveData<String> getPwd() {
        return pwd;
    }

    public UnPeekLiveData<String> getChangeResult() {
        return changeResult;
    }
}
