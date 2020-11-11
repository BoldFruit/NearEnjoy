package com.example.login.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.login.R;

import androidx.core.content.ContextCompat;

/**
 * Time:2020/2/23 8:27
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginRelativeBtn extends RelativeLayout {

    private int mTxtNormalColor;
    private int mTxtEnableColor;

    private int mBtnNormalBack;
    private int mBtnEnableBack;

    private String mCenterTxt;

    private RelativeLayout mBtnBack;
    private TextView mCenterTxtView;

    public LoginRelativeBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoginRelativeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RelativeButton);

        mTxtNormalColor = typedArray.getColor(R.styleable.RelativeButton_normal_txt, ContextCompat.getColor(context, R.color.login_grey_txt));
        mTxtEnableColor = typedArray.getColor(R.styleable.RelativeButton_enable_txt, ContextCompat.getColor(context, R.color.login_white));

        mBtnEnableBack = typedArray.getResourceId(R.styleable.RelativeButton_enable_btn, R.drawable.login_bg_login_btn_enter_main_complete);
        mBtnNormalBack = typedArray.getResourceId(R.styleable.RelativeButton_normal_btn, R.drawable.login_bg_login_btn_enter_main_normal);

        mCenterTxt = typedArray.getString(R.styleable.RelativeButton_center_txt);

        View view = LayoutInflater.from(context).inflate(R.layout.login_layout_login_btn, this);
        mBtnBack = view.findViewById(R.id.login_btn_container);
        mCenterTxtView = view.findViewById(R.id.login_btn_txt);
        mCenterTxtView.setText(mCenterTxt);
        mCenterTxtView.setTextColor(mTxtNormalColor);
        mBtnBack.setBackground(getResources().getDrawable(mBtnNormalBack));
        typedArray.recycle();
    }

    public void setType(BtnType type) {
        if (type == BtnType.NORMAL) {
            mCenterTxtView.setTextColor(mTxtNormalColor);
            mBtnBack.setBackground(getResources().getDrawable(mBtnNormalBack));

        } else {
            mCenterTxtView.setTextColor(mTxtEnableColor);
            mBtnBack.setBackground(getResources().getDrawable(mBtnEnableBack));
        }
    }

    public enum BtnType {
        NORMAL,

        ENABLE
    }

}
