package com.example.module_splash;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Time:2020/2/28 14:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SplashViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments = new ArrayList<>();

    public SplashViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.mFragments = list;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
