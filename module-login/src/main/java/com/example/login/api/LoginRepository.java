package com.example.login.api;
import android.os.HardwarePropertiesManager;

import com.example.lib_common.linkage.multi_link.thr_link.SchoolData;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.api.requestbody.LoginByPWDRequestBody;
import com.example.login.api.requestbody.LoginBySMSRequestBody;
import com.example.login.api.requestbody.UpdatePWDRequestBody;
import com.example.login.api.requestbody.UpdatePhoneRequestBody;
import com.example.login.new_part.QuestionBean;
import com.example.network.HttpClient;
import com.example.network.response.ApiResponse;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.Scope;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 18:04.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class LoginRepository {

    private LoginApi mApi;

    private LoginRepository(LoginApi api) {
        mApi = api;
    }

    private static class InstanceHolder {
        private static final LoginRepository INSTANCE = new LoginRepository(HttpClient.getInstance().createService(LoginApi.class));
    }

    public static LoginRepository getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void sendVerifyCode(String phone, Observer<String> observer) {
        HttpClient.getInstance().apiSubscribe(mApi.sendVerifyCode(phone), observer);
    }


    public void loginBySMS(int schoolId,
                           String phone,
                           String smsCode,
                           Observer<LoginResponseBean> observer) {
//        HttpClient.getInstance().
//                apiSubscribe(mApi.loginBySMS(schoolId, phone, smsCode), observer);
        HttpClient.getInstance().apiSubscribe(mApi.loginBySms(new LoginBySMSRequestBody(schoolId, phone, smsCode)), observer);
    }

    public void loginByPWD(String phone,
                           String pwd,
                           Observer<LoginResponseBean> observer) {
//        HttpClient.getInstance()
//        .apiSubscribe(mApi.loginByPWD(phone, pwd), observer);
        HttpClient.getInstance()
                .apiSubscribe(mApi.loginByPwd(new LoginByPWDRequestBody(phone, pwd)), observer);


    }

    public void updatePwd(String pwd, Observer<String> observer) {
        HttpClient.getInstance().apiSubscribe(mApi.updatePwd(new UpdatePWDRequestBody(pwd)), observer);
    }

    public void updatePhone(String phone, String smsCode, Observer<String> observer) {
        HttpClient.getInstance().apiSubscribe(mApi.updatePhone(new UpdatePhoneRequestBody(phone, smsCode)), observer);
    }


    public void getQuestions(String schoolId, String page, String size, Observer<List<QuestionBean>> observer, Scope scope) {
        Observable<ApiResponse<List<QuestionBean>>> questions = mApi.getQuestions(schoolId, page, size);
        questions.as(RxLife.as(scope));
        HttpClient.getInstance().apiSubscribe(questions, observer);
    }


    public void getSchools(Observer<List<SchoolData>> observer, Scope scope) {
        Observable<ApiResponse<List<SchoolData>>> schools = mApi.getSchools();
        schools.as(RxLife.as(scope));
        HttpClient.getInstance().apiSubscribe(schools, observer);
    }

}
