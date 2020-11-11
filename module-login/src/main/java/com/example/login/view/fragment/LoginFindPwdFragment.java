package com.example.login.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.utils.ToastUtil;
import com.example.base.view.fragment.MvvmNoRefreshFragment;
import com.example.lib_common.textview.CountDownTv;
import com.example.lib_data.AppCurrentUser;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.R;
import com.example.login.databinding.LoginFragmentFindPwdBinding;
import com.example.login.model.LoginModel;
import com.example.login.view.LoginActivity;
import com.example.login.view.LoginProblemsActivity;
import com.example.login.view.ui.LoginRelativeBtn;
import com.example.login.view.ui.MultiVerifyRelativeLayout;
import com.example.login.viewmodel.LoginFindPwdViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

/**
 * Time:2020/2/23 9:34
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:注意，我这里使用的是MvvmNoRefreshFragment
 */
public class LoginFindPwdFragment extends MvvmNoRefreshFragment<LoginFragmentFindPwdBinding, LoginFindPwdViewModel> {

    public static final String FIND_PASSWORD_TAG = "find_password";
    private MutableLiveData<Boolean> isPhoneComplete;
    private MutableLiveData<Boolean> isVerifyCodeNull;
    private MutableLiveData<Boolean> isCountDown;
    private MediatorLiveData<Boolean> isBtnNextEnable;

    private MutableLiveData<Boolean> text;

    @Override
    public int getLayoutId() {
        return R.layout.login_fragment_find_pwd;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginFindPwdViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected String getFragmentTag() {
        return FIND_PASSWORD_TAG;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {

        ((LoginProblemsActivity)getActivity()).changeTitle("找回登陆密码");

        mViewModel.getResponse().observe(this, loginResponseBean -> {

            //暂时存储用户的手机号
            AppCurrentUser.getInstance().setLoginPhone(mViewDataBinding.loginEdtFindPwdPhone.getPhoneText());
            //如果是新用户，开启登陆界面的新用户登陆
            if (loginResponseBean.isNewUser()) {
                Bundle bundle = new Bundle();
                bundle.putString(FIND_PASSWORD_TAG, "newUser");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra(FIND_PASSWORD_TAG, bundle);
                startActivity(intent);
            } else {
                Navigation.findNavController(mViewDataBinding.loginBtnFindPwdNext).
                        navigate(R.id.action_login_fragment_find_password_to_loginChangePwdFragment);
            }
        });

        isPhoneComplete = new MutableLiveData<>(false);
        isVerifyCodeNull = new MutableLiveData<>(true);
        isCountDown = new MutableLiveData<>(false);
        isBtnNextEnable = new MediatorLiveData<>();

        isBtnNextEnable.addSource(isPhoneComplete, aBoolean -> {
            isBtnNextEnable.setValue(aBoolean && (!isVerifyCodeNull.getValue()));
        });

        isBtnNextEnable.addSource(isVerifyCodeNull, aBoolean -> {
            isBtnNextEnable.setValue(!aBoolean && isPhoneComplete.getValue());
        });

        isBtnNextEnable.observe(this, aBoolean -> {
            mViewDataBinding.loginBtnFindPwdNext.setType(aBoolean ? LoginRelativeBtn.BtnType.ENABLE : LoginRelativeBtn.BtnType.NORMAL);
        });

        addListenerToPhone();
        addListenerToVerifyCode();
        addListenerToVerifyLayout();

        mViewDataBinding.loginBtnFindPwdNext.setOnClickListener(v -> {

            //TODO:验证码登陆
            mViewModel.getSmsCode().setValue(mViewDataBinding.loginEdtFindPwdVerifyCode.getText().toString().trim());
            mViewModel.loginBySms();

        });
    }

    private void addListenerToVerifyCode() {
        mViewDataBinding.loginEdtFindPwdVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isVerifyCodeNull.setValue(s.toString().length() == 0);
            }
        });
    }

    private void addListenerToVerifyLayout() {
        mViewDataBinding.loginFindPwdMultiLayout.setBtnClickListener(v -> {
            mViewDataBinding.loginFindPwdMultiLayout.setType(MultiVerifyRelativeLayout.MultiVerifyType.COUNT_DOWN);
            //TODO:开始请求验证码
            mViewModel.getPhone().setValue(mViewDataBinding.loginEdtFindPwdPhone.getPhoneText());
            mViewModel.getVerifyCode();


        }).setCallbackForCountDown(new CountDownTv.CountDownListener() {
            @Override
            public void onFinish(TextView textView) {
                isCountDown.setValue(false);
                mViewDataBinding.loginFindPwdMultiLayout.setType(isPhoneComplete.getValue() ?
                        MultiVerifyRelativeLayout.MultiVerifyType.ENABLE :
                        MultiVerifyRelativeLayout.MultiVerifyType.NORMAL);
            }

            @Override
            public void onTick(Long time) {

            }

            @Override
            public void beforeStart() {
                isCountDown.setValue(true);
            }
        }).setCountDown(60 * 1000, 1000, "s").allReady();


    }

    private void addListenerToPhone() {
        isPhoneComplete.observe(this, aBoolean -> {
            //不再倒计时的时候，输入框的变动才会影响获得验证码的按钮
           if (!isCountDown.getValue()) {
               mViewDataBinding.loginFindPwdMultiLayout.setType(aBoolean ?
                       MultiVerifyRelativeLayout.MultiVerifyType.ENABLE
                       : MultiVerifyRelativeLayout.MultiVerifyType.NORMAL);
           }
        });

        mViewDataBinding.loginEdtFindPwdPhone.setOnPhoneEditTextChangeListener((s, isEleven) -> {
            isPhoneComplete.setValue(isEleven);
        });

    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {
//        if (key.equals(LoginModel.tagName)) {
//            Navigation.findNavController(mViewDataBinding.loginBtnFindPwdNext).
//                    navigate(R.id.action_login_fragment_find_password_to_loginChangePwdFragment);
//        }
    }

    @Override
    public void onNetFailed(String key, BaseNetworkStatus status) {
        if (key.equals(LoginModel.tagName)) {
            ToastUtil.show(getContext(), "验证失败");
        }
    }
}
