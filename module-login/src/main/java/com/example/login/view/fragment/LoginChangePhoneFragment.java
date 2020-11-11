package com.example.login.view.fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.lib_common.util.ChangeEditContentUtil;
import com.example.lib_common.util.EditCheckUtil;
import com.example.login.R;
import com.example.login.databinding.LoginFragmentChangePhoneBinding;
import com.example.login.util.MultiBooleanWatcher;
import com.example.login.view.LoginProblemsActivity;
import com.example.login.view.ui.LoginRelativeBtn;
import com.example.login.viewmodel.LoginChangePhoneViewModel;


public class LoginChangePhoneFragment extends MvvmNetworkFragment<LoginFragmentChangePhoneBinding, LoginChangePhoneViewModel> {

    private MutableLiveData<Boolean> isPhoneNull;
    private MutableLiveData<Boolean> isPwdNull;
    private MultiBooleanWatcher multiBooleanWatcher;

    @Override
    public int getLayoutId() {
        return R.layout.login_fragment_change_phone;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginChangePhoneViewModel.class;
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
        ((LoginProblemsActivity)getActivity()).changeTitle("更换手机号");
        isPhoneNull = new MutableLiveData<>(true);
        isPwdNull = new MutableLiveData<>(true);
        multiBooleanWatcher = new MultiBooleanWatcher();

        ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtChangePhonePassword, '*');

        EditCheckUtil.checkNull(mViewDataBinding.loginEdtChangePhoneOriginalPhone, isPhoneNull);
        EditCheckUtil.checkNull(mViewDataBinding.loginEdtChangePhonePassword, isPwdNull);

        multiBooleanWatcher.setWatcher(false, isPhoneNull, isPwdNull);

        multiBooleanWatcher.getWatcherLiveData().observe(this, aBoolean -> {
            mViewDataBinding.loginBtnChangePhoneNext.setType(aBoolean ? LoginRelativeBtn.BtnType.ENABLE : LoginRelativeBtn.BtnType.NORMAL);
        });

        mViewDataBinding.loginBtnChangePhoneNext.setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_loginChangePhoneFragment_to_loginBindPhoneFragment);
            mViewModel.getPhone().setValue(mViewDataBinding.loginEdtChangePhoneOriginalPhone.getPhoneText());
            mViewModel.getPassword().setValue(mViewDataBinding.loginEdtChangePhonePassword.getText().toString().trim());
            mViewModel.loginByPwd();
        });

        mViewDataBinding.loginTxtChangePhoneForget.setOnClickListener(v -> {
            if (Navigation.findNavController(v).getCurrentDestination().getId() == R.id.loginChangePhoneFragment) {
                Navigation.findNavController(v).navigate(R.id.action_loginChangePhoneFragment_to_login_fragment_find_password);
            }
        });

        mViewModel.getResponseData().observe(this, loginResponseBean -> {
            if (Navigation.findNavController(mViewDataBinding.loginBtnChangePhoneNext).getCurrentDestination().getId() == R.id.loginChangePhoneFragment) {
                Navigation.findNavController(mViewDataBinding.loginBtnChangePhoneNext).navigate(R.id.action_loginChangePhoneFragment_to_loginBindPhoneFragment);
            }

        });



    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
