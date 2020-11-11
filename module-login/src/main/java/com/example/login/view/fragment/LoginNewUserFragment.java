package com.example.login.view.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.utils.ToastUtil;
import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.lib_common.util.ChangeEditContentUtil;
import com.example.login.R;
import com.example.login.databinding.LoginLayoutFragmentNewUserBinding;
import com.example.login.view.ui.LoginRelativeBtn;
import com.example.login.viewmodel.LoginNewUserViewModel;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Time:2020/2/21 22:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginNewUserFragment extends MvvmNetworkFragment<LoginLayoutFragmentNewUserBinding, LoginNewUserViewModel> {

    private MutableLiveData<Boolean> isPasswordVisible;
    private MediatorLiveData<Boolean> isLoginBtn;
    private MutableLiveData<Boolean> isPhoneComplete;
    private MutableLiveData<Boolean> isPasswordNull;

    private MutableLiveData<String> pwd;

    @Override
    public int getLayoutId() {
        return R.layout.login_layout_fragment_new_user;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginNewUserViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected String getFragmentTag() {
        return null;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {

        pwd = mViewModel.getPwd();

        isPasswordVisible = new MutableLiveData<>();
        isPasswordVisible.setValue(false);

        isPhoneComplete = new MutableLiveData<>();
        isPhoneComplete.setValue(false);

        isPasswordNull = new MutableLiveData<>();
        isPasswordNull.setValue(true);

        isLoginBtn = new MediatorLiveData<>();

        addListenerToLoginBtn();

        isPasswordVisible.observe(this, aBoolean -> {
            mViewDataBinding.loginImgNewUserEye.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            mViewDataBinding.loginImgNewUserBlindEye.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
            if (aBoolean) {
//                mViewDataBinding.loginEdtNewUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ChangeEditContentUtil.clearEditTransformation(mViewDataBinding.loginEdtNewUserPassword);
            } else {
                ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtNewUserPassword, '*');
            }

//            //设置transformationMethod后会产生光标前置的bug，每次转换transformationMethod后都要设置光标位置
//            mViewDataBinding.loginEdtNewUserPassword.setSelection(mViewDataBinding.
//                    loginEdtNewUserPassword.getText().toString().length());

        });

       mViewDataBinding.loginImgNewUserEyePart.setOnClickListener(v -> {
           isPasswordVisible.setValue(!isPasswordVisible.getValue());
       });

       addListenerToEdit();

       addObserverToUpdate();
    }

    private void addObserverToUpdate() {

        mViewModel.upDateResult().observe(this, s -> {
            ToastUtil.show(getContext(), "设置成功");
            //TODO:进入主界面
        });
    }

    private void addListenerToLoginBtn() {
        isLoginBtn.addSource(isPasswordNull, aBoolean -> {
            isLoginBtn.setValue((!aBoolean) && isPhoneComplete.getValue());
        });

        isLoginBtn.addSource(isPhoneComplete, aBoolean -> {
            isLoginBtn.setValue((!isPasswordNull.getValue()) && aBoolean);
        });

        isLoginBtn.observe(this, aBoolean -> {

            isLoginBtnClickable(aBoolean);

        });

        mViewDataBinding.loginBtnNewUser.setOnClickListener(v -> {
            pwd.setValue(mViewDataBinding.loginEdtNewUserPassword.getText().toString().trim());
            mViewModel.setNewPwd();
        });
    }

    private void isLoginBtnClickable(Boolean aBoolean) {
        mViewDataBinding.loginBtnNewUser.setType(aBoolean ? LoginRelativeBtn.BtnType.ENABLE : LoginRelativeBtn.BtnType.NORMAL);
    }

    private void addListenerToEdit() {
        mViewDataBinding.loginEdtPhoneNewUser.setOnPhoneEditTextChangeListener((s, isEleven) -> {
            isPhoneComplete.setValue(isEleven);
        });

        mViewDataBinding.loginEdtNewUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordNull.setValue((s.toString().length() == 0));
            }
        });
    }

    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {
        ToastUtil.show(getContext(), "完成完成");
    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
