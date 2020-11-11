package com.example.lib_common.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.lib_common.R;

import androidx.core.content.ContextCompat;

/**
 * Time:2020/2/23 8:27
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class RelativeButton extends RelativeLayout {

    private int mTxtNormalColor;
    private int mTxtEnableColor;

    private int mBtnNormalBack;
    private int mBtnEnableBack;

    private String mCenterTxt;

    private RelativeLayout mBtnBack;
    private TextView mCenterTxtView;
    private ProgressBar mProgressBar;
    private ImageView mImageError;
    private BtnType currentType;

    public RelativeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RelativeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RelativeButton);

        mTxtNormalColor = typedArray.getColor(R.styleable.RelativeButton_normal_txt, ContextCompat.getColor(context, R.color.white));
        mTxtEnableColor = typedArray.getColor(R.styleable.RelativeButton_enable_txt, ContextCompat.getColor(context, R.color.white));

        mBtnEnableBack = typedArray.getResourceId(R.styleable.RelativeButton_enable_btn, R.drawable.common_bg_rl_btn_complete);
        mBtnNormalBack = typedArray.getResourceId(R.styleable.RelativeButton_normal_btn, R.drawable.common_rl_btn_normal);

        mCenterTxt = typedArray.getString(R.styleable.RelativeButton_center_txt);

        View view = LayoutInflater.from(context).inflate(R.layout.common_relative_btn, this);
        mBtnBack = view.findViewById(R.id.rl_btn_container);
        mCenterTxtView = view.findViewById(R.id.rl_btn_txt);
        mProgressBar = view.findViewById(R.id.rl_btn_pg_bar);
        mImageError = view.findViewById(R.id.rl_btn_img_error);
        mCenterTxtView.setText(mCenterTxt);
        mCenterTxtView.setTextColor(mTxtNormalColor);
        mBtnBack.setBackground(getResources().getDrawable(mBtnNormalBack));
        typedArray.recycle();
        currentType = BtnType.NORMAL;
        mProgressBar.setVisibility(GONE);
        mCenterTxtView.setVisibility(VISIBLE);
        mBtnBack.setVisibility(VISIBLE);
        mImageError.setVisibility(GONE);
    }

    public void setType(BtnType type) {
        currentType = type;
        if (type == BtnType.NORMAL) {
            setErrorVisible(false);
            setProgressBarVisible(false);
            setBtnIsNormal(true);
        } else if (type == BtnType.ENABLE) {
            setErrorVisible(false);
            setProgressBarVisible(false);
            setBtnIsNormal(false);
        } else if (type == BtnType.PROGRESSING){
            setBtnVisibility(false);
            setErrorVisible(false);
            setProgressBarVisible(true);
        } else {
            setErrorVisible(true);
            setProgressBarVisible(false);
            setBtnVisibility(false);
        }
    }

    public void setBtnIsNormal(boolean isNormal) {
        mBtnBack.setVisibility(VISIBLE);
        mCenterTxtView.setVisibility(VISIBLE);
        mBtnBack.setBackground(getResources().getDrawable(isNormal ? mBtnNormalBack : mBtnEnableBack));
        mCenterTxtView.setTextColor(isNormal ? mTxtNormalColor : mTxtEnableColor);
    }

    public void setBtnVisibility(boolean isVisible) {
       if (!isVisible) {
           mBtnBack.setBackgroundColor(Color.parseColor("#00000000"));
       }
        mCenterTxtView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setProgressBarVisible(boolean isProgressing) {
        mProgressBar.setVisibility(isProgressing ? VISIBLE : GONE);
    }

    public void setErrorVisible(boolean isVisible) {
        mImageError.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public BtnType getCurrentType() {
        return currentType;
    }

    public enum BtnType {
        NORMAL,

        ENABLE,

        PROGRESSING,

        ERROR
    }

}
