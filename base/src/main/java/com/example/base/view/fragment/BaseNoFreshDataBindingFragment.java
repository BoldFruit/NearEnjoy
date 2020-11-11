package com.example.base.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Time:2020/2/23 16:18
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class BaseNoFreshDataBindingFragment<D extends ViewDataBinding> extends BaseNavNoRefreshFragment {

    protected D mBaseDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mBaseDataBinding = DataBindingUtil.bind(view);
        return view;
    }
}
