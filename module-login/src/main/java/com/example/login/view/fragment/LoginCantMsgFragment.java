package com.example.login.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.base.view.fragment.BaseNoFreshDataBindingFragment;
import com.example.lib_data.AppCurrentUser;
import com.example.login.R;
import com.example.login.databinding.FragmentLoginCantMsgBinding;
import com.example.login.view.LoginActivity;


public class LoginCantMsgFragment extends BaseNoFreshDataBindingFragment<FragmentLoginCantMsgBinding> {

    @Override
    protected void initView() {
        mBaseDataBinding.loginTxtCantMsgPhone.setText(AppCurrentUser.getInstance().getLoginPhone());
        mBaseDataBinding.loginBtnBackToMain.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_cant_msg;
    }
}
