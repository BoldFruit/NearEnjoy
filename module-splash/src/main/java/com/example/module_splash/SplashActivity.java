package com.example.module_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.arouter.ARouter;
import com.example.arouter.Constants;
import com.example.fw_annotations.BindPath;
import com.example.lib_common.linkage_kt.adapter.MainAdapter;
import com.example.lib_common.util.DensityUtil;
import com.example.lib_common.util.SPUtils;
import com.example.lib_common.util.statusbar.StatusBarUtil;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.module_splash.databinding.ActivitySplashBinding;
import com.example.network.HttpClient;
import com.example.network.observer.BaseObserver;
import com.example.network.response.ApiResponse;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@BindPath(Constants.ACTIVITY_SPLASH)
public class SplashActivity extends AppCompatActivity {
    private List<Fragment> mFragments;
    private ActivitySplashBinding mDataBinding;
    private CompositeDisposable mCompositeDisposable;
    //设置展示3秒开屏页
    private int time = 3;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        StatusBarUtil.hideStatusBar(this);
        //首次登陆时展示开屏页
        if (TokenManager.getIsFirstLogin()) {
            mFragments = new ArrayList<>();
            mFragments.add(new SplashFragment1());
            mFragments.add(new SplashFragment2());
            mFragments.add(new SplashFragment3());
            mDataBinding.splashViewpager2.setAdapter(new SplashViewPagerAdapter(this, mFragments));
            SPUtils.put(TokenManager.IS_FIRST_LOGIN, false);
        } else {
            //移除splash
            ((ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content))).removeView(mDataBinding.splashViewpager2);
            //进行网络请求查看是否有需要展示的页面
            getSplash();
        }
        mDataBinding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer !=null) {
                    mTimer.cancel();
                    ARouter.getInstance().startActivity(SplashActivity.this, Constants.ACTIVITY_MAIN);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    /**
     * 网络请求获取
     */
    private void getSplash() {
        HttpClient.getInstance()
                .createService(ApiService.class)
                .getSplash("1", "1")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse<List<GetSplashBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mCompositeDisposable == null) {
                            mCompositeDisposable = new CompositeDisposable();
                        }
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ApiResponse<List<GetSplashBean>> mListApiResponse) {
                        List data = mListApiResponse.getData();
                        if (data != null && !data.isEmpty()) {
                            initSplashData(data);
                        } else {
                            ARouter.getInstance().startActivity(SplashActivity.this, Constants.ACTIVITY_MAIN);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //错误的话直接跳转到首页
                        ARouter.getInstance().startActivity(SplashActivity.this, Constants.ACTIVITY_MAIN);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 初始化返回的数据
     * @param mData
     */
    private void initSplashData(List<GetSplashBean> mData) {
        GetSplashBean data = mData.get(0);
        if (data.isShowed()) {
            Bitmap bg = getBitmap(data.getBgImage());
            Bitmap upperImage = getBitmap(data.getUpperImage());
            DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
            //创建纯色背景
            Bitmap bgColor = Bitmap.createBitmap(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_4444);
            bgColor.eraseColor(Color.parseColor(data.getBgColor()));
            //组合三张图片
            Canvas mCanvas = new Canvas(bgColor);
            mCanvas.drawBitmap(bg, new Matrix(), null);
            mCanvas.drawBitmap(upperImage, new Matrix(), null);
            mDataBinding.container.setBackground(new BitmapDrawable(bgColor));
            mDataBinding.txtSkip.setVisibility(View.VISIBLE);
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (time > 0) {
                        mDataBinding.txtSkip.setText(String.format(getResources().getString(R.string.splash_skip_minute), time--));
                    } else {
                        mTimer.cancel();
                        ARouter.getInstance().startActivity(SplashActivity.this, Constants.ACTIVITY_MAIN);
                    }
                }
            }, 1000);
        } else {
            ARouter.getInstance().startActivity(SplashActivity.this, Constants.ACTIVITY_MAIN);
        }
    }

    /**
     * 根据url获得bitmap
     * @param url
     * @return
     */
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
