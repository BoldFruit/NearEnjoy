package com.example.base.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Time:2020/2/23 14:34
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: fragment出栈返回上一页时，上一页的fragment会重新走onCreateView方法
 * 而我们的很多view和数据初始化工作都是在onViewCreated之后进行的，导致的结果是每
 * 次回上一个页面可能会重新刷新，这一点体验很差。这里提供一个方法来避免每次重新创建view。
 */
public abstract class BaseNavNoRefreshFragment extends BaseNormalFragment {

    private View lastView = null;
    protected boolean isViewInit = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (lastView == null) {
            lastView = super.onCreateView(inflater, container, savedInstanceState);
        }
        beforeReturn();
        return lastView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       if (!isViewInit) {
           super.onViewCreated(view, savedInstanceState);
           isViewInit = true;
       }
    }

    /**
     * 有些数据需要在onCreateView处理，但是由于这种Fragment无法重新绘制，故
     * 用这个方式来处理每次onCreateView里需要进行的数据处理
     */
    protected void beforeReturn() {

    }
}
