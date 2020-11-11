package com.example.login.model;

import com.example.base.model.BaseModel;
import com.example.base.model.bean.BaseCachedData;
import com.example.lib_data.AppCurrentUser;
import com.example.lib_data.AppDataBaseManager;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.login.api.LoginRepository;
import com.example.login.viewmodel.LoginViewModel;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import androidx.lifecycle.MutableLiveData;

/**
 * @author YangZhaoxin.
 * @since 2020/2/1 13:42.
 * email yangzhaoxin@hrsoft.net.
 */

public class LoginModel extends BaseModel<LoginResponseBean> {

    public static final String tagName = "LoginModel";
    private MutableLiveData<LoginViewModel.LoginType> mType;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> mCodeOrPassword;
    private MutableLiveData<Integer> schoolId;

    public LoginModel(MutableLiveData<LoginViewModel.LoginType> mType,
                      MutableLiveData<String> phone,
                      MutableLiveData<String> code,
                      MutableLiveData<Integer> schoolId) {
        this.mType = mType;
        this.phone = phone;
        this.mCodeOrPassword = code;
        this.schoolId = schoolId;
    }

    @Override
    protected void load() {
        if (mType.getValue() == LoginViewModel.LoginType.SMS) {
            LoginRepository.getInstance().loginBySMS(
                    schoolId.getValue(),
                    phone.getValue(),
                    mCodeOrPassword.getValue(),
                    new BaseObserver<LoginResponseBean>(this) {
                        @Override
                        public void onError(ApiException e) {
                            loadFail(e.getMessage());
                        }

                        @Override
                        public void onNext(LoginResponseBean loginResponseBean) {
                            loadSuccess(loginResponseBean);
                        }
                    }

            );
        } else {
            LoginRepository.getInstance().loginByPWD(
                    phone.getValue(),
                    mCodeOrPassword.getValue(),
                    new BaseObserver<LoginResponseBean>(this) {
                        @Override
                        public void onError(ApiException e) {
                            loadFail(e.getMessage());
                        }

                        @Override
                        public void onNext(LoginResponseBean loginResponseBean) {
                            loadSuccess(loginResponseBean);
                        }
                    }
            );

        }
    }

    @Override
    protected boolean isSaveToDataBase() {
        return true;
    }

    @Override
    protected void saveDataToDataBase(BaseCachedData<LoginResponseBean> data) {
        TokenManager.saveToken(data.getData().getToken());
        try {
            AppDataBaseManager.getDataBase().getUserDao().insertUser(data.getData().getUser());
            AppCurrentUser.getInstance().setCurrentUser(data.getData().getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
