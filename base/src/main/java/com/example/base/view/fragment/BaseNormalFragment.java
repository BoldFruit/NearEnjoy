package com.example.base.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.base.network.NetType;
import com.example.base.network.NetworkManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Time:2020/2/23 15:41
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:适用于普通的没有网络请求的fragment
 */
public abstract class BaseNormalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);

        beforeReturnView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        observeNetType();
    }

    private void observeNetType() {
        if (!(NetworkManager.getInstance().getNetTypeLiveData() == null)) {
            NetworkManager.getInstance().getNetTypeLiveData().observe(getViewLifecycleOwner(), netType -> {
                onNetTypeChange(netType);
            });
        }
    }

    private void onNetTypeChange(NetType netType) {

    }

    protected abstract void initView();

    protected abstract void initData();

    protected void beforeReturnView() {

    }

    protected abstract int getLayoutId();
}
