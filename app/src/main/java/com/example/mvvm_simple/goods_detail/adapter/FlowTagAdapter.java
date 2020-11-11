package com.example.mvvm_simple.goods_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lib_common.SPUtils;
import com.example.lib_common.widget.flowlayout.FlowLayout;
import com.example.lib_common.widget.flowlayout.TagAdapter;
import com.example.lib_data.data_user.token.TokenManager;
import com.example.mvvm_simple.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.IntDef;

public class FlowTagAdapter extends TagAdapter<String> {

    private Context mContext;
    private int mType;

    private HashMap<String, String> labelsMap;
    public static final int NORMAL = 0;
    public static final int HOT_TAG = 1;
    public static final int CLEAR_TAG = 2;
    public static final int YELLOW_TAG = 3;
    public static final int CLICKABLE_TAG = 4;
    public static final int SMALL_YELLOW_TAG = 5;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOT_TAG, CLEAR_TAG, NORMAL, YELLOW_TAG, CLICKABLE_TAG, SMALL_YELLOW_TAG})
    public @interface TagType {}

    public FlowTagAdapter(List<String> datas, Context context, @TagType int type) {
        super(datas);
        List<String> temp = new ArrayList<>(datas);
        mContext = context;
        mType = type;
        labelsMap = TokenManager.getLabelMap();
        mTagDatas.clear();
        for (String item :
                temp) {
            mTagDatas.add(labelsMap.get(item));
        }
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
        }
        return mLayout;
    }


    private int dp2px(float dp) {
        return (int) (mContext.getResources().getSystem().getDisplayMetrics().density * dp + 0.5f);
    }

    public void unRegister() {
        mContext = null;
    }
}