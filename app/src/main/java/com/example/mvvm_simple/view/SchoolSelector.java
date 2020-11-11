package com.example.mvvm_simple.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvvm_simple.R;

import androidx.annotation.Nullable;

/**
 * Time:2020/2/28 18:32
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SchoolSelector extends LinearLayout {

    private TextView mTxtProvince;
    private TextView mTxtCity;
    private TextView mTxtSchool;
    private ImageView mImgPull;
    public enum ClickType {
        //放下
        PULL_DOWN,

        //收起
        PULL_UP
    }

    public interface OnSelectorPullListener {
        void onPullDown();
        void onPullUp(SchoolSelector schoolSelector);
    }

    private OnSelectorPullListener listener;

    private ClickType type;

    public SchoolSelector(Context context) {
        super(context);
        initView();
    }

    public SchoolSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SchoolSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_select_school, this);
        mTxtProvince = view.findViewById(R.id.layout_school_selector_txt_province);
        mTxtCity = view.findViewById(R.id.layout_school_selector_txt_city);
        mTxtSchool = view.findViewById(R.id.layout_school_selector_txt_school);
        mImgPull = view.findViewById(R.id.layout_school_selector_img_pull);
        type = ClickType.PULL_UP;
        this.setOnClickListener(v -> {
            if (type == ClickType.PULL_UP) {
                type = ClickType.PULL_DOWN;
                mImgPull.setImageResource(R.mipmap.common_ic_pull_down);
                listener.onPullDown();
            } else {
                type = ClickType.PULL_UP;
                mImgPull.setImageResource(R.mipmap.common_ic_pull_up);
                listener.onPullUp(this);
            }
        });
    }

    public void setPullListener(OnSelectorPullListener listener) {
        this.listener = listener;
    }

    public void setmTxtProvince(String province) {
        mTxtProvince.setText(province);
    }

    public void setmTxtCity(String city) {
        mTxtCity.setText(city);
    }

    public void setmTxtSchool(String school) {
        mTxtSchool.setText(school);
    }

    public void setContent(String province, String city, String school) {
        mTxtProvince.setText(province);
        mTxtCity.setText(city);
        mTxtSchool.setText(school);
    }

    public SelectorContent getContent() {
        return new SelectorContent(
                mTxtProvince.getText().toString(),
                mTxtCity.getText().toString(),
                mTxtSchool.getText().toString()
        );
    }

    public class SelectorContent {
        String province;
        String city;
        String school;

        public SelectorContent(String province, String city, String school) {
            this.province = province;
            this.city = city;
            this.school = school;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getSchool() {
            return school;
        }
    }
}
