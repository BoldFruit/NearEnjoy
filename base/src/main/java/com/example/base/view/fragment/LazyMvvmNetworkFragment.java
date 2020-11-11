package com.example.base.view.fragment;

import android.os.Bundle;
import android.view.View;
import com.example.base.model.SuperBaseModel;
import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.network.NetType;
import com.example.base.network.NetworkManager;
import com.example.base.view.IBaseView;
import com.example.base.viewmodel.IMvvmNetworkViewModel;

import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

/**
 * @author DuLong
 * @since 2020/5/8
 * email 798382030@qq.com
 */
public abstract class LazyMvvmNetworkFragment<V extends ViewDataBinding, VM extends IMvvmNetworkViewModel> extends MvvmBaseFragment<V, VM> implements IBaseView {
    private boolean isVisible = false;
    private boolean isViewCreated = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViewModel();
        performDataBinding();
        observeNetWorkStatus();
        isViewCreated = true;
        lazyLoad();
    }

    /**
     * 检测网络状态方法
     */
    private void observeNetWorkStatus() {
        Map<String, SuperBaseModel> models = mViewModel.getModels();
        Iterator<String> iterator = models.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            models.get(key).getNetworkStatus().observe(getViewLifecycleOwner(), status -> {
                observeNet(key, (BaseNetworkStatus) status);
            });
        }

        // 检测网络状态
        if (!(NetworkManager.getInstance().getNetTypeLiveData() == null)) {
            NetworkManager.getInstance().getNetTypeLiveData().observe(getViewLifecycleOwner(), netType -> {
                onNetTypeChange(netType);
            });
        }
    }

    protected void observeNet(String key, BaseNetworkStatus status) {
        switch (status.getStatus()) {
            case LOADING:
                // TODO: 不知道会不会有冲突
                onNetLoading(key,  status);
                break;
            case DONE:
                onNetDone(key, status);
                break;
            case FAILED:
                onNetFailed(key, status);
                break;
            case NO_NETWORK:
                onNoNetwork(key, status);
                break;
            case INIT:
            default:
                onNetInit(key, status);
                break;
        }
    }

    protected void onNetTypeChange(NetType netType) {
        switch (netType) {
            case NONE:
                break;
            case WIFI:
                break;
            case CMWAP:
                break;
            case CMNET:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetInit(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetFailed(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetLoading(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNoNetwork(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mViewModel.detachModels();
    }

    /**
     * 在这个方法中可以监听Fragment是否可见
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) lazyLoad();
    }

    /**
     * 判断是否满足懒加载的条件
     */
    private void lazyLoad() {
        if (isVisible && isViewCreated) {
            loadData();
            //重置数据，防止重新创建反复创建Fragment
            isViewCreated = false;
            isVisible = false;
        }
    }

    /**
     * 进行数据的加载
     */
    private void loadData() {
        initDataAndView();
    }


    private void performDataBinding() {
        if (getBindingVariable() > 0) {
            mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        }
        mViewDataBinding.executePendingBindings();
    }
}
