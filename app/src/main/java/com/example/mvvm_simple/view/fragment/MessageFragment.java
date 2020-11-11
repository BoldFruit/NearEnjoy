package com.example.mvvm_simple.view.fragment;

import com.example.base.view.fragment.LazyMvvmNetworkFragment;
import com.example.base.view.fragment.MvvmNetworkFragment;
import com.example.mvvm_simple.R;
import com.example.mvvm_simple.databinding.FragmentMessageBinding;
import com.example.mvvm_simple.viewmodel.MessageViewModel;

import androidx.lifecycle.ViewModel;

/**
 * Time:2020/3/2 11:32
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class MessageFragment extends LazyMvvmNetworkFragment<FragmentMessageBinding, MessageViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return MessageViewModel.class;
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
