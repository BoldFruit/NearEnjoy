package com.example.login.view.fragment;
import com.example.base.view.fragment.BaseNoFreshDataBindingFragment;
import com.example.login.R;
import com.example.login.databinding.LoginFragmentProblemsBinding;
import com.example.login.view.LoginProblemsActivity;

import androidx.navigation.Navigation;

/**
 * Time:2020/2/23 16:15
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginProblemsFragment extends BaseNoFreshDataBindingFragment<LoginFragmentProblemsBinding> {


    @Override
    protected void initView() {

        mBaseDataBinding.loginProblemsFindPwd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_login_problems_to_find_password);
        });

        mBaseDataBinding.loginProblemsCantReceive.setOnClickListener(v -> {

            //TODO:
            Navigation.findNavController(v).navigate(R.id.action_login_fragment_problems_to_loginCantMsgFragment);

        });

        //手机无法接收验证码
        mBaseDataBinding.loginProblemsPhoneProblem.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_login_fragment_problems_to_loginChangePhoneFragment);
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void beforeReturn() {
        ((LoginProblemsActivity)getActivity()).changeTitle("出现问题");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment_problems;
    }
}
