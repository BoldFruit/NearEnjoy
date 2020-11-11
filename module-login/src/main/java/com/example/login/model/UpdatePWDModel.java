package com.example.login.model;
import com.example.base.model.BaseModel;
import com.example.login.api.LoginRepository;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;
import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/22 17:41
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class UpdatePWDModel extends BaseModel<String> {

    public static final String UPDATE_PWD_TAG = "update_pwd_model";

    private MutableLiveData<String> pwd;

    public UpdatePWDModel(MutableLiveData<String> pwd) {
        this.pwd = pwd;
    }

    @Override
    protected void load() {

        LoginRepository.getInstance().updatePwd(pwd.getValue(), new BaseObserver<String>(this) {
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
