package com.example.login.view.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.widget.TextView;

import com.example.base.utils.ToastUtil;
import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.lib_common.textview.CountDownTv;
import com.example.lib_common.util.EditCheckUtil;
import com.example.login.R;
import com.example.login.databinding.LoginFragmentBindPhoneBinding;
import com.example.login.util.MultiBooleanWatcher;
import com.example.login.view.LoginProblemsActivity;
import com.example.login.view.ui.LoginRelativeBtn;
import com.example.login.view.ui.MultiVerifyRelativeLayout;
import com.example.login.viewmodel.LoginBindPhoneViewModel;


public class LoginBindPhoneFragment extends MvvmNetworkFragment<LoginFragmentBindPhoneBinding, LoginBindPhoneViewModel> {

    private MutableLiveData<Boolean> isPhoneNull;
    private MutableLiveData<Boolean> isVerifyNull;
    private MultiBooleanWatcher watcher;

    @Override
    public int getLayoutId() {
        return R.layout.login_fragment_bind_phone;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginBindPhoneViewModel.class;
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

        ((LoginProblemsActivity)getActivity()).changeTitle("绑定新手机号");

        isPhoneNull = new MutableLiveData<>(true);
        isVerifyNull = new MutableLiveData<>(true);
        watcher = new MultiBooleanWatcher();

        EditCheckUtil.checkNull(mViewDataBinding.loginEdtBindOriginalPhone, isPhoneNull);
        EditCheckUtil.checkNull(mViewDataBinding.loginEdtBindPhoneVerifyCode, isVerifyNull);

        mViewModel.getBindResult().observe(this, s -> {
            ToastUtil.show(getContext(), s);
        });

        isPhoneNull.observe(this, aBoolean -> {
            mViewDataBinding.loginMultiVerifyBindPhone.setType(aBoolean ? MultiVerifyRelativeLayout.MultiVerifyType.NORMAL : MultiVerifyRelativeLayout.MultiVerifyType.ENABLE);
        });

        watcher.setWatcher(false, isPhoneNull, isVerifyNull);

        watcher.getWatcherLiveData().observe(this, aBoolean -> {

            mViewDataBinding.loginBtnBindPhoneComplete.setType(aBoolean ? LoginRelativeBtn.BtnType.ENABLE : LoginRelativeBtn.BtnType.NORMAL);

        });

        mViewDataBinding.loginBtnBindPhoneComplete.setOnClickListener(v -> {
            //TODO:绑定手机号
            mViewModel.getPhone().setValue(mViewDataBinding.loginEdtBindOriginalPhone.getPhoneText());
            mViewModel.getSmsCode().setValue(mViewDataBinding.loginEdtBindPhoneVerifyCode.getText().toString().trim());
            mViewModel.bindNewPhone();
        });

        mViewDataBinding.loginMultiVerifyBindPhone
                .setCountDown(60 * 1000, 1000, "s")
                .setBtnClickListener(v -> {
                    mViewDataBinding.loginMultiVerifyBindPhone.setType(MultiVerifyRelativeLayout.MultiVerifyType.COUNT_DOWN);
                }).setCallbackForCountDown(new CountDownTv.CountDownListener() {
            @Override
            public void onFinish(TextView textView) {
                if(watcher.getWatcherLiveData().getValue()) {
                    mViewDataBinding.loginMultiVerifyBindPhone.setType(MultiVerifyRelativeLayout.MultiVerifyType.ENABLE);
                } else {
                    mViewDataBinding.loginMultiVerifyBindPhone.setType(MultiVerifyRelativeLayout.MultiVerifyType.NORMAL);
                }
            }

            @Override
            public void onTick(Long time) {

            }

            @Override
            public void beforeStart() {

            }
        }).allReady();
    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
