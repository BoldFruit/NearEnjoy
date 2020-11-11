package com.example.mvvm_simple.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.arouter.ARouter;
import com.example.base.utils.ToastUtil;
import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.base.viewmodel.MvvmNetworkViewModel;
import com.example.lib_common.SPUtils;
import com.example.lib_common.statusbar.StatusBarUtil;
import com.example.mvvm_simple.R;
import com.example.mvvm_simple.databinding.ActivityMainBinding;
import com.example.mvvm_simple.viewmodel.WelcomeViewModel;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

public class WelcomeActivity extends AppCompatActivity {

    public static final String IS_FIRST = "is_first";
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorWelcome));
        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            if (!SPUtils.get(IS_FIRST, false)) {
                ARouter.getInstance().startActivity(this, "splash/SplashActivity");
                finish();
            } else {
                ToastUtil.show(this, "打开广告界面");
            }
        }, 1000);

    }
}
