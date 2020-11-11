package com.example.login.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.lib_common.textview.CountDownTv;
import com.example.login.R;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/22 20:14
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 获得验证码的流程太繁琐，抽取出来，方便使用
 */
public class MultiVerifyRelativeLayout extends RelativeLayout {

    private RelativeLayout mBtnGetVerify;
    private CountDownTv mCountDownTv;
    private TextView mTxtBtn;
    private int mBtnEnableRes;
    private int mBtnNormalRes;
    private int mTxtNormalRes;
    private int mTxtEnableRes;
    private long mCountDownTotalTime;
    private long mCountDownPerTime;
    private String mCountDownUnit;
    private CountDownTv.CountDownListener mCountDownListener;
    private MutableLiveData<Boolean> isFinish;
    private MutableLiveData<Boolean> isBeginToStart;
    private MutableLiveData<Long> onTick;

    public MultiVerifyRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public MultiVerifyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiVerifyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

       View view = LayoutInflater.from(context).inflate(R.layout.login_layout_multi_verify, this);
       mBtnGetVerify = view.findViewById(R.id.login_btn_multi_verify);
       mCountDownTv = view.findViewById(R.id.login_txt_multi_verify_count_down);
       mTxtBtn = view.findViewById(R.id.login_txt_multi_verify);

       isFinish = new MutableLiveData<>(false);
       isBeginToStart = new MutableLiveData<>(false);
       onTick = new MutableLiveData<>();

       mBtnNormalRes = R.drawable.login_bg_login_btn_verify_normal;
       mBtnEnableRes = R.drawable.login_bg_login_btn_enable_verify;
       mTxtNormalRes = R.color.login_grey_txt;
       mTxtEnableRes = R.color.login_white;
       mCountDownTotalTime = 60 * 1000;
       mCountDownPerTime = 1000;
       mCountDownUnit = "s";

       mCountDownListener = new CountDownTv.CountDownListener() {
           @Override
           public void onFinish(TextView textView) {
               isFinish.setValue(true);
           }

           @Override
           public void onTick(Long time) {
               onTick.setValue(time);
           }

           @Override
           public void beforeStart() {
               isBeginToStart.setValue(true);
           }
       };
       mCountDownTv.setOnFinishListener(mCountDownListener);
    }

    public MultiVerifyRelativeLayout setBtnRes(int btnEnableRes, int btnNormalRes)  {
        this.mBtnEnableRes = btnEnableRes;
        this.mBtnNormalRes = btnNormalRes;
        return this;
    }


    public MultiVerifyRelativeLayout setTxtRes(int txtEnableRes, int txtNormalRes) {
        this.mTxtEnableRes = txtEnableRes;
        this.mTxtNormalRes = txtNormalRes;
        return this;
    }

    public MultiVerifyRelativeLayout setCountDown(long totalTime, long perTime, String unit) {
        this.mCountDownTotalTime = totalTime;
        this.mCountDownPerTime = perTime;
        this.mCountDownUnit = unit;
        return this;
    }

    public MultiVerifyRelativeLayout setCallbackForCountDown(CountDownTv.CountDownListener listener) {
        this.mCountDownListener = listener;
        mCountDownTv.setOnFinishListener(mCountDownListener);
        return this;
    }

    public MultiVerifyRelativeLayout setBtnClickListener(OnClickListener listener) {
        mBtnGetVerify.setOnClickListener(listener);
        return this;
    }

    public void allReady() {

    }



    public void setType(MultiVerifyType type) {

        switch (type) {
            case NORMAL:
                letBtnNormal();
                letCountDown(false);
                break;
            case ENABLE:
                letBtnEnable();
                letCountDown(false);
                break;
            case COUNT_DOWN:
                letCountDown(true);

        }

    }

    private void letCountDown(boolean isCountDown) {
//        if (!isCountDown) {
//            mCountDownTv.stopCountDown();
//        }

        mBtnGetVerify.setVisibility(isCountDown ? GONE : VISIBLE);
        mCountDownTv.setVisibility(isCountDown ? VISIBLE : GONE);

        if (isCountDown) {
            this.setClickable(false);
            mCountDownTv.beginCountDown(mCountDownTotalTime, mCountDownPerTime, mCountDownUnit);
        }

    }

    private void letBtnEnable() {
        mTxtBtn.setTextColor(ContextCompat.getColor(this.getContext(), mTxtEnableRes));
        mBtnGetVerify.setBackground(ContextCompat.getDrawable(this.getContext(), mBtnEnableRes));
        mBtnGetVerify.setClickable(true);
    }


    private void letBtnNormal() {
        mTxtBtn.setTextColor(ContextCompat.getColor(this.getContext(), mTxtNormalRes));
        mBtnGetVerify.setBackground(ContextCompat.getDrawable(this.getContext(), mBtnNormalRes));
        mBtnGetVerify.setClickable(false);
    }

    public enum MultiVerifyType {
        NORMAL,

        ENABLE,

        COUNT_DOWN
    }

    public MutableLiveData<Boolean> getIsFinish() {
        return isFinish;
    }

    public MutableLiveData<Boolean> getIsBeginToStart() {
        return isBeginToStart;
    }

    public MutableLiveData<Long> getOnTick() {
        return onTick;
    }
}
