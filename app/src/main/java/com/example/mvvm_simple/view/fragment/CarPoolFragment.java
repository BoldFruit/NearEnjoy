package com.example.mvvm_simple.view.fragment;

import com.example.base.view.fragment.LazyMvvmNetworkFragment;
import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.mvvm_simple.R;
import com.example.mvvm_simple.databinding.FragmentCarpoolBinding;
import com.example.mvvm_simple.viewmodel.CarPoolViewModel;

import androidx.lifecycle.ViewModel;

/**
 * Time:2020/3/2 11:31
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class CarPoolFragment extends LazyMvvmNetworkFragment<FragmentCarpoolBinding, CarPoolViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_carpool;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return CarPoolViewModel.class;
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

    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
