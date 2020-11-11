package com.example.found.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.found.R;
import com.example.found.model.bean.HotSearchBean;
import com.example.lib_common.widget.flowlayout.FlowLayout;
import com.example.lib_common.widget.flowlayout.TagAdapter;

import java.util.List;

/**
 * @author DuLong
 * @since 2020/3/3
 * email 798382030@qq.com
 */
public class HotSearchTagAdapter extends TagAdapter<HotSearchBean> {

    private Context mContext;

    public HotSearchTagAdapter(List<HotSearchBean> datas, Context mContext) {
        super(datas);
        this.mContext = mContext;
    }

    @Override
    public View getView(FlowLayout parent, int position, HotSearchBean mHotSearchBean) {
        LinearLayout mLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
        TextView mTextView = mLayout.findViewById(R.id.txt_tag_name);
        mTextView.setText(mHotSearchBean.getContent());
        //在标签的左端添加火焰
        if (mHotSearchBean.isManagerSet()) {
            ImageView image = new ImageView(mContext);
            image.setImageResource(R.mipmap.ic_small_fire);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(12), dp2px(12));
            lp.leftMargin = dp2px(2);
            mLayout.addView(image, lp);
        }
        return mLayout;
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getSystem().getDisplayMetrics().density * dp + 0.5f);
    }
}
