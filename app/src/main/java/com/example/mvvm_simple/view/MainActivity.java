package com.example.mvvm_simple.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.arouter.ARouter;
import com.example.arouter.Constants;
import com.example.base.utils.ToastUtil;
import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.fw_annotations.BindPath;
import com.example.lib_common.bottomnavi.easynavigation.view.EasyNavigationBar;
import com.example.lib_common.util.DensityUtil;
import com.example.lib_common.util.statusbar.StatusBarUtil;
import com.example.lib_common.view.wave.WaveHelper;
import com.example.lib_common.view.wave.WaveView;
import com.example.mvvm_simple.R;
import com.example.mvvm_simple.databinding.ActivityMainBinding;
import com.example.mvvm_simple.model.bean.ClassificationBean;
import com.example.mvvm_simple.view.fragment.CarPoolFragment;
import com.example.mvvm_simple.view.fragment.MarketFragment;
import com.example.mvvm_simple.view.fragment.MessageFragment;
import com.example.mvvm_simple.view.fragment.MineFragment;
import com.example.mvvm_simple.view.paging.Status;
import com.example.mvvm_simple.viewmodel.CategoriesViewModel;
import com.example.mvvm_simple.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Inflater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.CompletableOnSubscribe;

/**
 * Time:2020/3/2 11:06
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@BindPath(Constants.ACTIVITY_MAIN)
public class MainActivity extends MvvmNetworkActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {
    //记录是否bottomSheet正在展示
    private boolean isShowingBottomSheet = false;
    private String[] tabs = {"市场", "拼", "消息", "我的"};
    private int[] mSelectedImg = {R.drawable.ic_market_click,
            R.drawable.ic_pin_click,
            R.drawable.ic_message_click,
            R.drawable.ic_mine_click};

    private int[] mNormalImg = {
            R.drawable.ic_market_nomal,
            R.drawable.ic_pin_nomal,
            R.drawable.ic_message_normal,
            R.drawable.ic_mine_nomal
    };

    public static final String CATEGORY_ID = " categoryId";
    public static void actionStart(Context mContext, ClassificationBean.SecondListBean id) {
        Intent mIntent = new Intent(mContext, MainActivity.class);
        mIntent.putExtra(CATEGORY_ID, id);
        mContext.startActivity(mIntent);
    }

    private WaveView mWave;

    private WaveHelper helper;

    private List<Fragment> fragments = new ArrayList<>();

    private View inflateView;

    /**
     * SingleTask启动模式下，无法在OnCreate中接收到intent的变化
     * 但可以在以下的函数监听到
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //当从发布成功后返回，应该刷新数据
        ClassificationBean.SecondListBean id = intent.getParcelableExtra(CATEGORY_ID);
        if (id != null) {
            notifyDataChanged(id);
        }
    }

    /**
     * 注意到有数据需要更新
     * 通知相应fragment更新数据
     * 注意该方法只能用于继承了FragmentStatePagerAdapter
     * @param mId
     */
    private void notifyDataChanged(ClassificationBean.SecondListBean mId) {
        ViewPager mViewPager = mViewDataBinding.navigationBar.getmViewPager();
        PagerAdapter mFragmentStatePagerAdapter = mViewPager.getAdapter();
        if (mFragmentStatePagerAdapter != null) {
            MarketFragment mMarketFragment = (MarketFragment)mFragmentStatePagerAdapter.instantiateItem(mViewPager, 0);
            mMarketFragment.notifyDataChanged(mId);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {
        //设置底部弹出的页面
        View bottomContent = LayoutInflater.from(this).inflate(R.layout.fragment_bottom_sheet, null);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_main_plus, null);
        //灰色蒙层
        View bg = mViewDataBinding.navigationBar.getBg();
        View bottomSheetView = mViewDataBinding.navigationBar.getBottomSheetView();
        //设置其中的点击事件
        bottomContent.findViewById(R.id.relayout_mine_left).setOnClickListener(this);
        mViewDataBinding.navigationBar.setBottomSheetLayout(bottomContent);
        mWave = view.findViewById(R.id.wave);
        mWave.setWaveColor(
                Color.parseColor("#CCffbf00"),
                Color.parseColor("#ffa500"));
        mWave.setBorder(8, R.color.app_grey_line);
        helper = new WaveHelper(mWave, new WaveHelper.AnimationListener() {
            @Override
            public void onWaveUp() {

            }

            @Override
            public void onWaveDownToOrigin() {
                helper.cancel();
            }
        });
        int distance = DensityUtil.dpToPx(this, 240);
        mWave.setOnClickListener(v -> {
            if (isShowingBottomSheet) {
                bg.setVisibility(View.GONE);
                bottomSheetView.animate().translationY(0).start();
                isShowingBottomSheet = false;
            } else {
                bg.setVisibility(View.VISIBLE);
                bottomSheetView.animate().translationY(-distance).start();
                isShowingBottomSheet = true;
            }
            helper.start();
        });

        bg.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            bottomSheetView.animate().translationY(0).start();
            isShowingBottomSheet = false;
        });
        fragments.add(new MarketFragment());
        fragments.add(new CarPoolFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MineFragment());
        mViewDataBinding.navigationBar
                .titleItems(tabs)
                .normalIconItems(mNormalImg)
                .selectIconItems(mSelectedImg)
                .fragmentList(fragments)
                .canScroll(false)
                .mode(EasyNavigationBar.MODE_ADD_VIEW)
                .addCustomView(view)
                .addAlignBottom(false)
                .navigationHeight(50)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener((view1, position) -> {
                    switch (position) {
                        case 0:
                        case 1:
                        case 3:
                            StatusBarUtil.setTranslucentStatus(this);
                            StatusBarUtil.setStatusBarDarkTheme(this, true);
                            break;
                        case 4:
                            StatusBarUtil.setTranslucentStatus(this);
                            break;
                    }
                    return false;
                }).build();
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relayout_mine_left:
                Intent mIntent = new Intent(this, ReleaseIdleActivity.class);
                startActivity(mIntent);
        }
    }
}
