package com.example.mvvm_simple.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.lib_common.widget.flowlayout.FlowLayout;
import com.example.lib_common.widget.flowlayout.TagAdapter;
import com.example.mvvm_simple.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import androidx.annotation.IntDef;

/**
 * @author DuLong
 * @since 2020/2/24
 * email 798382030@qq.com
 */
public class MyTagAdapter extends TagAdapter<String> {

    private Context mContext;
    private int mType;
    public static final int NORMAL = 0;
    public static final int HOT_TAG = 1;
    public static final int CLEAR_YELLOW_TAG = 2;
    public static final int YELLOW_TAG = 3;
    public static final int CLICKABLE_TAG = 4;
    public static final int SMALL_YELLOW_TAG = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOT_TAG, CLEAR_YELLOW_TAG, NORMAL, YELLOW_TAG, CLICKABLE_TAG, SMALL_YELLOW_TAG})
    public @interface TagType {}

    public MyTagAdapter(List<String> datas, Context context, @TagType int type) {
        super(datas);
        mContext = context;
        mType = type;

    }

    /**
     * @param parent 父布局
     * @param position  数据的索引
     * @param data data
     * @return tag的布局
     */
    @Override
    public View getView(FlowLayout parent, int position, String data) {
        LinearLayout mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.main_item_tag, parent, false);
        TextView mTextView = mLayout.findViewById(R.id.txt_tag_name);
        mTextView.setText(data);
        switch (mType) {
            case NORMAL:
                break;
            case SMALL_YELLOW_TAG:
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.main_item_small_tag_yellow, parent, false);
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setText(data);
                break;
            case YELLOW_TAG:
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_market_tag_yellow, parent, false);
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setText(data);
                break;
            case CLEAR_YELLOW_TAG:
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_idle_tag, parent, false);
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setText(data);
                break;
        }
        return mLayout;
    }


    private int dp2px(float dp) {
        return (int) (mContext.getResources().getSystem().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    public void onSelected(int position, View view) {
        super.onSelected(position, view);
    }
}
