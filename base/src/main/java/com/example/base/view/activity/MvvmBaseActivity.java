package com.example.base.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.example.base.viewmodel.IMvvmBaseViewModel;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Time:2020/1/23 22:59
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

public abstract class MvvmBaseActivity<V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
        extends AppCompatActivity {

    protected VM mViewModel;
    protected V mViewDataBinding;


    public abstract @LayoutRes int getLayoutId();

    public abstract Class<? extends ViewModel> getViewModel();
    public abstract int getBindingVariable();

    /**
     * 初始化参数,通常用来得到从之前View传来的Bundle等数据
     */
    protected abstract void initParameters();

    protected abstract void initDataAndView();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //静止屏幕翻转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initParameters();
        initViewModel();
        performDataBinding();
        initDataAndView();
        setUIReaction();
    }

    /**
     * TODO: 将基础的UI展示事件与ViewModel中的LiveData构成观察，一旦ViewModel中的相关LiveData变动，则会触发相应方法
     */
    protected void setUIReaction() {

    }

    /**
     * TODO: 绑定ViewModel,解绑viewModel
     */
    protected void initViewModel() {
        if (getViewModel() != null) {
            mViewModel = (VM) new ViewModelProvider(this).get(getViewModel());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 绑定dataBinding
     */
    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());

        // 进行数据绑定
        if (getBindingVariable() > 0) {
            mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        }
        mViewDataBinding.executePendingBindings();
    }

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null, null);
    }

    public void startActivity(Class<?> clazz,String key, Bundle bundle) {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (bundle != null && key != null && !key.isEmpty()) {
                intent.putExtra(key, bundle);
            }
            startActivity(intent);
        } else {
            throw new NullPointerException();
        }
    }
}

