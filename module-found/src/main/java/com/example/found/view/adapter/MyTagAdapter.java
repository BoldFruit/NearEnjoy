package com.example.found.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import androidx.annotation.IntDef;

import com.example.lib_common.widget.flowlayout.FlowLayout;
import com.example.lib_common.widget.flowlayout.TagAdapter;
import com.example.found.R;

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
    public static final int CLEAR_TAG = 2;
    public static final int YELLOW_TAG = 3;
    public static final int CLICKABLE_TAG = 4;
    public static final int SMALL_YELLOW_TAG = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOT_TAG, CLEAR_TAG, NORMAL, YELLOW_TAG, CLICKABLE_TAG, SMALL_YELLOW_TAG})
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
        LinearLayout mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
        TextView mTextView = mLayout.findViewById(R.id.txt_tag_name);
        mTextView.setText(data);
        switch (mType) {
            case NORMAL:
                break;
            case YELLOW_TAG:
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_tag_yellow, parent, false);
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setText(data);
                break;
            case SMALL_YELLOW_TAG:
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_small_tag_yellow, parent, false);
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setText(data);
                break;
            case HOT_TAG:
                //在标签左端动态添加图标,只有前两个标签有
                if (position <= 1) {
                    ImageView image = new ImageView(mContext);
                    image.setImageResource(R.mipmap.ic_small_fire);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(16), dp2px(16));
                    lp.leftMargin = dp2px(2);
                    mLayout.addView(image, lp);
                }
                break;
            case CLEAR_TAG:
                ImageView mImageView = new ImageView(mContext);
                mImageView.setImageResource(R.drawable.ic_remove);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTagDatas.remove(position);
                        notifyDataChanged();
                    }
                });
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(dp2px(20), dp2px(20));
                mLayoutParams.leftMargin = dp2px(2);
                mLayout.addView(mImageView, mLayoutParams);
                break;
            case CLICKABLE_TAG:
                //初始化时设为未选中的状态
                mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_tag_yellow, parent, false);
                mLayout.setBackground(mContext.getDrawable(R.drawable.common_bg_tag_gray));
                mTextView = mLayout.findViewById(R.id.txt_tag_name);
                mTextView.setTextColor(mContext.getResources().getColor(R.color.txt_screen_gray));
                mTextView.setText(data);
                break;
        }
        return mLayout;
    }

    @Override
    public void onSelected(int position, View view) {
        super.onSelected(position, view);
        if (mType == CLICKABLE_TAG) {
            view.setBackground(mContext.getDrawable(R.drawable.bg_tag_yellow));
            TextView tagName = view.findViewById(R.id.txt_tag_name);
            tagName.setTextColor(mContext.getResources().getColor(R.color.bg_tag_yellow));
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void unSelected(int position, View view) {
        super.unSelected(position, view);
        if (mType == CLICKABLE_TAG) {
            view.setBackground(mContext.getDrawable(R.drawable.common_bg_tag_gray));
            TextView tagName = view.findViewById(R.id.txt_tag_name);
            tagName.setTextColor(mContext.getResources().getColor(R.color.txt_screen_gray));
        }
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getSystem().getDisplayMetrics().density * dp + 0.5f);
    }
}
