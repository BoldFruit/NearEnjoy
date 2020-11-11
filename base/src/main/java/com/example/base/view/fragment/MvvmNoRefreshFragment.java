package com.example.base.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.base.view.INoRefresh;
import com.example.base.viewmodel.MvvmNetworkViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

/**
 * Time:2020/2/25 9:07
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class MvvmNoRefreshFragment<D extends ViewDataBinding, V extends MvvmNetworkViewModel>
        extends MvvmNetworkFragment<D, V> {

    private View lastView = null;
    protected boolean isViewInit = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (lastView == null) {
            lastView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return lastView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!isViewInit) {
            super.onViewCreated(view, savedInstanceState);
            isViewInit = true;
        }
    }


}
