package com.example.lib_neuqer_chat_ui.emoji;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Time:2020/3/22 7:31
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EmojiPagerAdapter extends PagerAdapter {

    private List<View> dataList = new ArrayList<>();

    public EmojiPagerAdapter(List<View> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(dataList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(dataList.get(position));
        return (dataList.get(position));
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
