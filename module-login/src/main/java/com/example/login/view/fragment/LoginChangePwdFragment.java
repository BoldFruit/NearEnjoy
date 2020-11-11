package com.example.login.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.lib_common.util.ChangeEditContentUtil;
import com.example.lib_common.util.EditCheckUtil;
import com.example.lib_data.AppCurrentUser;
import com.example.login.R;
import com.example.login.databinding.LoginFragmentChangePwdBinding;
import com.example.login.util.MultiBooleanWatcher;
import com.example.login.view.LoginActivity;
import com.example.login.view.LoginProblemsActivity;
import com.example.login.view.ui.LoginRelativeBtn;
import com.example.login.viewmodel.LoginChangePwdViewModel;

public class LoginChangePwdFragment extends MvvmNetworkFragment<LoginFragmentChangePwdBinding, LoginChangePwdViewModel> {

    public static final String CHANG_PWD_TAG = "change_pwd";
    public static final String CHANG_PWD_TYPE_TAG = "change_pwd_type";
    public static final String CHANG_PWD_PHONE_TAG = "change_pwd_phone";
    private MutableLiveData<Boolean> isFirstPwdEdtNull;
    private MutableLiveData<Boolean> isSecondPwdEdtNull;
    private MultiBooleanWatcher multiBooleanWatcher;

    @Override
    public int getLayoutId() {
        return R.layout.login_fragment_change_pwd;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginChangePwdViewModel.class;
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

        ((LoginProblemsActivity)getActivity()).changeTitle("更换密码");

        multiBooleanWatcher = new MultiBooleanWatcher();
        isFirstPwdEdtNull = new MutableLiveData<>(true);
        isSecondPwdEdtNull = new MutableLiveData<>(true);

        ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtChangePwd, '*');
        ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtChangePwdConfirm, '*');

        EditCheckUtil.checkNull(mViewDataBinding.loginEdtChangePwd, isFirstPwdEdtNull, new EditCheckUtil.OnTextChange() {
            @Override
            public void before(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onChange(CharSequence s, int start, int before, int count) {
                mViewDataBinding.loginTxtChangePwdErrorDeal.setVisibility(View.GONE);
            }

            @Override
            public void after(Editable s) {

            }
        });
        EditCheckUtil.checkNull(mViewDataBinding.loginEdtChangePwdConfirm, isSecondPwdEdtNull);

        multiBooleanWatcher.setWatcher(false, isFirstPwdEdtNull, isSecondPwdEdtNull);
        multiBooleanWatcher.getWatcherLiveData().observe(this, aBoolean -> {
            mViewDataBinding.loginBtnChangePwd.setType(aBoolean ? LoginRelativeBtn.BtnType.ENABLE : LoginRelativeBtn.BtnType.NORMAL);
        });

        mViewDataBinding.loginBtnChangePwd.setOnClickListener(v -> {

            if (!EditCheckUtil.isContentSame(mViewDataBinding.loginEdtChangePwd, mViewDataBinding.loginEdtChangePwdConfirm)) {
                mViewDataBinding.loginTxtChangePwdErrorDeal.setVisibility(View.VISIBLE);
                mViewDataBinding.loginTxtChangePwdErrorDeal.setText("两次密码不同，请重新设置");
            } else {
                //TODO:开始请求更改密码
                mViewModel.getPwd().setValue(mViewDataBinding.loginEdtChangePwdConfirm.getText().toString().trim());
                mViewModel.updatePWD();
            }

        });

        mViewModel.getChangeResult().observe(this, s -> {
            //TODO:打开密码登陆界面
            Bundle bundle = new Bundle();
            bundle.putString(CHANG_PWD_TYPE_TAG, "pwd");
            bundle.putString(CHANG_PWD_PHONE_TAG, AppCurrentUser.getInstance().getLoginPhone());
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra(CHANG_PWD_TAG, bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
