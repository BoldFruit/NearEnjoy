package com.example.module_splash;

import android.view.View;

import com.example.arouter.ARouter;
import com.example.arouter.Constants;
import com.example.base.view.fragment.BaseNoFreshDataBindingFragment;
import com.example.base.view.fragment.BaseNormalFragment;
import com.example.module_splash.databinding.FragmentSplash3Binding;

/**
 * Time:2020/2/28 14:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SplashFragment3 extends BaseNoFreshDataBindingFragment<FragmentSplash3Binding> {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mBaseDataBinding.splashBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通往登陆界面
                ARouter.getInstance().startActivity(getActivity(), Constants.ACTIVITY_APP_LOGIN);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash3;
    }
}
